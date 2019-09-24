package example.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.style.base.activity.BaseTitleBarActivity;
import com.style.framework.R;
import com.xiajun.widget.libsafekeyboard.KeyboardPopupWindow;

public class SafeKeyboardActivity extends BaseTitleBarActivity {
    private static final String TAG = "MainActivity";
    private EditText numberEt;
    private KeyboardPopupWindow keyboardPopupWindow;
    private boolean isUiCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_keyboard_activity);
        setFullScreenStableDarkMode(true);

        initView();
    }

    private void initView() {
        numberEt = findViewById(R.id.numberEt);
        keyboardPopupWindow = new KeyboardPopupWindow(SafeKeyboardActivity.this, getWindow().getDecorView(), numberEt, true);
//        numberEt.setInputType(InputType.TYPE_NULL);//该设置会导致光标不可见
        numberEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyboardPopupWindow != null) {
                    keyboardPopupWindow.show();
                }
            }
        });
        numberEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "数字输入框是否有焦点——>" + hasFocus);
                if (keyboardPopupWindow != null && isUiCreated) {//很重要，Unable to add window -- token null is not valid; is your activity running?
                    keyboardPopupWindow.refreshKeyboardOutSideTouchable(!hasFocus);// 需要等待页面创建完成后焦点变化才去显示自定义键盘
                }

                if (hasFocus) {//隐藏系统软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(numberEt.getWindowToken(), 0);
                }

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardPopupWindow != null && keyboardPopupWindow.isShowing()) {
                keyboardPopupWindow.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isUiCreated = true;
    }


    @Override
    protected void onDestroy() {
        if (keyboardPopupWindow != null) {
            keyboardPopupWindow.releaseResources();
        }
        super.onDestroy();
    }
}
