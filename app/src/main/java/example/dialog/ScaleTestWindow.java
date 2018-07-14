package example.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.style.framework.R;

public class ScaleTestWindow extends PopupWindow {
    private Context mContext;

    public ScaleTestWindow(Context context, View contentView) {
        this(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mContext = context;
        init(contentView);
    }

    public ScaleTestWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    //此类用来装载一些变量逻辑在super操作之前，因为构造super语句之前不能有其他语句
    public static class Builder {
        private Context context;
        private ScaleTestWindow popWindow;

        public Builder(Context context) {
            this.context = context;
        }

        public ScaleTestWindow create() {
            View contentView = LayoutInflater.from(context).inflate(R.layout.window_scale_test, null);
            popWindow = new ScaleTestWindow(context, contentView);
            //某些版本手机上不设置这个属性点击窗口外和返回键没反应
            popWindow.setBackgroundDrawable(new ColorDrawable());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //popupElevation这个属性在Api21及以上的默认的值是16dp
                popWindow.setElevation(30);
            }
            popWindow.setAnimationStyle(R.style.Anim_grow_from_view);
            return popWindow;
        }

    }

    private void init(View contentView) {

    }

    /*
   * 有输入框时隐藏软键盘
   */
    public void hiddenSoftInput(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
