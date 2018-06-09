package example.dialog;

import android.view.View;
import android.widget.TextView;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.dialog.BaseSingleWheelDialog;
import com.style.dialog.ChangeAddressDialog;
import com.style.dialog.ChangeBirthdayDialog;
import com.style.dialog.BaseDoubleWheelDialog;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWheelBinding;
import com.style.utils.StringUtil;

import java.util.List;

import example.helper.DataHelper;


/**
 * Created by xiajun on 2016/10/8.
 */
public class WheelActivity extends BaseActivity {

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
    protected BaseActivityPresenter getPresenter() {
        return null;
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
            singleChoiceDialog.setAddresskListener(new BaseSingleWheelDialog.OnAddressCListener() {
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
        String[] s = StringUtil.getStringArray(str, " - ");
        if (s != null) {
            if (s.length == 2) {
                province = s[0];
                city = s[1];
            }
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
        String[] s = StringUtil.getStringArray(str, " ");
        if (s != null) {
            if (s.length == 2) {
                province = s[0];
                city = s[1];
            }
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