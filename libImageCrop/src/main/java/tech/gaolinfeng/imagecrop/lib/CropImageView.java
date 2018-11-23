package tech.gaolinfeng.imagecrop.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by gaolf on 15/12/21.
 * 在同步地设置了图片后，需要调用startCrop初始化，此时会根据图片大小做初始化工作；如果指定cropScaleType为NONE，
 * 则使用默认的scaleType对图片做初始缩放，如果发现默认的scaleType缩放图片后，图片比裁剪框大小还要小，就将图片放大到和裁剪框一样大。
 *
 * 使用方式参考ImageCropActivity
 */
public class CropImageView extends ImageView {

    public enum CropScaleType {
        CROP_SCALE_TYPE_NONE,                 // 按照图片原scaleType尺寸显示，
        CROP_SCALE_TYPE_HORIZONTAL,           // 如果图片在缩放(最大放大倍数=MAX_ZOOM_RELATIVE_TO_INTRINSIC)后可以横向填满屏幕，则缩放图片并横向填满屏幕
    }

    private enum TouchState {
        TOUCH_STATE_UNKNOWN,            // 两根手指以上 ---- 用户想干嘛？？
        TOUCH_STATE_IDLE,               // 没有拖动或缩放
        TOUCH_STATE_DRAG,               // 拖动
        TOUCH_STATE_ZOOM,               // 缩放
    }

    private static final float MAX_ZOOM_RELATIVE_TO_INTRINSIC = 3;

    private Matrix matrix;                                                          // image's controlling matrix
    private MatrixHelper matrixHelper;
    private TouchState touchState = TouchState.TOUCH_STATE_IDLE;                    // 当前的交互

    private Rect edgeRect;                                                          // 裁剪区域
    private CropScaleType cropScaleType;                                            // 非标准的scaleType，见CropScaleType
    private boolean cropCircle = true;                                              // 是否裁成圆形
    private boolean blockTouchEvent = false;                                        // 禁用交互 - 当前正在自动缩放

    private ScaleGestureDetector scaleGestureDetector;                              // 多指缩放
    private GestureDetector gestureDetector;                                        // 单指手势
    private Scroller flingScroller;                                                 // 处理fling时滑动的scroller

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public CropImageView(Context context) {
        super(context);
        init();
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        matrixHelper = new MatrixHelper();
        scaleGestureDetector = new ScaleGestureDetector(getContext(), onScaleGestureListener);
        gestureDetector = new GestureDetector(getContext(), onGestureListener);
        flingScroller = new Scroller(getContext());
        maskPaint = new Paint();
        maskPaint.setColor(Color.argb(255 / 2, 0, 0, 0));
        maskPaint.setStyle(Paint.Style.FILL);
        cropScaleType = CropScaleType.CROP_SCALE_TYPE_HORIZONTAL;
    }

    public void startCrop() {
        startCrop(null);
    }

