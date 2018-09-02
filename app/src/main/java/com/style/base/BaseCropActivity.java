package com.style.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.style.app.ConfigUtil;
import com.style.data.glide.GlideCircleTransform;
import com.style.data.glide.GlideRectBoundTransform;
import com.style.data.glide.GlideRoundTransform;
import com.style.dialog.SelAvatarDialog;
import com.style.framework.BuildConfig;
import com.style.framework.R;
import com.style.framework.databinding.ActivityGlideDealBinding;
import com.style.utils.DeviceInfoUtil;
import com.style.utils.FileUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import tech.gaolinfeng.imagecrop.lib.ImageCropActivity;


public abstract class BaseCropActivity extends BaseActivity {

    private File photoFile;
    private String targetPath;

    /**
     * 设置拍照的图片路径
     *
     * @return
     */
    protected abstract String getCameraPhotoPath();

    /**
     * 设置用来处理剪裁的文件路径
     * 因为不能再原图做裁剪处理
     *
     * @return
     */
    protected abstract String getCopyFilePath();

    /**
     * 设置裁剪后的文件路径
     *
     * @return
     */
    protected abstract String getTargetFilePath();

    /**
     * 设置裁剪压缩后文件最大长度，默认100kb，头像适合
     * @return
     */
    protected abstract int getMaxCropSize();

    /**
     * 裁剪完成回调
     *
     * @param targetPath 裁剪后的文件路径
     */
    protected abstract void onAvatarCropped(String targetPath);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:// 拍照
                    if (null != photoFile && photoFile.exists()) {
                        DeviceInfoUtil.notifyUpdateGallary(this, photoFile);// 通知系统更新相册
                        dealPicture(photoFile);
                    } else {
                        showToast(R.string.File_does_not_exist);
                    }
                    break;
                case 2:// 本地
                    if (data != null) {
                        Uri uri = data.getData();
                        photoFile = FileUtil.UriToFile(this, uri);
                        dealPicture(photoFile);
                    } else {
                        showToast(R.string.File_does_not_exist);
                    }
                    break;
                case 3:
                    onAvatarCropped(targetPath);
                    break;
            }
        }
    }

    protected void dealPicture(File originalFile) {
        //需要把原文件复制一份，否则会在原文件上操作
        String copeFilePath = getCopyFilePath();
        File copeFile = new File(copeFilePath);
        if (copeFile.exists()) {
            copeFile.delete();
        }
        if (!copeFile.getParentFile().exists()) {
            copeFile.getParentFile().mkdirs();
        }
        boolean isCopy = FileUtil.copyfile(originalFile, copeFile, true);
        if (isCopy) {
            Log.e(getTAG(), "图片复制后的路径-->" + copeFilePath);
            targetPath = getTargetFilePath();
            Log.e(getTAG(), "图片裁剪后的路径-->" + targetPath);
            Intent intent = ImageCropActivity.createIntent(this, copeFilePath, targetPath, getCropAreaStr(), false, getMaxCropSize());
            startActivityForResult(intent, 3);
        } else {
            showToast("复制图片出错");
        }
    }

    protected String getCropAreaStr() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        int rectWidth = screenWidth * 2 / 3;
        int left = screenWidth / 6;
        int right = left + rectWidth;
        int top = (screenHeight - rectWidth) / 2;
        int bottom = top + rectWidth;
        return left + ", " + top + ", " + right + ", " + bottom;
    }

    protected void selectPhoto() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.putExtra("crop", false);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }


    protected void takePhoto() {
        String imageFilePath = getCameraPhotoPath();
        photoFile = new File(imageFilePath);
        if (photoFile.exists()) {
            photoFile.delete();
        }
        if (!photoFile.getParentFile().exists()) {
            photoFile.getParentFile().mkdirs();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", photoFile);
        } else {
            uri = Uri.fromFile(photoFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1);
    }

    protected void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.CAMERA)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(grated -> {
                        if (grated) {
                            takePhoto();
                        } else {
                            //onError("请开启相机权限");
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                    });
        } else {
            takePhoto();
        }
    }

}
