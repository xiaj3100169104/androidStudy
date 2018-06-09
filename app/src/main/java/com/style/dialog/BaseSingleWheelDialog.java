package com.style.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.framework.R;
import com.style.view.wheel.adapters.AbstractWheelTextAdapter;
import com.style.view.wheel.views.OnWheelChangedListener;
import com.style.view.wheel.views.OnWheelScrollListener;
import com.style.view.wheel.views.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * 单条列表选择对话框
 *
 * @author ywl
 */
public abstract class BaseSingleWheelDialog extends BaseCenterDialog {
    private WheelPicker wheelCenter;
    private List<String> dataList = new ArrayList<>();
    private OnAddressCListener onAddressCListener;
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
        wheelCenter.setIndicatorSize(ScreenUtils.dp2px(getContext(), 1));
        wheelCenter.setIndicatorColor(0xff91c532);
        wheelCenter.setItemTextSize(ScreenUtils.sp2px(getContext(), 21));
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
        if (!TextUtils.isEmpty(str) || dataList.indexOf(str) != -1)
            currentIndex = dataList.indexOf(str);
        wheelCenter.setSelectedItemPosition(currentIndex);
    }

    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    @Override
    public void onClickSure() {
        if (onAddressCListener != null) {
            onAddressCListener.onClick(currentItem);
        }
        super.onClickSure();
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnAddressCListener {
        void onClick(String province);
    }

    public void setList(List<String> list) {
        this.dataList = list;
    }
}