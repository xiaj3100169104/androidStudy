/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.R;
import com.google.zxing.ResultPoint;
import com.google.zxing.camera.CameraManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 80L;
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;

    private static final int SCANNER_LINE_MOVE_DISTANCE = 16;
    private static final int SCANNER_LINE_HEIGHT = 6;
    private static final int CORNER_RECT_WIDTH = 8;
    private static final int CORNER_RECT_HEIGHT = 40;

    private static final boolean DEBUG = false;


    private CameraManager cameraManager;
    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int laserColor;
    private final int resultPointColor;
    private final int cornerColor;
    private final int frameColor;
    private final int labelTextColor;
    private final float labelTextSize;
    private String labelText;
    private List<ResultPoint> possibleResultPoints;
    private List<ResultPoint> lastPossibleResultPoints;

    public int scannerStart = 0;
    public int scannerEnd = 0;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        /*Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        laserColor = resources.getColor(R.color.viewfinder_laser);
        resultPointColor = resources.getColor(R.color.possible_result_points);  // */

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewfinderView);
        laserColor = array.getColor(R.styleable.ViewfinderView_laser_color, 0x00FF00);
        cornerColor = array.getColor(R.styleable.ViewfinderView_corner_color, 0x00FF00);
        frameColor = array.getColor(R.styleable.ViewfinderView_frame_color, 0xFFFFFF);
        resultPointColor = array.getColor(R.styleable.ViewfinderView_result_point_color, 0xC0FFFF00);
        maskColor = array.getColor(R.styleable.ViewfinderView_mask_color, 0x60000000);
        resultColor = array.getColor(R.styleable.ViewfinderView_result_color, 0xB0000000);
        labelTextColor = array.getColor(R.styleable.ViewfinderView_label_text_color, 0x90FFFFFF);
        labelText = array.getString(R.styleable.ViewfinderView_label_text);
        labelTextSize = array.getFloat(R.styleable.ViewfinderView_label_text_size, 36f);

        possibleResultPoints = new ArrayList<>(5);
        lastPossibleResultPoints = null;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        Rect frame = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        int orientation = cameraManager.getCameraOrientation();
        if (frame == null || previewFrame == null) {
            return;
        }

        if(scannerStart == 0 || scannerEnd == 0) {
            scannerStart = frame.top;
            scannerEnd = frame.bottom;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.save();
            canvas.rotate(orientation, frame.exactCenterX(), frame.exactCenterY());
            canvas.drawBitmap(resultBitmap, null, frame, paint);
            canvas.restore();
        } else {
            drawFrame(canvas, frame); //画框框
            drawCorner(canvas, frame);
            drawScanLine(canvas, frame);
            drawTextInfo(canvas, frame);
            if (DEBUG) {
                drawResultPoints(canvas, frame, previewFrame, orientation);
            }

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY,
                    frame.left - POINT_SIZE,
                    frame.top - POINT_SIZE,
                    frame.right + POINT_SIZE,
                    frame.bottom + POINT_SIZE);
        }
    }

    private void drawTextInfo(Canvas canvas, Rect frame) {
        paint.setColor(labelTextColor);
        paint.setTextSize(labelTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(labelText, frame.left + frame.width() / 2, frame.bottom + CORNER_RECT_HEIGHT * 2, paint);
    }

    private void drawFrame(Canvas canvas, Rect frame) {
        paint.setColor(frameColor);
        canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
        canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
        canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
        canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);
    }

    //绘制边角
    private void drawCorner(Canvas canvas, Rect frame) {
        paint.setColor(cornerColor);
        //左上
        canvas.drawRect(frame.left, frame.top, frame.left + CORNER_RECT_WIDTH, frame.top + CORNER_RECT_HEIGHT, paint);
        canvas.drawRect(frame.left, frame.top, frame.left + CORNER_RECT_HEIGHT, frame.top + CORNER_RECT_WIDTH, paint);
        //右上
        canvas.drawRect(frame.right - CORNER_RECT_WIDTH, frame.top, frame.right, frame.top + CORNER_RECT_HEIGHT, paint);
        canvas.drawRect(frame.right - CORNER_RECT_HEIGHT, frame.top, frame.right, frame.top + CORNER_RECT_WIDTH, paint);
        //左下
        canvas.drawRect(frame.left, frame.bottom - CORNER_RECT_WIDTH, frame.left + CORNER_RECT_HEIGHT, frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - CORNER_RECT_HEIGHT, frame.left + CORNER_RECT_WIDTH, frame.bottom, paint);
        //右下
        canvas.drawRect(frame.right - CORNER_RECT_WIDTH, frame.bottom - CORNER_RECT_HEIGHT, frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - CORNER_RECT_HEIGHT, frame.bottom - CORNER_RECT_WIDTH, frame.right, frame.bottom, paint);
    }

    private void drawScanLine(Canvas canvas, Rect frame) {
        Shader shader = new RadialGradient(
                (float)(frame.left + frame.width() / 2),
                (float)(scannerStart + SCANNER_LINE_HEIGHT / 2),
                360f,
                laserColor,
                shadeColor(laserColor),
                Shader.TileMode.MIRROR);
        paint.setAlpha(CURRENT_POINT_OPACITY);
        paint.setShader(shader);
        if (scannerStart <= scannerEnd) {
            canvas.drawRect(frame.left, scannerStart, frame.right, scannerStart + SCANNER_LINE_HEIGHT, paint);
            scannerStart += SCANNER_LINE_MOVE_DISTANCE;
        } else {
            scannerStart = frame.top;
        }
        paint.setShader(null);
    }

    private int shadeColor(int color) {
        String hax = Integer.toHexString(color);
        String result = "20"+hax.substring(2);
        return Integer.valueOf(result, 16);
    }

    private void drawResultPoints(Canvas canvas, Rect frame, Rect previewFrame, int orientation) {
        float scaleX = frame.width() / (float) previewFrame.width();
        float scaleY = frame.height() / (float) previewFrame.height();

        List<ResultPoint> currentPossible = possibleResultPoints;
        List<ResultPoint> currentLast = lastPossibleResultPoints;
        int frameLeft = frame.left;
        int frameTop = frame.top;
        if (currentPossible.isEmpty()) {
            lastPossibleResultPoints = null;
        } else {
            possibleResultPoints = new ArrayList<>(5);
            lastPossibleResultPoints = currentPossible;
            paint.setAlpha(CURRENT_POINT_OPACITY);
            paint.setColor(resultPointColor);
            canvas.save();
            canvas.rotate(orientation, frame.exactCenterX(), frame.exactCenterY());
            synchronized (currentPossible) {
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            POINT_SIZE, paint);
                }
            }
            canvas.restore();
        }
        if (currentLast != null) {
            paint.setAlpha(CURRENT_POINT_OPACITY / 2);
            paint.setColor(resultPointColor);
            canvas.save();
            canvas.rotate(orientation, frame.exactCenterX(), frame.exactCenterY());
            synchronized (currentLast) {
                float radius = POINT_SIZE / 2.0f;
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            radius, paint);
                }
            }
            canvas.restore();
        }
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                // trim it
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }

}


