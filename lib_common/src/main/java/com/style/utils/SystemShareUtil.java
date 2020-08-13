package com.style.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by xiajun on 2018/9/4.
 */

public class SystemShareUtil {
    public static void shareText(Context context, CharSequence title, String value) {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, value);
        context.startActivity(Intent.createChooser(textIntent, title));
    }

    public static void shareImage(Context context, @NonNull String authority, String path) {
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/jpeg");
        Uri uri = FileProvider.getUriForFile(context, authority, new File(path));
        imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(imageIntent, "图片"));
    }
}
