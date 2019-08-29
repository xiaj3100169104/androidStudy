package com.aigestudio.wheelpicker.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.IDebug;
import com.aigestudio.wheelpicker.IWheelPicker;
import com.aigestudio.wheelpicker.R;
import com.aigestudio.wheelpicker.WheelPicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WheelDatePicker extends LinearLayout implements WheelPicker.OnItemSelectedListener, IWheelYearPicker, IWheelMonthPicker, IWheelDayPicker {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());

    private WheelYearPicker mPickerYear;
    private WheelMonthPicker mPickerMonth;
    private WheelDayPicker mPickerDay;

    private OnDateSelectedListener mListener;

    private TextView mTVYear, mTVMonth, mTVDay;

    private int mYear, mMonth, mDay;

    public WheelDatePicker(Context context) {
        this(context, null);
    }

    public WheelDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_wheel_date_picker, this);

        mPickerYear = (WheelYearPicker) findViewById(R.id.wheel_date_picker_year);
        mPickerMonth = (WheelMonthPicker) findViewById(R.id.wheel_date_picker_month);
        mPickerDay = (WheelDayPicker) findViewById(R.id.wheel_date_picker_day);
        mPickerYear.setOnItemSelectedListener(this);
        mPickerMonth.setOnItemSelectedListener(this);
        mPickerDay.setOnItemSelectedListener(this);

        setMaximumWidthTextYear();
        mPickerMonth.setMaximumWidthText("00");
        mPickerDay.setMaximumWidthText("00");

        mTVYear = (TextView) findViewById(R.id.wheel_date_picker_year_tv);
        mTVMonth = (TextView) findViewById(R.id.wheel_date_picker_month_tv);
        mTVDay = (TextView) findViewById(R.id.wheel_date_picker_day_tv);

        mYear = mPickerYear.getCurrentYear();
        mMonth = mPickerMonth.getCurrentMonth();
        mDay = mPickerDay.getCurrentDay();
    }

    private void setMaximumWidthTextYear() {
        List years = mPickerYear.getData();
        String lastYear = String.valueOf(years.get(years.size() - 1));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lastYear.length(); i++)
            sb.append("0");
        mPickerYear.setMaximumWidthText(sb.toString());
    }

    @Override
    public void onItemSelected(WheelPicker picker, String data, int position) {
        if (picker.getId() == R.id.wheel_date_picker_year) {
            mYear = Integer.valueOf(data);
            mPickerDay.setYear(mYear);
        } else if (picker.getId() == R.id.wheel_date_picker_month) {
            mMonth = Integer.valueOf(data);
            mPickerDay.setMonth(mMonth);
        }
        mDay = mPickerDay.getCurrentDay();
        String date = mYear + "-" + mMonth + "-" + mDay;
        if (null != mListener) try {
            mListener.onDateSelected(this, SDF.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mListener = listener;
    }

    public void setVisibleItemCount(int count) {
        mPickerYear.setVisibleItemCount(count);
        mPickerMonth.setVisibleItemCount(count);
        mPickerDay.setVisibleItemCount(count);
    }

    public void setCyclic(boolean isCyclic) {
        mPickerYear.setCyclic(isCyclic);
        mPickerMonth.setCyclic(isCyclic);
        mPickerDay.setCyclic(isCyclic);
    }

    public void setSelectedItemTextColor(int color) {
        mPickerYear.setSelectedItemTextColor(color);
        mPickerMonth.setSelectedItemTextColor(color);
        mPickerDay.setSelectedItemTextColor(color);
    }

    public void setItemTextColor(int color) {
        mPickerYear.setItemTextColor(color);
        mPickerMonth.setItemTextColor(color);
        mPickerDay.setItemTextColor(color);
    }

    public void setItemTextSize(int size) {
        mPickerYear.setItemTextSize(size);
        mPickerMonth.setItemTextSize(size);
        mPickerDay.setItemTextSize(size);
    }

    public void setIndicator(boolean hasIndicator) {
        mPickerYear.setIndicator(hasIndicator);
        mPickerMonth.setIndicator(hasIndicator);
        mPickerDay.setIndicator(hasIndicator);
    }

    public void setIndicatorSize(int size) {
        mPickerYear.setIndicatorSize(size);
        mPickerMonth.setIndicatorSize(size);
        mPickerDay.setIndicatorSize(size);
    }

    public void setIndicatorColor(int color) {
        mPickerYear.setIndicatorColor(color);
        mPickerMonth.setIndicatorColor(color);
        mPickerDay.setIndicatorColor(color);
    }

    public void setAtmospheric(boolean hasAtmospheric) {
        mPickerYear.setAtmospheric(hasAtmospheric);
        mPickerMonth.setAtmospheric(hasAtmospheric);
        mPickerDay.setAtmospheric(hasAtmospheric);
    }

    public void setCurved(boolean isCurved) {
        mPickerYear.setCurved(isCurved);
        mPickerMonth.setCurved(isCurved);
        mPickerDay.setCurved(isCurved);
    }

    @Override
    public void setYearFrame(int start, int end) {
        mPickerYear.setYearFrame(start, end);
    }

    @Override
    public int getYearStart() {
        return mPickerYear.getYearStart();
    }

    @Override
    public void setYearStart(int start) {
        mPickerYear.setYearStart(start);
    }

    @Override
    public int getYearEnd() {
        return mPickerYear.getYearEnd();
    }

    @Override
    public void setYearEnd(int end) {
        mPickerYear.setYearEnd(end);
    }

    @Override
    public int getSelectedYear() {
        return mPickerYear.getSelectedYear();
    }

    @Override
    public void setSelectedYear(int year) {
        mYear = year;
        mPickerYear.setSelectedYear(year);
        mPickerDay.setYear(year);
    }

    @Override
    public int getCurrentYear() {
        return mPickerYear.getCurrentYear();
    }

    @Override
    public int getSelectedMonth() {
        return mPickerMonth.getSelectedMonth();
    }

    @Override
    public void setSelectedMonth(int month) {
        mMonth = month;
        mPickerMonth.setSelectedMonth(month);
        mPickerDay.setMonth(month);
    }

    @Override
    public int getCurrentMonth() {
        return mPickerMonth.getCurrentMonth();
    }

    @Override
    public int getSelectedDay() {
        return mPickerDay.getSelectedDay();
    }

    @Override
    public void setSelectedDay(int day) {
        mDay = day;
        mPickerDay.setSelectedDay(day);
    }

    @Override
    public int getCurrentDay() {
        return mPickerDay.getCurrentDay();
    }

    @Override
    public void setYearAndMonth(int year, int month) {
        mYear = year;
        mMonth = month;
        mPickerYear.setSelectedYear(year);
        mPickerMonth.setSelectedMonth(month);
        mPickerDay.setYearAndMonth(year, month);
    }

    @Override
    public int getYear() {
        return getSelectedYear();
    }

    @Override
    public void setYear(int year) {
        mYear = year;
        mPickerYear.setSelectedYear(year);
        mPickerDay.setYear(year);
    }

    @Override
    public int getMonth() {
        return getSelectedMonth();
    }

    @Override
    public void setMonth(int month) {
        mMonth = month;
        mPickerMonth.setSelectedMonth(month);
        mPickerDay.setMonth(month);
    }

    public interface OnDateSelectedListener {
        void onDateSelected(WheelDatePicker picker, Date date);
    }
}