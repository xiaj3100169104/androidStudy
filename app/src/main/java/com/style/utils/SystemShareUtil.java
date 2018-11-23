package com.style.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.style.app.FileDirConfig;

import java.io.File;

/**
 * Created by xiajun on 2018/9/4.
 */

public class SystemShareUtil {
    public static void shareText(Context context, String value) {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, value);
        context.startActivity(Intent.createChooser(textIntent, "文章"));
    }

    public static void shareImage(Context context, String path) {
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/jpeg");
        Uri uri = FileProvider.getUriForFile(context, FileDirConfig.FILE_PROVIDER_AUTHORITY, new File(path));
        imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(imageIntent, "图片"));
    }
}
