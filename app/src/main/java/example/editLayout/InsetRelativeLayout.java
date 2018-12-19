package example.editLayout;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.RelativeLayout;

/**
 * Created by xiajun on 2018/7/13.
 * 界面主题为透明状态栏时，如果需要底部的编辑框布局跟随软键盘移动就需要在改布局上加android:fitsSystemWindows="true"
 * 此为避免因这个属性导致的paddingTop为状态栏高度。
 * 注：会导致本身的paddingTop失效
 */

public class InsetRelativeLayout extends RelativeLayout {
    private int[] mInsets = new int[4];

    public InsetRelativeLayout(Context context) {
        super(context);
    }

    public InsetRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InsetRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
// Intentionally do not modify the bottom inset. For some reason,
// if the bottom inset is modified, window resizing stops working.
            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;

            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }

        return super.fitSystemWindows(insets);
    }

    @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.getSystemWindowInsetLeft();
            mInsets[1] = insets.getSystemWindowInsetTop();
            mInsets[2] = insets.getSystemWindowInsetRight();
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
                    insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }

}
