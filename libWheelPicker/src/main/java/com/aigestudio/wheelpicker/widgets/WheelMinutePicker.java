package com.aigestudio.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 分钟选择器
 * <p>
 * Picker for Minutes
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelMinutePicker extends WheelPicker implements IWheelMinutePicker {
    private String mSelectedMinute;

    public WheelMinutePicker(Context context) {
        this(context, null);
    }

    public WheelMinutePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<String> data = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i <= 9) {
                data.add("0"+i);
                continue;
            }
            data.add(String.valueOf(i));
        }
        super.setData(data);

        mSelectedMinute = "00";
        updateSelected();
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    @Override
    public String getSelectedMinute() {
        return mSelectedMinute;
    }

    @Override
    public void setSelectedMinute(String minute) {
        mSelectedMinute = minute;
        updateSelected();
    }

    @Override
    public String getCurrentMinute() {
        return getData().get(getCurrentItemPosition());
    }

    private void updateSelected() {
        setSelectedItem(mSelectedMinute);
    }
}