/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.google.zxing.activity;
//
//import com.google.zxing.ResultPoint;
//import com.google.zxing.camera.CameraManager;
//import com.nisec.tcbox.flashdrawer.R;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//import android.view.View;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
// * transparency outside it, as well as the laser scanner animation and result points.
// *
// * @author dswitkin@google.com (Daniel Switkin)
// * 官方
// */
//public final class ViewfinderView extends View {
//
//    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
//    private static final long ANIMATION_DELAY = 80L;
//    private static final int CURRENT_POINT_OPACITY = 0xA0;
//    private static final int MAX_RESULT_POINTS = 20;
//    private static final int POINT_SIZE = 6;
//
//    private CameraManager cameraManager;
//    private final Paint paint;
//    private Bitmap resultBitmap;
//    private final int maskColor;
//    private final int resultColor;
//    private final int laserColor;
//    private final int resultPointColor;
//    private int scannerAlpha;
//    private List<ResultPoint> possibleResultPoints;
//    private List<ResultPoint> lastPossibleResultPoints;
//
//    // This constructor is used when the class is built from an XML resource.
//    public ViewfinderView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        // Initialize these once for performance rather than calling them every time in onDraw().
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        Resources resources = getResources();
//        maskColor = resources.getColor(R.color.viewfinder_mask);
//        resultColor = resources.getColor(R.color.result_view);
//        laserColor = resources.getColor(R.color.viewfinder_laser);
//        resultPointColor = resources.getColor(R.color.possible_result_points);
//        scannerAlpha = 0;
//        possibleResultPoints = new ArrayList<>(5);
//        lastPossibleResultPoints = null;
//    }
//
//    public void setCameraManager(CameraManager cameraManager) {
//        this.cameraManager = cameraManager;
//    }
//
//    @SuppressLint("DrawAllocation")
//    @Override
//    public void onDraw(Canvas canvas) {
//        if (cameraManager == null) {
//            return; // not ready yet, early draw before done configuring
//        }
//        Rect frame = cameraManager.getFramingRect();
//        Rect previewFrame = cameraManager.getFramingRectInPreview();
//        if (frame == null || previewFrame == null) {
//            return;
//        }
//        int width = canvas.getWidth();
//        int height = canvas.getHeight();
//
//        // Draw the exterior (i.e. outside the framing rect) darkened
//        paint.setColor(resultBitmap != null ? resultColor : maskColor);
//        canvas.drawRect(0, 0, width, frame.top, paint);
//        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
//        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
//        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
//
//        if (resultBitmap != null) {
//            // Draw the opaque result bitmap over the scanning rectangle
//            paint.setAlpha(CURRENT_POINT_OPACITY);
//            canvas.drawBitmap(resultBitmap, null, frame, paint);
//        } else {
//
//            // Draw a red "laser scanner" line through the middle to show decoding is active
//            paint.setColor(laserColor);
//            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
//            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//            int middle = frame.height() / 2 + frame.top;
//            canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);
//
//            float scaleX = frame.width() / (float) previewFrame.width();
//            float scaleY = frame.height() / (float) previewFrame.height();
//
//            List<ResultPoint> currentPossible = possibleResultPoints;
//            List<ResultPoint> currentLast = lastPossibleResultPoints;
//            int frameLeft = frame.left;
//            int frameTop = frame.top;
//            if (currentPossible.isEmpty()) {
//                lastPossibleResultPoints = null;
//            } else {
//                possibleResultPoints = new ArrayList<>(5);
//                lastPossibleResultPoints = currentPossible;
//                paint.setAlpha(CURRENT_POINT_OPACITY);
//                paint.setColor(resultPointColor);
//                synchronized (currentPossible) {
//                    for (ResultPoint point : currentPossible) {
//                        canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
//                                frameTop + (int) (point.getY() * scaleY),
//                                POINT_SIZE, paint);
//                    }
//                }
//            }
//            if (currentLast != null) {
//                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
//                paint.setColor(resultPointColor);
//                synchronized (currentLast) {
//                    float radius = POINT_SIZE / 2.0f;
//                    for (ResultPoint point : currentLast) {
//                        canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
//                                frameTop + (int) (point.getY() * scaleY),
//                                radius, paint);
//                    }
//                }
//            }
//
//            // Request another update at the animation interval, but only repaint the laser line,
//            // not the entire viewfinder mask.
//            postInvalidateDelayed(ANIMATION_DELAY,
//                    frame.left - POINT_SIZE,
//                    frame.top - POINT_SIZE,
//                    frame.right + POINT_SIZE,
//                    frame.bottom + POINT_SIZE);
//        }
//    }
//
//    public void drawViewfinder() {
//        Bitmap resultBitmap = this.resultBitmap;
//        this.resultBitmap = null;
//        if (resultBitmap != null) {
//            resultBitmap.recycle();
//        }
//        invalidate();
//    }
//
//    /**
//     * Draw a bitmap with the result points highlighted instead of the live scanning display.
//     *
//     * @param barcode An image of the decoded barcode.
//     */
//    public void drawResultBitmap(Bitmap barcode) {
//        resultBitmap = barcode;
//        invalidate();
//    }
//
//    public void addPossibleResultPoint(ResultPoint point) {
//        List<ResultPoint> points = possibleResultPoints;
//        synchronized (points) {
//            points.add(point);
//            int size = points.size();
//            if (size > MAX_RESULT_POINTS) {
//                // trim it
//                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
//            }
//        }
//    }
//}
