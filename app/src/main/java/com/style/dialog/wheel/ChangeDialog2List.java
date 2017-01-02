package com.style.dialog.wheel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.style.dialog.wheel.adapters.AbstractWheelTextAdapter;
import com.style.dialog.wheel.views.OnWheelChangedListener;
import com.style.dialog.wheel.views.OnWheelScrollListener;
import com.style.dialog.wheel.views.WheelView;
import com.style.framework.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 更改封面对话框
 *
 * @author ywl
 */
public class ChangeDialog2List extends Dialog implements View.OnClickListener {
    private static final String UNLIMITED = "不限";
    private WheelView wvProvince;
    private WheelView wvCitys;
    private View lyChangeAddress;
    private View lyChangeAddressChild;
    private TextView btnSure;
    private TextView btnCancel;

    private Context context;

    private List<String> arrProvinces = new ArrayList<>();
    private List<String> arrCitys = new ArrayList<>();
    private AddressTextAdapter provinceAdapter;
    private AddressTextAdapter cityAdapter;

    private String strProvince;
    private String strCity;
    private OnAddressCListener onAddressCListener;

    private int maxsize = 24;
    private int minsize = 14;
    private TextView tv_title;

    public ChangeDialog2List(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    public ChangeDialog2List(Context context, List<String> list1, List<String> list2) {
        super(context, R.style.ShareDialog);
        this.context = context;
        setList(list1, list2);
    }

    public void setMyTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_2_list);

        wvProvince = (WheelView) findViewById(R.id.wv_address_province);
        wvCitys = (WheelView) findViewById(R.id.wv_address_city);
        lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
        lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);
        tv_title = (TextView) findViewById(R.id.tv_share_title);
        lyChangeAddress.setOnClickListener(this);
        lyChangeAddressChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        strProvince = arrProvinces.get(0);
        strCity = arrCitys.get(0);
    }

    public void initView(String value1, String value2) {
        if (!TextUtils.isEmpty(value1))
            strProvince = value1;
        if (!TextUtils.isEmpty(value2))
            strCity = value2;
        provinceAdapter = new AddressTextAdapter(context, arrProvinces, getProvinceItem(strProvince), maxsize, minsize);
        wvProvince.setVisibleItems(5);
        wvProvince.setViewAdapter(provinceAdapter);
        wvProvince.setCurrentItem(getProvinceItem(strProvince));

        cityAdapter = new AddressTextAdapter(context, arrCitys, getCityItem(strCity), maxsize, minsize);
        wvCitys.setVisibleItems(5);
        wvCitys.setViewAdapter(cityAdapter);
        wvCitys.setCurrentItem(getCityItem(strCity));
        wvProvince.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                strProvince = currentText;
                setTextviewSize(strProvince, provinceAdapter);
                int position1 = getProvinceItem(strProvince);
                int position2 = getCityItem(strCity);
                if (position1 > position2) {
                    wvCitys.setCurrentItem(position1);
                }
            }
        });
        wvProvince.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
            }
        });

        wvCitys.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                strCity = currentText;
                setTextviewSize(strCity, cityAdapter);
                int position1 = getProvinceItem(strProvince);
                int position2 = getCityItem(strCity);
                if (position2 < position1) {
                    wvProvince.setCurrentItem(position2);
                }
            }
        });
        wvCitys.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, cityAdapter);
            }
        });
    }

    public void setList(List<String> list1, List<String> list2) {
        this.arrProvinces = list1;
        this.arrCitys = list2;
    }

    private class AddressTextAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected AddressTextAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(24);
            } else {
                textvew.setTextSize(14);
            }
        }
    }

    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSure) {
            if (onAddressCListener != null) {
                onAddressCListener.onClick(strProvince, strCity);
            }
        } else if (v == btnCancel) {

        } else if (v == lyChangeAddressChild) {
            return;
        } else {
            dismiss();
        }
        dismiss();
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnAddressCListener {
        public void onClick(String province, String city);
    }

    /**
     * 返回省会索引，没有就返回默认“不限
     *
     * @param province
     * @return
     */
    public int getProvinceItem(String province) {
        int size = arrProvinces.size();
        int provinceIndex = 0;
        boolean noprovince = true;
        for (int i = 0; i < size; i++) {
            if (province.equals(arrProvinces.get(i))) {
                noprovince = false;
                return provinceIndex;
            } else {
                provinceIndex++;
            }
        }
        if (noprovince) {
            strProvince = arrProvinces.get(0);
            return 0;
        }
        return provinceIndex;
    }

    /**
     * 得到城市索引，没有返回默认“不限”
     *
     * @param city
     * @return
     */
    public int getCityItem(String city) {
        int size = arrCitys.size();
        int cityIndex = 0;
        boolean nocity = true;
        for (int i = 0; i < size; i++) {
            System.out.println(arrCitys.get(i));
            if (city.equals(arrCitys.get(i))) {
                nocity = false;
                return cityIndex;
            } else {
                cityIndex++;
            }
        }
        if (nocity) {
            strCity = arrCitys.get(0);
            return 0;
        }
        return cityIndex;
    }

}