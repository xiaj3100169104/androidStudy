package com.style.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式处理工具类
 * Created by xiajun on 2017/1/9.
 */
public class MyDateUtil {
    public final static String pattern = "yyyy-MM-dd HH:mm:ss E";

    public static String getTimeConversationString(long millions) {
        String pattern;
        if (isBelongToThisYear(millions)) {
            if (isTheDayBefore(millions, 0)) {
                pattern = "HH:mm";
            } else if (isTheDayBefore(millions, -1)) {
                pattern = "昨天 HH:mm";
            } else if (isTheDayBefore(millions, -2)) {
                pattern = "前天 HH:mm";
            }else if (isTheDayBefore(millions, -3)) {
                pattern = "E";
            } else if (isTheDayBefore(millions, -4)) {
                pattern = "E";
            }  else {
                pattern = "MM-dd";
            }
        } else
            pattern = "yyyy-MM-dd";
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(millions));
    }

    /**
     * @param
     * @return 返回描述性时间，"今天 HH:mm"，"昨天 HH:mm"，"前天 HH:mm",yyyy-MM-dd HH:mm
     */
    public static String getTimeDynamicString(String strTime, String formatType) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        Date date = format.parse(strTime);
        long millions = date.getTime();
        String pattern;
        if (isBelongToThisYear(millions)) {
            if (isTheDayBefore(millions, 0)) {
                pattern = "今天 HH:mm";
            } else if (isTheDayBefore(millions, -1)) {
                pattern = "昨天 HH:mm";
            } else if (isTheDayBefore(millions, -2)) {
                pattern = "前天 HH:mm";
            } else {
                pattern = "MM月dd日  HH:mm";
            }
        } else {
            pattern = "yyyy-MM-dd HH:mm";
        }
        return new SimpleDateFormat(pattern, Locale.CHINA).format(date);
    }

    /**
     * @param
     * @return 返回描述性时间
     */
    public static String getTimeFromNow(String strTime, String formatType) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        Date date  = format.parse(strTime);
        long speMillis = date.getTime();
        long currentMillis = System.currentTimeMillis();
        long value = currentMillis - speMillis;
        String describeTime = null;
        if (value > DateUtils.DAY_IN_MILLIS) {
            int num = (int) (value / DateUtils.DAY_IN_MILLIS);
            describeTime = num + "天前";
        } else if (value > DateUtils.HOUR_IN_MILLIS) {
            int num = (int) (value / DateUtils.HOUR_IN_MILLIS);
            describeTime = num + "小时前";
        } else if (value > DateUtils.MINUTE_IN_MILLIS) {
            int num = (int) (value / DateUtils.MINUTE_IN_MILLIS);
            describeTime = num + "分钟前";
        } else if (value > DateUtils.SECOND_IN_MILLIS) {
            int num = (int) (value / DateUtils.SECOND_IN_MILLIS);
            describeTime = num + "秒前";
        }
        return describeTime;
    }

    /**
     * 判断是否属于今年
     *
     * @param inputTime 指定时间
     * @return true 属于，false不属于
     */
    private static boolean isBelongToThisYear(long inputTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(inputTime);
        int year = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(System.currentTimeMillis());
        int curYear = calendar.get(Calendar.YEAR);
        if (year == curYear)
            return true;
        return false;
    }

    /**
     * 判断是否属于近几天中那一天的时间
     *
     * @param inputTime 指定时间
     * @param dayBefore 0代表今天，-1代表昨天，-2代表前天，，，，
     * @return true 属于指定那天，false不属于指定那天
     */
    private static boolean isTheDayBefore(long inputTime, int dayBefore) {
        TimeInfo ti = getDayStartAndEndTime(dayBefore);
        if (inputTime > ti.getStartTime() && inputTime < ti.getEndTime())
            return true;
        return false;
    }

    /**
     * 近几天的开始和结束时间信息，24小时制
     *
     * @param dayBefore 0代表今天，-1代表昨天，-2代表前天，，，，
     * @return 近几天的开始和结束时间信息，24小时制
     */
    public static TimeInfo getDayStartAndEndTime(int dayBefore) {
        Calendar calendar1 = Calendar.getInstance();
        if (dayBefore != 0) {
            calendar1.add(Calendar.DATE, dayBefore);
        }
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();

        Calendar calendar2 = Calendar.getInstance();
        if (dayBefore != 0) {
            calendar2.add(Calendar.DATE, -1);
        }
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();

        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    /**
     * 判断输入时间是星期几
     *
     * @param date 要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String DateToWeek(Date date) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String week[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return week[dayOfWeek - 1];
    }

    public static int getAge(Date birth) {
        return (int) ((System.currentTimeMillis() - birth.getTime())/DateUtils.YEAR_IN_MILLIS);
    }

    public static class TimeInfo {
        private long startTime;
        private long endTime;

        public TimeInfo() {
        }

        public long getStartTime() {
            return this.startTime;
        }

        public void setStartTime(long var1) {
            this.startTime = var1;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public void setEndTime(long var1) {
            this.endTime = var1;
        }
    }

    //计算倒计时的时间
    public static String endData(Long time) {
        long second = time / 1000;
        if (second < 60) {
            return "0:0:" + second;
        }
        long minute = second / 60;
        if (minute < 60) {
            second = second - 60 * minute;
            return "0:" + minute + ":" + second;
        }
        long hour = minute / 60;
        if (hour < 24) {
            second = second - 60 * minute;
            minute = minute - 60 * hour;
            return hour + ":" + minute + ":" + second;
        }
        long day = hour / 24;
        second = second - 60 * minute;
        minute = minute - 60 * hour;
        hour = hour - 24 * day;
        return day + "天" + " " + hour + ":" + minute + ":" + second;
    }

}