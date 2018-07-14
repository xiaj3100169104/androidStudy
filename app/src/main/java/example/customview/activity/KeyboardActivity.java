package example.customview.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.style.framework.R;

import example.customview.popupwindow.CustomKeyboardWindow;


public class KeyboardActivity extends AppCompatActivity {

    private CustomKeyboardWindow mLoginPop;
    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_keyboard);
        rootView = (ViewGroup) findViewById(R.id.root);
    }

    public void open(View cbLogin) {
        if (mLoginPop == null) {
            mLoginPop = new CustomKeyboardWindow.Builder(this).create();
            //某些版本手机上不设置这个属性点击窗口外和返回键没反应
            mLoginPop.setBackgroundDrawable(new ColorDrawable());
            mLoginPop.bt_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = mLoginPop.getNumber();
                    mLoginPop.dismiss();

                }
            });
        }
        mLoginPop.setNumber("6546");
        mLoginPop.showAsDropDown(cbLogin, 10, 10);
    }

}
