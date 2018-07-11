package example.customview.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.style.framework.R;
import com.style.view.CallKeyboard;

/**
 * Created by xiajun on 2016/6/28.
 */
public class CustomKeyboardWindow extends PopupWindow {
    private Context mContext;
    private ImageView btDelete;
    private EditText et_content;
    private CallKeyboard callKeyboard;
    public Button bt_positive;

    public CustomKeyboardWindow(Context context, View contentView) {
        this(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mContext = context;
        init(contentView);
    }

    public CustomKeyboardWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    //此类用来装载一些变量逻辑在super操作之前，因为构造super语句之前不能有其他语句
    public static class Builder {
        private Context context;
        private CustomKeyboardWindow dialog;

        /**
         * 构造函数
         *
         * @param context 上下文对象
         */
        public Builder(Context context) {
            this.context = context;
        }

        public CustomKeyboardWindow create() {
            View contentView = LayoutInflater.from(context).inflate(R.layout.dlg_keyboard, null);
            dialog = new CustomKeyboardWindow(context, contentView);
            return dialog;
        }

    }

    private void init(View contentView) {
        //setAnimationStyle(R.style.Animations_ExtendsFromTop);
        et_content = (EditText) contentView.findViewById(R.id.et_call_number);
        btDelete = (ImageView) contentView.findViewById(R.id.bt_delete);
        callKeyboard = (CallKeyboard) contentView.findViewById(R.id.call_keyboard);
        bt_positive = (Button) contentView.findViewById(R.id.bt_confirm);
      /*  et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    et_content.setSelection(s.length());
            }
        });*/
        callKeyboard.setOnNumberClickListener(new CallKeyboard.OnNumberClickListener() {
            @Override
            public void onClickNumber(String s) {
                int index = et_content.getSelectionStart();
                Editable edit = et_content.getEditableText();//获取EditText的文字
                edit.insert(index, s);//光标所在位置插入文字
            }
        });
        et_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenSoftInput((Activity) mContext, et_content);
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable s = et_content.getText();
                if (s.length() > 0) {
                    et_content.setText(s.subSequence(0, s.length() - 1));
                    et_content.setSelection(s.length() - 1);
                }
            }
        });
        btDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_content.setText("");
                return true;
            }
        });
    }

    public void setNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            et_content.setText(number);
            et_content.setSelection(number.length());
        }
    }

    public String getNumber() {
        return et_content.getText().toString();
    }

    /*
   * 有输入框时隐藏软键盘
   */
    public void hiddenSoftInput(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
