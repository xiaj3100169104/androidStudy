package example.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.style.framework.R;

public class ScaleTestWindow extends PopupWindow {
    private Context context;

    public ScaleTestWindow(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Context getContext() {
        return context;
    }

    private void init() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.window_scale_test, null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        //6.0以下系统不设置这个属性点击窗口外和返回键没反应，如果有背景但透明度值太低会导致阴影效果无效。
        //如果根布局背景是不透明圆角矩形，建议直接在代码里面设置该圆角背景，否则会导致背景四个角不能被覆盖。
        setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.rounded_corners));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(20);
        }
        setAnimationStyle(R.style.Anim_grow_from_view);
    }
}
