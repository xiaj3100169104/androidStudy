package example.drag.swipeMenu;

import android.animation.ValueAnimator;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.dmcbig.mediapicker.utils.ScreenUtils;

/**
 * 左滑菜单RecyclerView：
 * 注意:在适配器里设置item内容区宽度为屏幕宽度，总宽度为屏幕宽度加菜单宽度.
 * 在onBindViewHolder中调用holder.setIsRecyclable(false);
 * 在notifyDataSetChanged前调用 一下bd.recyclerView.closeMenuFromOpen();
 * 不要调用notifyItemChanged，会有奇怪现象。
 * 方案二：在确定要打开菜单时在子布局上动态添加菜单布局，是否可行待验证
 */
public class SwipeMenuRecyclerView extends RecyclerView {
    protected final String TAG = getClass().getSimpleName();

    private int screenWidth;
    //滑动开始与点击事件的位移临界值
    private int mTouchSlop;
    //是否处于手指拖动中
    private boolean mIsBeingDragged = false;
    //最大偏移，正值代表向右，负值代表向左，绝对值就是菜单宽度
    private int xMaxOffset;
    private float xDown;
    private float yDown;
    private float xLastMove;
    private ValueAnimator va;
    private boolean isMenuOpen;
    private View menuChild;

    public SwipeMenuRecyclerView(Context context) {
        super(context);
    }

    public SwipeMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        screenWidth = ScreenUtils.getScreenWidth(context);
        xMaxOffset = (int) -TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, context.getResources().getDisplayMetrics());
    }

    public void setMenuMaxOffset(int xMaxOffset) {
        this.xMaxOffset = -xMaxOffset;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent   " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getX();
                yDown = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float xMove = ev.getX();
                float yMove = ev.getY();
                float xDistance = Math.abs(xMove - xDown);
                float yDistance = Math.abs(yMove - yDown);
                //菜单处于关闭状态，
                if (!isMenuOpen && (xMove - xDown < -mTouchSlop && xDistance > yDistance)) {
                    Log.e(TAG, "start move");
                    mIsBeingDragged = true;
                    xLastMove = xMove;
                    menuChild = findChildViewUnder(xDown, yDown);
                    return true;
                }
                //所以这里如果菜单已经打开就拦截拖拽行为，但并不知道点击的是打开菜单的item
                if (isMenuOpen && (xDistance > mTouchSlop || yDistance > mTouchSlop)) {
                    mIsBeingDragged = true;
                    Log.e(TAG, "start move");
                    xLastMove = xMove;
                    return true;
                }

                break;
            //没有拦截move，处理点击打开菜单以外区域的点击事件
            case MotionEvent.ACTION_UP:
                if (isMenuOpen && !isTouchDownOnMenu()) {
                    closeMenuFromOpen();
                    return true;
                }
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onTouchEvent   " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float xMove = ev.getX();
                //float xDistance = Math.abs(xMove - xLastMove);
                if (mIsBeingDragged) {
                    if (!isMenuOpen) {
                        float translationX = 0 + xMove - xLastMove;
                        Log.i(TAG, "translationX  " + translationX);
                        translationMenuX(translationX);
                    } else if (isMenuOpen && isTouchDownOnOpenMenuChild()) {
                        float translationX = xMaxOffset + xMove - xLastMove;
                        Log.i(TAG, "translationX  " + translationX);
                        translationMenuX(translationX);
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    Log.e(TAG, "stop move");
                    if (isMenuOpen && !isTouchDownOnOpenMenuChild()) {
                        closeMenuFromOpen();
                    } else {
                        autoTranslationX();
                    }
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isTouchDownOnOpenMenuChild() {
        boolean inChild = xDown >= 0 && xDown <= screenWidth && yDown >= menuChild.getTop() && yDown <= menuChild.getBottom();
        return inChild;
    }

    private boolean isTouchDownOnMenu() {
        boolean inChild = xDown >= screenWidth + xMaxOffset && xDown <= screenWidth && yDown >= menuChild.getTop() && yDown <= menuChild.getBottom();
        return inChild;
    }

    public void autoTranslationX() {
        float xOffset = menuChild.getTranslationX();
        if (xOffset <= xMaxOffset / 2)
            va = ValueAnimator.ofFloat(xOffset, xMaxOffset);
        if (xOffset < 0 && xOffset > xMaxOffset / 2)
            va = ValueAnimator.ofFloat(xOffset, 0f);
        if (va != null) {
            va.setDuration(100);
            //插值器，表示值变化的规律，默认均匀变化
            va.setInterpolator(new DecelerateInterpolator());
            va.addUpdateListener(animation -> {
                float v = (float) animation.getAnimatedValue();
                Log.d(TAG, "translationX:" + v);
                translationMenuX(v);

            });
            va.start();
        }
    }

    public void closeMenuFromOpen() {
        if (isMenuOpen) {
            float xOffset = menuChild.getTranslationX();
            va = ValueAnimator.ofFloat(xOffset, 0f);
            va.setDuration(200);
            //插值器，表示值变化的规律，默认均匀变化
            va.setInterpolator(new DecelerateInterpolator());
            va.addUpdateListener(animation -> {
                float v = (float) animation.getAnimatedValue();
                Log.d(TAG, "translationX:" + v);
                translationMenuX(v);

            });
            va.start();
        }
    }

    private void translationMenuX(float translationX) {
        if (translationX <= 0 && translationX >= xMaxOffset) {
            menuChild.setTranslationX(translationX);
            if (translationX <= xMaxOffset) {
                isMenuOpen = true;
            }
            if (translationX >= 0) {
                isMenuOpen = false;
            }
        }
    }

    public void setMenuOpen(boolean menuOpen) {
        isMenuOpen = menuOpen;
    }
}
