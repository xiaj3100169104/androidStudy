package example.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.palette.graphics.Palette;

import android.util.DisplayMetrics;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.style.config.FileDirConfig;
import com.style.base.BaseTitleBarActivity;
import com.style.data.glide.RectTopCornerTransform;
import com.style.data.glide.RoundTransform;
import com.style.data.glide.RoundRectTransform;
import com.style.dialog.SelAvatarDialog;
import com.style.framework.R;
import com.style.framework.databinding.ActivityGlideDealBinding;
import com.style.utils.BitmapUtil;
import com.style.utils.DeviceInfoUtil;
import com.style.utils.FileUtil;
import com.style.utils.PictureUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import tech.gaolinfeng.imagecrop.lib.ImageCropActivity;


public class GlideDealActivity extends BaseTitleBarActivity {

    private ActivityGlideDealBinding bd;
    private SelAvatarDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        bd = ActivityGlideDealBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        bd.btnCircle.setOnClickListener(v -> dealCircle());
        bd.btnCircleBorder.setOnClickListener(v -> dealCircleBorder());
        bd.btnRound.setOnClickListener(v -> dealRound());
        bd.btnRectStroke.setOnClickListener(v -> dealRectStroke());
        bd.btnRectTop.setOnClickListener(v -> dealRectTop());
        bd.btnImageSize.setOnClickListener(v -> compressImage());

