package com.style.constant;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.style.utils.FileUtil;

import java.io.File;
import java.util.List;

/**
 *  * 避免跳转传值的key和请求码重复混乱，最好统一放在这里
 * Created by xiajun on 2016/11/25.
 */
public class Skip {
    /**
     * 界面跳转请求码
     */
    public static final int CODE_TAKE_CAMERA = 0x000001;// 拍照
    public static final int CODE_TAKE_ALBUM = 0x000002;// 从相册中选择
    public static final int CODE_PHOTO_CROP = 0x000003;// 系统裁剪头像
    public static final int CODE_EMPTY_HISTORY = 0x000004;
    public static final int CODE_MAP = 0x000005;
    public static final int CODE_COPY_AND_PASTE = 0x000006;
    public static final int CODE_SELECT_FILE = 0x000007;
    /**
     * 界面跳转传值key
     */
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_SEARCH_RESULTS = "search_results";
    public static final String KEY_IMG_NAME = "img_name";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER = "user";
    public static final String KEY_CURUSER = "curUser";
    public static final String KEY_OUSER = "oUser";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMODATA = "emoData";
    public static final String KEY_USERDYNAMIC = "UserDynamic";
    public static final String KEY_POSITION = "position";
    public static final String KEY_SQUAREINFO = "SquareInfo";
    public static final String KEY_ISDELETE = "isDelete";
    public static final String KEY_USERLIST = "userList";

    public static void skipClearTop(Context context, Class<?> cls) {
        context.startActivity(new Intent().setClass(context, cls).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public static void skipClearTop(Context context, Class<?> cls, Intent intent) {
        intent.setClass(context, cls).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static File takePhoto(Activity activity, String dir, String name) {
        File photo = FileUtil.create(dir, name);
        Uri imageUri = Uri.fromFile(photo);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, CODE_TAKE_CAMERA);
        return photo;
    }

    public static File takePhotoFromFragment(Fragment fragment, String dir, String name) {
        File photo = FileUtil.create(dir, name);
        Uri imageUri = Uri.fromFile(photo);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        fragment.startActivityForResult(intent, CODE_TAKE_CAMERA);
        return photo;
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

    public static void selectPhotoFromFragment(Fragment activity) {
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
