package com.style.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.aigestudio.wheelpicker.WheelPicker;
import com.style.base.BaseBottomDialog;
import com.style.framework.R;

import java.util.ArrayList;
import java.util.List;

import static com.dmcbig.mediapicker.utils.ScreenUtils.sp2px;
import static com.style.utils.DeviceInfoUtil.dp2px;

/**
 * 单条列表选择对话框
 *
 * @author ywl
 */
public abstract class BaseSingleWheelDialog extends BaseBottomDialog {
    private WheelPicker wheelCenter;
    private List<String> dataList = new ArrayList<>();
    private OnSureClickListener listener;
    private String currentItem;

    public BaseSingleWheelDialog(Context context, List<String> list) {
        super(context);
        dataList = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_single_wheel_dialog);
    }

    @Override
    protected void init() {
        wheelCenter = findViewById(R.id.main_wheel_center);
        wheelCenter.setItemTextColor(0xffcccccc);
        wheelCenter.setSelectedItemTextColor(0xff666666);
        wheelCenter.setIndicator(true);
        wheelCenter.setIndicatorSize(dp2px(getContext(), 1));
        wheelCenter.setIndicatorColor(0xFFe3e3e3);
        wheelCenter.setItemTextSize(sp2px(getContext(), 21));
        wheelCenter.setCurved(false);
        wheelCenter.setCyclic(false);
        wheelCenter.setVisibleItemCount(5);
        wheelCenter.setAtmospheric(true);

        wheelCenter.setData(dataList);
        wheelCenter.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int offset) {

            }

            @Override
            public void onWheelSelected(int position) {
                currentItem = (String) wheelCenter.getData().get(position);
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });
    }

    public void setCurrentItem(String str) {
        int currentIndex = 0;
        if (!TextUtils.isEmpty(str) && dataList.indexOf(str) != -1)
            currentIndex = dataList.indexOf(str);
        //该方法不会触发onWheelSelected，所以需要手动设置当前选择数据
        wheelCenter.setSelectedItemPosition(currentIndex);
        currentItem = dataList.get(currentIndex);
    }

    public void setOnSureClickListener(OnSureClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClickSure() {
        if (listener != null) {
            listener.onClick(currentItem);
        }
        super.onClickSure();
    }

    public interface OnSureClickListener {
        void onClick(String value);
    }

    public void setList(List<String> list) {
        this.dataList = list;
    }
}