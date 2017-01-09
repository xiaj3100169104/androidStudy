package com.style.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiajun on 2017/1/9.
 * 资源获取工具类
 */

public class AssetsUtil {

    public static String getAssetsText(Context context, String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String text = new String(buffer, "utf-8");
        return text;
    }

    public static List<String> getFromAssets(Context context, String fileName, String charSet) {
        ArrayList<String> keyWords = new ArrayList<>();
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getAssets().open(fileName), charSet);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                keyWords.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyWords;
    }
}
