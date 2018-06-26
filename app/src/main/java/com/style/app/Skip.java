package com.style.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.style.framework.BuildConfig;
import com.style.utils.FileUtil;

import java.io.File;

/**
 * * 避免跳转传值的key和请求码重复混乱，最好统一放在这里
 * Created by xiajun on 2016/11/25.
 */
public class Skip {
    /**
     * 界面跳转请求码
     */
    public static final int CODE_TAKE_CAMERA = 0x000001;// 拍照
    public static final int CODE_TAKE_ALBUM = 0x000002;// 从相册中选择
    public static final int CODE_PHOTO_CROP = 0x000003;// 系统裁剪头像

    public static File takePhoto(Activity activity, String dir, String name) {
        File f = FileUtil.create(dir, name);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileProvider", f);
        } else {
            uri = Uri.fromFile(f);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, CODE_TAKE_CAMERA);
        return f;
    }

    public static void selectPhoto(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, CODE_TAKE_ALBUM);
    }
}
