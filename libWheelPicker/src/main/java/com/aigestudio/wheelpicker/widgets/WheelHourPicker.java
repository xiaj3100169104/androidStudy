package com.aigestudio.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 小时选择器
 * <p>
 * Picker for Hours
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelHourPicker extends WheelPicker implements IWheelHourPicker {
    private String mSelectedHour;

    public WheelHourPicker(Context context) {
        this(context, null);
    }

    public WheelHourPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<String> data = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            if (i <= 9) {
                data.add("0" + i);
                continue;
            }
            data.add(String.valueOf(i));
        }
        super.setData(data);

        mSelectedHour = "00";
        updateSelectedHour();
    }

    private void updateSelectedHour() {
        setSelectedItem(mSelectedHour);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    @Override
    public String getSelectedHour() {
        return mSelectedHour;
    }

    @Override
    public void setSelectedHour(String hour) {
        mSelectedHour = hour;
        updateSelectedHour();
    }

    @Override
    public String getCurrentHour() {
        return getData().get(getCurrentItemPosition());
    }
}