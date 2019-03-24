package com.aigestudio.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.R;
import com.aigestudio.wheelpicker.WheelPicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WheelTimePicker extends LinearLayout implements WheelPicker.OnItemSelectedListener, IWheelHourPicker, IWheelMinutePicker {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    private WheelHourPicker mPickerHour;
    private WheelMinutePicker mPickerMinute;
    private OnDateSelectedListener mListener;
    private String mHour, mMinute;

    public WheelTimePicker(Context context) {
        this(context, null);
    }

    public WheelTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_wheel_time_picker, this);
        mPickerHour = findViewById(R.id.wheel_date_picker_hour);
        mPickerMinute = findViewById(R.id.wheel_date_picker_minute);
        mPickerHour.setOnItemSelectedListener(this);
        mPickerMinute.setOnItemSelectedListener(this);

        mPickerHour.setMaximumWidthText("00");
        mPickerMinute.setMaximumWidthText("00");

        mHour = mPickerHour.getCurrentHour();
        mMinute = mPickerMinute.getCurrentMinute();
    }

    @Override
    public void onItemSelected(WheelPicker picker, String data, int position) {
        if (picker.getId() == R.id.wheel_date_picker_hour) {
            mHour = data;
        } else if (picker.getId() == R.id.wheel_date_picker_minute) {
            mMinute = data;
        }
        String date = mHour + ":" + mMinute;
        if (null != mListener)
            mListener.onDateSelected(this, date);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mListener = listener;
    }

    public void setVisibleItemCount(int count) {
        mPickerHour.setVisibleItemCount(count);
        mPickerMinute.setVisibleItemCount(count);
    }

    public void setCyclic(boolean isCyclic) {
        mPickerHour.setCyclic(isCyclic);
        mPickerMinute.setCyclic(isCyclic);
    }

    public void setSelectedItemTextColor(int color) {
        mPickerHour.setSelectedItemTextColor(color);
        mPickerMinute.setSelectedItemTextColor(color);
    }

    public void setItemTextColor(int color) {
        mPickerHour.setItemTextColor(color);
        mPickerMinute.setItemTextColor(color);
    }

    public void setItemTextSize(int size) {
        mPickerHour.setItemTextSize(size);
        mPickerMinute.setItemTextSize(size);
    }

    public void setIndicator(boolean hasIndicator) {
        mPickerHour.setIndicator(hasIndicator);
        mPickerMinute.setIndicator(hasIndicator);
    }

    public void setIndicatorSize(int size) {
        mPickerHour.setIndicatorSize(size);
        mPickerMinute.setIndicatorSize(size);
    }

    public void setIndicatorColor(int color) {
        mPickerHour.setIndicatorColor(color);
        mPickerMinute.setIndicatorColor(color);
    }

    public void setAtmospheric(boolean hasAtmospheric) {
        mPickerHour.setAtmospheric(hasAtmospheric);
        mPickerMinute.setAtmospheric(hasAtmospheric);
    }

    public void setCurved(boolean isCurved) {
        mPickerHour.setCurved(isCurved);
        mPickerMinute.setCurved(isCurved);
    }

    @Override
    public String getSelectedHour() {
        return mPickerHour.getSelectedHour();
    }

    @Override
    public void setSelectedHour(String hour) {
        mHour = hour;
        mPickerHour.setSelectedHour(hour);
    }

    @Override
    public String getCurrentHour() {
        return mPickerHour.getCurrentHour();
    }

    @Override
    public String getSelectedMinute() {
        return mPickerMinute.getSelectedMinute();
    }

    @Override
    public void setSelectedMinute(String minute) {
        mMinute = minute;
        mPickerMinute.setSelectedMinute(minute);
    }

    @Override
    public String getCurrentMinute() {
        return mPickerMinute.getCurrentMinute();
    }

    public interface OnDateSelectedListener {
        void onDateSelected(WheelTimePicker picker, String date);
    }
}