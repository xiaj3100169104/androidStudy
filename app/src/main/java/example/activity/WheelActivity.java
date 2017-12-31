package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.dialog.wheel.ChangeAddressDialog;
import com.style.dialog.wheel.ChangeBirthdayDialog;
import com.style.dialog.wheel.ChangeDialog1List;
import com.style.dialog.wheel.ChangeDialog2List;
import com.style.framework.R;
import example.helper.DataHelper;

import com.style.framework.databinding.ActivityWheelBinding;
import com.style.utils.StringUtil;

import java.util.List;


/**
 * Created by xiajun on 2016/10/8.
 */
public class WheelActivity extends BaseToolBarActivity {

    ActivityWheelBinding bd;
    private List<String> occupations;
    private List<String> fcAges;

    private ChangeDialog2List twoChoiceDialog;
    private ChangeDialog1List singleChoiceDialog;

    private ChangeBirthdayDialog mChangeBirthDialog;
    private ChangeAddressDialog mChangeAddressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_wheel);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("滚轮");
        occupations = DataHelper.getOccupationSelf(this);
        fcAges = DataHelper.getAgeCondition();

    }

    public void selOccupation(View v) {
        showSingleChoiceDialog("选择职业", occupations, bd.viewOccupation);
    }

    public void selFcAge(View v) {
        showTwoChoiceDialog("选择年龄", bd.viewAge, fcAges, fcAges);
    }

    public void selBirthday(View v) {
        showBirthDayDialog();
    }
    public void selHometown(View v) {
        selectPlaceDialog();
    }
    protected void showSingleChoiceDialog(String title, final List<String> items, final TextView textView) {
        String value = textView.getText().toString();
        if (singleChoiceDialog == null) {
            singleChoiceDialog = new ChangeDialog1List(this, items);
        }
        singleChoiceDialog.setAddresskListener(new ChangeDialog1List.OnAddressCListener() {
            @Override
            public void onClick(String value) {
                textView.setText(value);
            }
        });
        singleChoiceDialog.setList(items);
        singleChoiceDialog.show();
        singleChoiceDialog.setMyTitle(title);
        singleChoiceDialog.initView(value);
    }

    private void showTwoChoiceDialog(String title, final TextView textView, List<String> list1, List<String> list2) {
        String province = null;
        String city = null;
        String str = textView.getText().toString();
        String[] s = StringUtil.getStringArray(str, " - ");
        if (s != null) {
            if (s.length == 2) {
                province = s[0];
                city = s[1];
            }
        }
        if (twoChoiceDialog == null) {
            twoChoiceDialog = new ChangeDialog2List(this, list1, list2);
        }
        twoChoiceDialog.setAddresskListener(new ChangeDialog2List.OnAddressCListener() {
            @Override
            public void onClick(String province, String city) {
                logE("city", province + " - " + city);
                textView.setText(province + " - " + city);
            }
        });
        twoChoiceDialog.setList(list1, list2);
        twoChoiceDialog.show();
        twoChoiceDialog.setMyTitle(title);
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