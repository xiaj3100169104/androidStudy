package com.style.view.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.style.framework.R;

public class CustomTagsLayout extends ViewGroup {
    private int mTagMarginVertical;
    private int mTagMarginHorizontal;

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
        int mLineWidth = 0;          //每行子view宽度累加和
        int mLineHeight = 0;
        int mLineCount = 0;
        if (count > 0) {
            mLineHeight = getChildAt(0).getMeasuredHeight() + mTagMarginVertical * 2;
            mLineCount++;
        }
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            mLineWidth += child.getMeasuredWidth() + mTagMarginHorizontal * 2;
            //排满后换行
            if (mLineWidth + getPaddingLeft() + getPaddingRight() > mWidth) {
                mLineCount++;
                mLineWidth = child.getMeasuredWidth() + mTagMarginHorizontal * 2;
            }
        }
        setMeasuredDimension(mWidth, mLineHeight * mLineCount + getPaddingTop() + getPaddingBottom());
    }

    /**
     * 设置子控件摆放位置.可能会用到自定义布局参数中的其他属性
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        int childMeasureWidth = 0;
        int mLineWidth = 0;
        int mLineHeight = 0;
        if (count > 0) {
            mLineHeight = getChildAt(0).getMeasuredHeight() + mTagMarginVertical * 2;
        }
        int mLineIndex = 0;
        left = getPaddingLeft() + mTagMarginHorizontal;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //注意此处不能使用getWidth和getHeight，这两个方法必须在onLayout执行完，才能正确获取宽高
            childMeasureWidth = child.getMeasuredWidth();
            //排满后换行
            if (left + childMeasureWidth + mTagMarginHorizontal + getPaddingRight() > getWidth()) {
                mLineIndex++;
                left = getPaddingLeft() + mTagMarginHorizontal;
            } else {
                left += childMeasureWidth + mTagMarginHorizontal * 2;
            }
            if (i == 0) {
                left = getPaddingLeft() + mTagMarginHorizontal;
            }
            right = left + childMeasureWidth;
            top = getPaddingTop() + mLineHeight * mLineIndex + mTagMarginVertical;
            bottom = top + child.getMeasuredHeight();
            child.layout(left, top, right, bottom);
        }
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
        return new CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CustomLayoutParams;
    }

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
}
