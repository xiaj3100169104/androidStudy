package example.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.style.app.FileDirConfig;
import com.style.base.BaseDefaultTitleBarActivity;
import com.style.data.glide.GlideCircleTransform;
import com.style.data.glide.CornerRectTransform;
import com.style.data.glide.GlideRoundTransform;
import com.style.dialog.SelAvatarDialog;
import com.style.framework.R;
import com.style.framework.databinding.ActivityGlideDealBinding;
import com.style.utils.BitmapUtil;
import com.style.utils.DeviceInfoUtil;
import com.style.utils.FileUtil;
import com.style.utils.PictureUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import tech.gaolinfeng.imagecrop.lib.ImageCropActivity;


public class GlideDealActivity extends BaseDefaultTitleBarActivity {

    private ActivityGlideDealBinding bd;
    private SelAvatarDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_glide_deal);
        bd = getBinding();
        bd.btnCircle.setOnClickListener(v -> dealCircle());
        bd.btnRound.setOnClickListener(v -> dealRound());
        bd.btnRectStroke.setOnClickListener(v -> dealRectStroke());
        bd.btnAvatar.setOnClickListener(v -> selAvatar());

    }

    public void dealCircle() {
        //第一个是上下文，第二个是圆角的弧度
        RequestOptions myOptions = new RequestOptions().transform(new GlideCircleTransform(2, 0xFFFFAEB9));
        Glide.with(this).load(R.mipmap.image_fail).apply(myOptions).into(bd.iv1);
    }

    public void dealRound() {
        RequestOptions myOptions = new RequestOptions().transform(new GlideRoundTransform(5)).skipMemoryCache(true);
        Glide.with(this).load(R.mipmap.ic_add_photo).apply(myOptions).into(bd.iv2);
    }

    public void dealRectStroke() {
        RequestOptions myOptions = new RequestOptions().transform(new CornerRectTransform(4, 0xFFFF6347)).skipMemoryCache(true);
        Glide.with(this).load(R.mipmap.empty_photo).apply(myOptions).into(bd.iv3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onCropImageResult(requestCode, resultCode, data);
    }

    public void selAvatar() {
        showSelPicPopupWindow();
    }

    protected void onAvatarCropped(String targetPath) {
        File f = new File(targetPath);
        Log.e(getTAG(), "文件大小   " + f.length() / 1024);
        RequestOptions myOptions = new RequestOptions();
        Glide.with(this).load(targetPath).apply(myOptions).into(bd.ivAvatar);

    }

    protected void showSelPicPopupWindow() {
        if (dialog == null) {
            dialog = new SelAvatarDialog(this);
            dialog.setOnItemClickListener(new SelAvatarDialog.OnItemClickListener() {
                @Override
                public void OnClickCamera() {
                    checkCameraAndExternalStoragePermission();
                }

                @Override
                public void OnClickPhoto() {
                    checkExternalStoragePermission();
                }

                @Override
                public void OnClickCancel() {

                }
            });
        }
        dialog.show();
    }

    @SuppressLint("CheckResult")
    protected void checkCameraAndExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(grated -> {
                        if (grated) {
                            takePhoto();
                        } else {
                            showToast(R.string.error_no_camera_and_external_storage_permission);
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                    });
        } else {
            takePhoto();
        }
    }

    @SuppressLint("CheckResult")
    protected void checkExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(grated -> {
                        if (grated) {
                            selectPhoto();
                        } else {
                            showToast(R.string.error_no_external_storage_permission);
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                    });
        } else {
            selectPhoto();
        }
    }

    protected String getCameraPhotoPath() {
        return FileDirConfig.DIR_APP_IMAGE_CAMERA + File.separatorChar + String.valueOf(System.currentTimeMillis()) + ".jpg";
    }

    protected String getCopyFilePath() {
        return FileDirConfig.DIR_CACHE + File.separatorChar + String.valueOf(System.currentTimeMillis()) + ".image";
    }

    protected String getTargetFilePath() {
        return FileDirConfig.DIR_CACHE + File.separatorChar + String.valueOf(System.currentTimeMillis()) + ".image";
    }

    private int getMaxCropSize() {
        return 100;
    }

    private File photoFile;
    private String cropImagePath;

    public static final int CODE_TAKE_CAMERA = 997;// 拍照
    public static final int CODE_TAKE_ALBUM = 998;// 从相册中选择
    public static final int CODE_PHOTO_CROP = 999;// 系统裁剪头像

    protected void onCropImageResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_TAKE_CAMERA:// 拍照
                    if (null != photoFile && photoFile.exists()) {
                        try {
                            DeviceInfoUtil.notifyUpdateGallary(this, photoFile);// 通知系统更新相册
                            boolean isSucceed;
                            //需要把原文件复制一份，否则会在原文件上操作
                            String mCopyFilePath = getCopyFilePath();
                            //取出目标路径
                            cropImagePath = getTargetFilePath();
                            int degree = PictureUtils.readPictureDegree(photoFile.getAbsolutePath());
                            logE(getTAG(), "拍照后的角度：" + degree);
                            if (degree != 0) {// 旋转图片,保存
                                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getPath());
                                bitmap = BitmapUtil.rotateImageView(bitmap, degree);
                                BitmapUtil.saveBitmap(mCopyFilePath, bitmap, 100);
                                isSucceed = true;
                            } else {
                                isSucceed = FileUtil.copyFile(photoFile, mCopyFilePath);
                            }
                            if (isSucceed)
                                dealPictureCrop(mCopyFilePath, cropImagePath);
                            else
                                showToast("图片创建失败");
                        } catch (IOException e) {
                            e.printStackTrace();
                            showToast("图片创建失败");
                        }
                    } else {
                        showToast(R.string.file_does_not_exist);
                    }
                    break;
                case CODE_TAKE_ALBUM:// 本地
                    if (data != null) {
                        Uri uri = data.getData();
                        photoFile = FileUtil.UriToFile(this, uri);
                        //需要把原文件复制一份，否则会在原文件上操作
                        String mCopyFilePath = getCopyFilePath();
                        //取出目标路径
                        cropImagePath = getTargetFilePath();
                        boolean isSucceed = FileUtil.copyFile(photoFile, mCopyFilePath);
                        if (isSucceed)
                            dealPictureCrop(mCopyFilePath, cropImagePath);
                        else
                            showToast("图片创建失败");
                    } else {
                        showToast(R.string.file_does_not_exist);
                    }
                    break;
                case CODE_PHOTO_CROP:
                    onAvatarCropped(cropImagePath);
                    break;
            }
        }
    }

    protected void dealPictureCrop(String originalFilePath, String targetPath) {
        logE(getTAG(), "待裁剪图片路径-->" + originalFilePath);
        logE(getTAG(), "裁剪后图片路径-->" + targetPath);
        Intent intent = ImageCropActivity.createIntent(this, originalFilePath, targetPath, getCropAreaStr(), false, getMaxCropSize());
        startActivityForResult(intent, CODE_PHOTO_CROP);
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
        startActivityForResult(intent, CODE_TAKE_ALBUM);
    }


    protected void takePhoto() {
        photoFile = new File(getCameraPhotoPath());
        if (FileUtil.isNewFileCanWrite(photoFile)) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, FileDirConfig.FILE_PROVIDER_AUTHORITY, photoFile);
            } else {
                uri = Uri.fromFile(photoFile);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CODE_TAKE_CAMERA);
        } else {
            showToast("图片创建失败");
        }
    }
}
