package com.style.view.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.style.framework.R;

import java.util.ArrayList;

public class CustomTagsLayout extends ViewGroup {
    //标签间垂直间距
    private int mTagMarginVertical;
    //标签间水平间距
    private int mTagMarginHorizontal;
    private OnClickChildListener mOnClickChildListener;

    public CustomTagsLayout(Context context) {
        super(context);
    }

    public CustomTagsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTagsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTagsLayout, defStyleAttr, 0);
        mTagMarginHorizontal = a.getDimensionPixelSize(R.styleable.CustomTagsLayout_tag_margin_horizontal, 15);
        mTagMarginVertical = a.getDimensionPixelSize(R.styleable.CustomTagsLayout_tag_margin_vertical, 15);
        a.recycle();
    }

    /**
     * 要求所有的孩子测量自己的大小，然后根据这些孩子的大小完成自己的尺寸测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        final int count = getChildCount();
        if (count > 0) {
            int mLineHeight = getChildAt(0).getMeasuredHeight();
            int mLineIndex = -1;
            int left, right = 0;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                //注意此处不能使用getWidth和getHeight，这两个方法必须在onLayout执行完，才能正确获取宽高
                int childMeasureWidth = child.getMeasuredWidth();
                if (i == 0) {
                    mLineIndex = 0;
                    left = getPaddingLeft();
                } else {
                    //当前行容不下当前子view时换行,注意此处容器不能使用getWidth,onMeasure执行完才能正确获取宽高
                    if (right + mTagMarginHorizontal + childMeasureWidth + getPaddingRight() > mWidth) {
                        mLineIndex++;
                        left = getPaddingLeft();
                    } else {
                        left = right + mTagMarginHorizontal;
                    }
                }
                right = left + childMeasureWidth;
            }
            mHeight = mLineHeight * (mLineIndex + 1) + mTagMarginVertical * mLineIndex + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 设置子控件摆放位置.可能会用到自定义布局参数中的其他属性
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        if (count > 0) {
            int mLineHeight = getChildAt(0).getMeasuredHeight();
            int mLineIndex = -1;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                //注意此处不能使用getWidth和getHeight，这两个方法必须在onLayout执行完，才能正确获取宽高
                int childMeasureWidth = child.getMeasuredWidth();
                int childMeasureHeight = child.getMeasuredHeight();
                if (i == 0) {
                    mLineIndex = 0;
                    left = getPaddingLeft();
                    top = getPaddingTop();
                } else {
                    //当前行容不下当前子view时换行
                    if (right + mTagMarginHorizontal + childMeasureWidth + getPaddingRight() > getWidth()) {
                        mLineIndex++;
                        left = getPaddingLeft();
                    } else {
                        left = right + mTagMarginHorizontal;
                    }
                    top = getPaddingTop() + (mLineHeight + mTagMarginVertical) * mLineIndex;
                }
                right = left + childMeasureWidth;
                bottom = top + childMeasureHeight;
                child.layout(left, top, right, bottom);
                child.setOnClickListener(v -> {
                    if (mOnClickChildListener != null) {
                        mOnClickChildListener.onClickChild(v);
                    }
                });
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new CustomLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 确认子View的LayoutParams是否合法, 如果不合法，调用generateLayoutParams(p).
     *
     * @param p
     * @return
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CustomLayoutParams;
    }

    /**
     * 注意：布局参数类只是用来定义与子view尺寸、位置相关的属性，不要掺杂其他属性。
     */
    private class CustomLayoutParams extends LayoutParams {
        public CustomLayoutParams(LayoutParams p) {
            super(p);
        }

        public CustomLayoutParams(int matchParent, int matchParent1) {
            super(matchParent, matchParent1);
        }

        public CustomLayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }

    public void setTags(ArrayList<TextView> tags) {
        this.removeAllViews();
        for (TextView tagView : tags) {
            this.addView(tagView);
        }
    }

    public void setOnClickChildListener(OnClickChildListener l) {
        this.mOnClickChildListener = l;
    }

    public interface OnClickChildListener {
        void onClickChild(View tag);
    }
}
