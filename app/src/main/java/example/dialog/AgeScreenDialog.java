package example.dialog;

import android.content.Context;

import com.style.dialog.BaseDoubleWheelDialog;
import example.helper.DataHelper;

/**
 * Created by xiajun on 2018/6/9.
 */

public class AgeScreenDialog extends BaseDoubleWheelDialog {
    public AgeScreenDialog(Context context) {
        super(context, DataHelper.getAgeCondition(), DataHelper.getAgeCondition());

    }

    @Override
    protected void init() {
        super.init();
        setMyTitle("选择年龄");
    }
}