    public void startCrop(final Runnable onMatrixObtainedRunnable) {
        requestLayout();
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isLayoutRequested()) {
                    return true;
                }
                getViewTreeObserver().removeOnPreDrawListener(this);
                matrix = getImageMatrix();
                setImageMatrix(matrix);
                setScaleType(ScaleType.MATRIX);
                matrixHelper.init(matrix, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
                switch (cropScaleType) {
                    case CROP_SCALE_TYPE_NONE:
                        // do nothing
                        break;
                    case CROP_SCALE_TYPE_HORIZONTAL:
                        if (getWidth() > matrixHelper.getWidth()) {
//                            Log.e("gaolf", "screen width > show width, screen width: " + getWidth() + ", show width: " + matrixHelper);
                            if (matrixHelper.getIntrinsicWidth() * MAX_ZOOM_RELATIVE_TO_INTRINSIC > getWidth()) {
//                                Log.e("gaolf", "raw width * 3: " + matrixHelper.getIntrinsicWidth() * MAX_ZOOM_RELATIVE_TO_INTRINSIC);
                                scale = getWidth() / matrixHelper.getIntrinsicWidth();
                                matrix.getValues(matrixValues);
                                float currScale = matrixValues[Matrix.MSCALE_X];
                                matrix.postScale(scale / currScale, scale / currScale, edgeRect.left + edgeRect.width() / 2, edgeRect.top + edgeRect.height() / 2);
                                matrixHelper.update(matrix);
//                                Log.e("gaolf", "after scale, show width: " + matrixHelper.getWidth());
                                scale = 1;
                            }
                        }
                        break;
                }
                onScale();
                onTranslate();
                if (onMatrixObtainedRunnable != null) {
                    onMatrixObtainedRunnable.run();
                }
                return false;
            }
        });
    }

    public void setCropCircle(boolean cropCircle) {
        this.cropCircle = cropCircle;
    }

    public void setCropScaleType(CropScaleType cropScaleType) {
        this.cropScaleType = cropScaleType;
    }

    public void setEdge(Rect edgeRect) {
        this.edgeRect = new Rect(edgeRect);
        invalidate();
    }

    public RectF getCurrentRect() {
        RectF rect = new RectF(0, 0, matrixHelper.getIntrinsicWidth(), matrixHelper.getIntrinsicHeight());
        matrix.mapRect(rect);
        return rect;
    }

    public float getRawWidth() {
        return matrixHelper.getIntrinsicWidth();
    }

    public float getRawHeight() {
        return matrixHelper.getIntrinsicHeight();
    }

    private void onScale() {
        matrixHelper.update(matrix);
        if (matrixHelper.getWidth() < edgeRect.width()) {
            scaleX = edgeRect.width() / matrixHelper.getWidth();
        }
        if (matrixHelper.getHeight() < edgeRect.height()) {
            scaleY = edgeRect.height() / matrixHelper.getHeight();
        }

        if (scaleX != 1 || scaleY != 1) {
//            Log.e("gaolf", "scale fixed, scaleX: " + scaleX + ", scaleY: " + scaleY);
            scale = Math.max(scaleX, scaleY);
            matrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            matrixHelper.update(matrix);
            scaleX = 1;
            scaleY = 1;
            scale = 1;
        }
        setImageMatrix(matrix);
        invalidate();
    }

    private void fixConstraints() {
        onScale();
        onTranslate();
    }

    private void onTranslate() {
        matrixHelper.update(matrix);
        if (matrixHelper.getLeft() > edgeRect.left) {
            dx = edgeRect.left - matrixHelper.getLeft();
        } else if (matrixHelper.getRight() < edgeRect.right) {
            dx = edgeRect.right - matrixHelper.getRight();
        }
        if (matrixHelper.getTop() > edgeRect.top) {
            dy = edgeRect.top - matrixHelper.getTop();
        } else if (matrixHelper.getBottom() < edgeRect.bottom) {
            dy = edgeRect.bottom - matrixHelper.getBottom();
        }
        if (dx != 0 || dy != 0) {
            matrix.postTranslate(dx, dy);
            matrixHelper.update(matrix);
            dx = 0;
            dy = 0;
        }
        setImageMatrix(matrix);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (flingScroller.computeScrollOffset()) {
            matrix.postTranslate(flingScroller.getCurrX() - matrixHelper.getLeft(), flingScroller.getCurrY() - matrixHelper.getTop());
            matrixHelper.update(matrix);
//            Log.e("gaolf", "scroll, left: " + matrixHelper.getLeft() + ", right: " + matrixHelper.getRight());
            onTranslate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (cropCircle) {
            try {
                // 截个圆出来
                canvas.save();
                circlePath.reset();
                circlePath.addCircle((edgeRect.right + edgeRect.left) / 2, (edgeRect.bottom + edgeRect.top) / 2, (edgeRect.bottom - edgeRect.top) / 2, Path.Direction.CW);
                canvas.clipPath(circlePath, Region.Op.XOR);
                canvas.drawRect(0, 0, getWidth(), getHeight(), maskPaint);
                canvas.restore();
            } catch (Exception e) {
                e.printStackTrace();
                canvas.restore();
                cropCircle = false;
            }
        }

        if (!cropCircle) {
            canvas.drawRect(0, 0, getWidth(), edgeRect.top, maskPaint);                                         //top
            canvas.drawRect(0, edgeRect.bottom, getWidth(), getHeight(), maskPaint);                            //bottom
            canvas.drawRect(0, edgeRect.top, edgeRect.left, edgeRect.bottom, maskPaint);                        //left
            canvas.drawRect(edgeRect.right, edgeRect.top, getWidth(), edgeRect.bottom, maskPaint);              //right
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (matrix == null) {
            // not initialized, don't handle touch event.
            return false;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
//                Log.e("gaolf", "action pointer down: " + event.getActionIndex());
                if (event.getActionIndex() == 1) {
                    touchState = TouchState.TOUCH_STATE_ZOOM;
                } else {
                    touchState = TouchState.TOUCH_STATE_UNKNOWN;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
//                Log.e("gaolf", "action pointer up: " + event.getActionIndex());
                if (event.getActionIndex() == 2) {
                    touchState = TouchState.TOUCH_STATE_ZOOM;
                } else if (event.getActionIndex() == 1){
                    touchState = TouchState.TOUCH_STATE_DRAG;
                } else {
                    touchState = TouchState.TOUCH_STATE_UNKNOWN;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                flingScroller.abortAnimation();
//                Log.e("gaolf", "action down: " + event.getActionIndex());
                touchState = TouchState.TOUCH_STATE_DRAG;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                Log.e("gaolf", "action up/cancel: " + event.getActionIndex());
                touchState = TouchState.TOUCH_STATE_IDLE;
                break;
        }

        switch (touchState) {
            case TOUCH_STATE_DRAG:
            case TOUCH_STATE_IDLE:
                gestureDetector.onTouchEvent(event);
                break;
            case TOUCH_STATE_ZOOM:
                scaleGestureDetector.onTouchEvent(event);
                break;
        }

        return true;
    }

    private ScaleGestureDetector.OnScaleGestureListener onScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (blockTouchEvent) {
                return true;
            }
            scaleFactor = detector.getScaleFactor();
            matrix.getValues(matrixValues);
            if (matrixValues[Matrix.MSCALE_X] * scaleFactor > MAX_ZOOM_RELATIVE_TO_INTRINSIC) {
                // 缩放后，缩放比过大
                if (edgeRect.width() / matrixHelper.getIntrinsicWidth() > MAX_ZOOM_RELATIVE_TO_INTRINSIC
                        || edgeRect.height() / matrixHelper.getIntrinsicHeight() > MAX_ZOOM_RELATIVE_TO_INTRINSIC) {
                    // 原图就比裁剪框小这么多倍..完全没有意义，不让用户缩放
                    // do nothing
                    scaleFactor = 1;
                } else {
                    // 设置到最大缩放倍数
                    scaleFactor = MAX_ZOOM_RELATIVE_TO_INTRINSIC / matrixValues[Matrix.MSCALE_X];
                }
            }
            matrix.postScale(scaleFactor, scaleFactor, edgeRect.left + edgeRect.width() / 2, edgeRect.top + edgeRect.height() / 2);

            fixConstraints();
            return true;
        }
    };

    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (blockTouchEvent) {
                return true;
            }
            matrix.postTranslate(-distanceX, -distanceY);
            fixConstraints();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // on my nexus6p, onFling seems to be too sensitive..
//            Log.e("gaolf", "onFling, velocityX: " + velocityX + ", velocityY: " + velocityY);
            if (Math.abs(velocityX * velocityX + velocityY * velocityY) < 1000 * 1000 * getResources().getDisplayMetrics().density) {
                return true;
            }
            matrix.getValues(matrixValues);
//            Log.e("gaolf", "onFling, transX: " + (int) matrixValues[Matrix.MTRANS_X] + ", transY: " + (int)matrixValues[Matrix.MTRANS_Y] +
//                ", velocityX: " + velocityX + ", velocityY: " + velocityY);
            flingScroller.fling((int) matrixValues[Matrix.MTRANS_X], (int)matrixValues[Matrix.MTRANS_Y], (int) velocityX, (int) velocityY,
                    Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            invalidate();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (blockTouchEvent) {
                return true;
            }
//            Log.e("gaolf", "raw: (" + matrixHelper.getIntrinsicWidth() + ", " + matrixHelper.getIntrinsicHeight() + ")");
            matrix.getValues(matrixValues);
            float currentScale = matrixValues[Matrix.MSCALE_X];
            if (currentScale >= MAX_ZOOM_RELATIVE_TO_INTRINSIC - 0.01) {
                // zoom out
                float targetScale = Math.max(edgeRect.width() / matrixHelper.getIntrinsicWidth(), edgeRect.height() / matrixHelper.getIntrinsicHeight());
//                Log.e("gaolf", "zoom out now, scaleX: " + edgeRect.width() / matrixHelper.getIntrinsicWidth()
//                        + ", scaleY: " + edgeRect.height() / matrixHelper.getIntrinsicHeight());
//                Log.e("gaolf", "targetScale: " + targetScale);
                mainHandler.post(new AutoScaleRunnable(currentScale, targetScale, matrixHelper.getWidth(), matrixHelper.getHeight()));
            } else {
                // zoom in
                float targetScale = currentScale * 2;
                if (targetScale + 0.5 > MAX_ZOOM_RELATIVE_TO_INTRINSIC) {
                    // 缩放后，缩放比过大
                    if (edgeRect.width() / matrixHelper.getIntrinsicWidth() > MAX_ZOOM_RELATIVE_TO_INTRINSIC
                            || edgeRect.height() / matrixHelper.getIntrinsicHeight() > MAX_ZOOM_RELATIVE_TO_INTRINSIC) {
                        // 原图就比裁剪框小这么多倍..完全没有意义，不让用户缩放
                        // do nothing
                        targetScale = 1;
                    } else {
                        targetScale = MAX_ZOOM_RELATIVE_TO_INTRINSIC;
                    }
                }
                mainHandler.post(new AutoScaleRunnable(currentScale, targetScale, matrixHelper.getWidth(), matrixHelper.getHeight()));
                matrix.postScale(scaleFactor, scaleFactor, edgeRect.left + edgeRect.width() / 2, edgeRect.top + edgeRect.height() / 2);
            }
            return true;
        }
    };

    private class AutoScaleRunnable implements Runnable {
        private static final int SCALE_ANIMATION_FRAME_COUNT = 20;
        private float targetScale;
        private float fromScale;
        private float fromWidth;
        private float fromHeight;
        private int currentFrame;

        public AutoScaleRunnable(float fromScale, float targetScale, float fromWidth, float fromHeight) {
            this(fromScale, targetScale, 0, fromWidth, fromHeight);
        }

        public AutoScaleRunnable(float fromScale, float targetScale, int currentFrame, float fromWidth, float fromHeight) {
            this.fromScale = fromScale;
            this.targetScale = targetScale;
            this.currentFrame = currentFrame;
            this.fromWidth = fromWidth;
            this.fromHeight = fromHeight;
        }

        @Override
        public void run() {
            blockTouchEvent = true;
            currentFrame++;
            float scaleStep = (targetScale - fromScale) / SCALE_ANIMATION_FRAME_COUNT;
            float currentScale = fromScale + scaleStep * currentFrame;
            matrix.getValues(matrixValues);
            float prevScale = matrixValues[Matrix.MSCALE_X];
            matrix.postScale(currentScale / prevScale, currentScale / prevScale, edgeRect.left + edgeRect.width() / 2, edgeRect.top + edgeRect.height() / 2);
            fixConstraints();

//            matrixHelper.update(matrix);
//            setImageMatrix(matrix);
//            invalidate();
//
//            matrix.getValues(matrixValues);
//            Log.e("gaolf", "matrix transX: " + matrixValues[Matrix.MTRANS_X] + ", transY: " + matrixValues[Matrix.MTRANS_Y]);
            if (currentFrame < SCALE_ANIMATION_FRAME_COUNT) {
                mainHandler.post(new AutoScaleRunnable(fromScale, targetScale, currentFrame, fromWidth, fromHeight));
            } else {
                blockTouchEvent = false;
            }
        }
    }

    private float dx = 0, dy = 0;
    private float scale = 1, scaleX = 1, scaleY = 1;
    private Paint maskPaint;
    private Path circlePath = new Path();
    private float[] matrixValues = new float[9];
    private float scaleFactor = 1/*, scaleFactorX = 1, getScaleFactorY = 1*/;
}