        bd.btnAvatar.setOnClickListener(v -> selAvatar());
        bd.btnCatchColor.setOnClickListener(v -> {
            //bd.ivAvatar.setImageResource(R.mipmap.home_banner_3);
            bd.ivAvatar.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(bd.ivAvatar.getDrawingCache());
            bd.ivAvatar.setDrawingCacheEnabled(false);
            // 用来提取颜色的Bitmap
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.home_banner_3);
            catchColor(bitmap);
        });
        downImage();
    }

    private void compressImage() {
        //String path = "/storage/emulated/0/aaaaStyle/cache/1574660481789.jpg";
        String path = "/storage/emulated/0/aaaaStyle/cache/1574493622274.jpg";
        Bitmap b = BitmapUtil.getThumbnail(path, bd.iv3.getWidth(), bd.iv3.getHeight());
        Bitmap b2 = BitmapUtil.centerCrop(b, bd.iv3.getWidth(), bd.iv3.getHeight());
        bd.iv3.setImageBitmap(b2);
    }

    private void dealRectTop() {
        String url = "http://test-assets.wujinpu.cn/goods/goodsInfoImg/1a8e3067505640458199991ead4ee66e/4811568189101270.jpg?x-oss-process=style/280";
        RequestOptions myOptions = new RequestOptions().transform(new RectTopCornerTransform(8f))
                .error(R.mipmap.empty_photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(url).apply(myOptions).into(bd.iv1);
    }

    private void dealCircle() {
        RequestOptions myOptions = new RequestOptions().transform(new RoundTransform()).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(R.mipmap.empty_photo).apply(myOptions).into(bd.iv1);
    }

    private void dealCircleBorder() {
        RequestOptions myOptions = new RequestOptions().transform(new RoundTransform(1.5f, 0xFFFFAEB9)).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(R.mipmap.empty_photo).apply(myOptions).into(bd.iv1);
    }

    private void dealRound() {
        RequestOptions myOptions = new RequestOptions().transform(new RoundRectTransform(8f)).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(R.mipmap.empty_photo).apply(myOptions).into(bd.iv1);
    }

    private void dealRectStroke() {
        RequestOptions myOptions = new RequestOptions().transform(new RoundRectTransform(8f, 4f, 0xFFFF6347)).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(R.mipmap.empty_photo).apply(myOptions).into(bd.iv1);
    }

    private void downImage() {
        String url = "https://test-assets.wujinpu.cn/back/banner/8881573630042201.jpg";
        RequestOptions myOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).asBitmap().load(url).apply(myOptions).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Bitmap b = BitmapUtil.centerCrop(resource, bd.iv2.getWidth(), bd.iv2.getHeight());
                bd.iv2.setImageBitmap(b);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private void catchColor(Bitmap bitmap) {
        Palette.Builder pb = new Palette.Builder(bitmap);
        pb.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                //暗、柔和
                int darkMutedColor = palette.getDarkMutedColor(Color.BLACK);//如果分析不出来，则返回默认颜色
                //亮、柔和
                int lightMutedColor = palette.getLightMutedColor(Color.BLACK);
                //暗、鲜艳
                int darkVibrantColor = palette.getDarkVibrantColor(Color.BLACK);
                //亮、鲜艳
                int lightVibrantColor = palette.getLightVibrantColor(Color.BLACK);
                //柔和
                int mutedColor = palette.getMutedColor(Color.BLACK);
                //鲜艳
                int vibrantColor = palette.getVibrantColor(Color.BLACK);
                bd.btnCatchColor.setBackgroundColor(vibrantColor);
                /*//获取某种特性颜色的样品
                //Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
                Palette.Swatch lightVibrantSwatch = palette.getVibrantSwatch();
                //谷歌推荐的：图片的整体的颜色rgb的混合值---主色调
                int rgb = lightVibrantSwatch.getRgb();
                //谷歌推荐：图片中间的文字颜色
                int bodyTextColor = lightVibrantSwatch.getBodyTextColor();
                //谷歌推荐：作为标题的颜色（有一定的和图片的对比度的颜色值）
                int titleTextColor = lightVibrantSwatch.getTitleTextColor();
                //颜色向量
                float[] hsl = lightVibrantSwatch.getHsl();
                //分析该颜色在图片中所占的像素多少值
                int population = lightVibrantSwatch.getPopulation();*/
            }
        });
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
        Glide.with(this).asBitmap().load(targetPath).apply(myOptions).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                catchColor(resource);
                bd.ivAvatar.setImageBitmap(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap b = BitmapFactory.decodeFile(targetPath, options);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //如果已知，则解码后的位图将具有配置。
            if (options.outConfig != null) {
                Log.e("outConfig", options.outConfig + "");
            }
        }
        FaceDetector faceDet = new FaceDetector(b.getWidth(), b.getHeight(), 1);
        FaceDetector.Face[] faceList = new FaceDetector.Face[1];
        int n = faceDet.findFaces(b, faceList);
        Log.e("人脸数", n + "");
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
    //裁剪后图片输出路径
    private String cropImagePath;

    public static final int CODE_TAKE_CAMERA = 997;// 拍照
    public static final int CODE_TAKE_ALBUM = 998;// 从相册中选择
    public static final int CODE_PHOTO_CROP = 999;// 系统裁剪头像

    protected void onCropImageResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_TAKE_CAMERA:// 拍照返回
                    if (null != photoFile && photoFile.exists()) {
                        DeviceInfoUtil.notifyUpdateGallary(this, photoFile);// 通知系统更新相册
                        dealPictureFile(photoFile);
                    } else {
                        showToast(R.string.file_does_not_exist);
                    }
                    break;
                case CODE_TAKE_ALBUM:// 相册返回
                    if (data != null) {
                        Uri uri = data.getData();
                        photoFile = FileUtil.UriToFile(this, uri);
                        dealPictureFile(photoFile);
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

    /**
     * 旋转、复制图片
     *
     * @param photoFile
     */
    private void dealPictureFile(File photoFile) {
        try {
            //读取图片拍摄角度
            int degree = PictureUtil.readPictureDegree(photoFile.getAbsolutePath());
            logE(getTAG(), "拍照后的角度：" + degree);
            boolean isSucceed;
            //需要把原文件复制一份，否则会在原文件上操作
            String mCopyFilePath = getCopyFilePath();
            if (degree != 0) {// 旋转图片,保存
                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getPath());
                bitmap = BitmapUtil.rotateImageView(bitmap, degree);
                BitmapUtil.saveBitmap(mCopyFilePath, bitmap, 100);
                isSucceed = true;
            } else {
                isSucceed = FileUtil.copyFile(photoFile, mCopyFilePath);
            }
            if (isSucceed) {
                cropImagePath = getTargetFilePath();
                skipToCrop(mCopyFilePath, cropImagePath);
            } else
                showToast("图片创建失败");
        } catch (IOException e) {
            e.printStackTrace();
            showToast("图片创建失败");
        }
    }

    /**
     * 跳转到图片裁剪界面
     *
     * @param originalFilePath
     * @param targetPath
     */
    protected void skipToCrop(String originalFilePath, String targetPath) {
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
