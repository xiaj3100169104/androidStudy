package com.aigestudio.wheelpicker.widgets;

/**
 * 分钟选择器方法接口
 * <p>
 * Interface of WheelMinutePicker
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public interface IWheelMinutePicker {
    /**
     * 获取分钟选择器初始化时选择的分钟
     *
     * @return 选择的分钟
     */
    String getSelectedMinute();

    /**
     * 设置分钟选择器初始化时选择的分钟
     *
     * @param minute 选择的分钟
     */
    void setSelectedMinute(String minute);

    /**
     * 获取当前选择的分钟
     *
     * @return 当前选择的分钟
     */
    String getCurrentMinute();
}
