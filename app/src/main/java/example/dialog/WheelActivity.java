package example.dialog;

import android.text.TextUtils;
import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.dialog.BaseDoubleWheelDialog;
import com.style.dialog.BaseSingleWheelDialog;
import com.style.dialog.ChangeAddressDialog;
import com.style.dialog.ChangeBirthdayDialog;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWheelBinding;


/**
 * Created by xiajun on 2016/10/8.
 */
public class WheelActivity extends BaseTitleBarActivity {

    ActivityWheelBinding bd;

    private AgeScreenDialog twoChoiceDialog;
    private OccupationDialog singleChoiceDialog;

    private ChangeBirthdayDialog mChangeBirthDialog;
    private ChangeAddressDialog mChangeAddressDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_wheel;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("滚轮");

    }

    public void selOccupation(View v) {
        String str = bd.viewOccupation.getText().toString();
        showOccupationDialog(str);
    }

    public void selFcAge(View v) {
        String str = bd.viewAge.getText().toString();
        showAgeScreenDialog(str);
    }

    public void selBirthday(View v) {
        showBirthDayDialog();
    }

    public void selHometown(View v) {
        selectPlaceDialog();
    }

    protected void showOccupationDialog(String str) {
        if (singleChoiceDialog == null) {
            singleChoiceDialog = new OccupationDialog(this);
            singleChoiceDialog.setOnSureClickListener(new BaseSingleWheelDialog.OnSureClickListener() {
                @Override
                public void onClick(String value) {
                    bd.viewOccupation.setText(value);
                }
            });
        }
        singleChoiceDialog.show();
        singleChoiceDialog.setCurrentItem(str);
    }

    private void showAgeScreenDialog(String str) {
        String province = null;
        String city = null;
        String[] s = null;
        if (!TextUtils.isEmpty(str))
            s = str.split(" - ");
        if (s != null && s.length == 2) {
            province = s[0];
            city = s[1];
        }
        if (twoChoiceDialog == null) {
            twoChoiceDialog = new AgeScreenDialog(this);
        }
        twoChoiceDialog.setAddresskListener(new BaseDoubleWheelDialog.OnAddressCListener() {
            @Override
            public void onClick(String province, String city) {
                logE("city", province + " - " + city);
                bd.viewAge.setText(province + " - " + city);
            }
        });
        twoChoiceDialog.show();
        twoChoiceDialog.initView(province, city);
    }

    private void showBirthDayDialog() {
        if (mChangeBirthDialog == null) {
            mChangeBirthDialog = new ChangeBirthdayDialog(this);
            mChangeBirthDialog.setDate(1990, 01, 01);
            mChangeBirthDialog.setBirthdayListener(new ChangeBirthdayDialog.OnBirthListener() {
                @Override
                public void onClick(String year, String month, String day) {
                    logE("birthday", year + "-" + month + "-" + day);
                    bd.viewBirthday.setText(year + "-" + month + "-" + day);
                }
            });
        }
        mChangeBirthDialog.show();
    }

    protected void selectPlaceDialog() {
        String province = null;
        String city = null;
        String str = bd.viewHometown.getText().toString();
        String[] s = null;
        if (!TextUtils.isEmpty(str))
            s = str.split(" ");
        if (s != null && s.length == 2) {
            province = s[0];
            city = s[1];
        }
        if (mChangeAddressDialog == null) {
            mChangeAddressDialog = new ChangeAddressDialog(this);
        }
        mChangeAddressDialog.setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {
            @Override
            public void onClick(String province, String city) {
                logE("city", province + " " + city);
                bd.viewHometown.setText(province + " " + city);
            }
        });
        mChangeAddressDialog.setAddress(province, city);
        mChangeAddressDialog.show();
    }
}