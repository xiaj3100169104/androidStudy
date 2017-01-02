package com.style.helper;

import android.content.Context;

import com.style.framework.R;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    public static final String UNLIMITED = "不限";
    private static final String CM = "cm";
    private static final String KG = "kg";

    public static List<String> getSexSelf(Context context) {
        String[] s = getStringArray(context, R.array.sex);
        List<String> list = getList(s);
        return list;
    }

    public static List<String> getSexCondition(Context context) {
        List<String> list = getSexSelf(context);
        list.add(0, UNLIMITED);
        return list;
    }

    public static List<String> getAgeSelf() {
        List<String> list = new ArrayList<>();
        for (int i = 16; i <= 70; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    public static List<String> getAgeCondition() {
        List<String> list = getAgeSelf();
        list.add(0, UNLIMITED);
        return list;
    }

    public static List<String> getBodyHeightSelf() {
        List<String> list = new ArrayList<>();
        for (int i = 150; i <= 210; i++) {
            list.add(String.valueOf(i) + CM);
        }
        return list;

    }

    public static List<String> getBodyHeightCondition() {
        List<String> list = getBodyHeightSelf();
        list.add(0, UNLIMITED);
        return list;
    }

    public static List<String> getBodyWeightSelf() {
        List<String> list = new ArrayList<>();
        for (int i = 35; i <= 150; i++) {
            list.add(String.valueOf(i) + KG);
        }
        return list;

    }

    public static List<String> getIncomeSelf(Context context) {
        String[] s = getStringArray(context, R.array.income_groups);
        List<String> list = getList(s);
        return list;
    }

    public static List<String> getIncomeCondition(Context context) {
        String[] s = getStringArray(context, R.array.income_condition);
        List<String> list = getList(s);
        return list;
    }

    public static List<String> getEducationSelf(Context context) {
        String[] s = getStringArray(context, R.array.education);
        List<String> list = getList(s);
        return list;
    }

    public static List<String> getEducationCondition(Context context) {
        String[] s = getStringArray(context, R.array.condition_education);
        List<String> list = getList(s);
        return list;
    }

    public static List<String> getOccupationSelf(Context context) {
        String[] s = getStringArray(context, R.array.occupations);
        List<String> list = getList(s);
        return list;
    }

    public static List<String> getHousingSituationSelf(Context context) {
        String[] s = getStringArray(context, R.array.housing_situation);
        List<String> list = getList(s);
        return list;
    }

    public static List<String> getEmotionStateSelf(Context context) {
        String[] s = getStringArray(context, R.array.emotion_state);
        List<String> list = getList(s);
        return list;
    }

    public static List<String> getEmotionStateCondition(Context context) {
        List<String> list = getEmotionStateSelf(context);
        list.add(0, UNLIMITED);
        return list;
    }

    public static List<String> getList(String[] s) {
        List<String> list = new ArrayList<>();
        if (s != null) {
            int length = s.length;
            for (int i = 0; i < length; i++) {
                list.add(s[i]);
            }
        }
        return list;
    }

    public static String[] getStringArray(Context context, int resId) {
        String[] s = context.getResources().getStringArray(resId);
        return s;
    }
}
