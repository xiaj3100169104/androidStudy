package example.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.dialog.wheel.ChangeAddressDialog;
import com.style.dialog.wheel.ChangeBirthdayDialog;
import com.style.dialog.wheel.ChangeDialog1List;
import com.style.dialog.wheel.ChangeDialog2List;
import com.style.framework.R;
import cn.style.helper.DataHelper;
import com.style.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by xiajun on 2016/10/8.
 */
public class WheelActivity extends BaseToolBarActivity {

    @Bind(R.id.view_occupation)
    TextView viewOccupation;
    @Bind(R.id.view_age)
    TextView viewAge;
    @Bind(R.id.view_birthday)
    TextView viewBirthday;
    @Bind(R.id.view_hometown)
    TextView viewHometown;
    private List<String> occupations;
    private List<String> fcAges;

    private ChangeDialog2List twoChoiceDialog;
    private ChangeDialog1List singleChoiceDialog;

    private ChangeBirthdayDialog mChangeBirthDialog;
    private ChangeAddressDialog mChangeAddressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_wheel;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        setToolbarTitle("滚轮");
        occupations = DataHelper.getOccupationSelf(this);
        fcAges = DataHelper.getAgeCondition();

    }

    @OnClick(R.id.ll_occupation)
    public void selOccupation() {
        showSingleChoiceDialog("选择职业", occupations, viewOccupation);
    }

    @OnClick(R.id.ll_fc_age)
    public void selFcAge() {
        showTwoChoiceDialog("选择年龄", viewAge, fcAges, fcAges);
    }

    @OnClick(R.id.ll_birthday)
    public void selBirthday() {
        showBirthDayDialog();
    }
    @OnClick(R.id.ll_hometown)
    public void selHometown() {
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
                    viewBirthday.setText(year + "-" + month + "-" + day);
                }
            });
        }
        mChangeBirthDialog.show();
    }

    protected void selectPlaceDialog() {
        String province = null;
        String city = null;
        String str = viewHometown.getText().toString();
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
                viewHometown.setText(province + " " + city);
            }
        });
        mChangeAddressDialog.setAddress(province, city);
        mChangeAddressDialog.show();
    }
}