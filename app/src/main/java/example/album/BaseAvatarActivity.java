package example.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.style.app.ConfigUtil;
import com.style.app.Skip;
import com.style.base.BaseActivity;
import com.style.dialog.SelAvatarDialog;
import com.style.framework.BuildConfig;
import com.style.framework.R;
import com.style.utils.BitmapUtil;
import com.style.utils.DeviceInfoUtil;
import com.style.utils.FileUtil;
import com.style.utils.PictureUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class BaseAvatarActivity extends BaseActivity {

    private File photoFile;
    private boolean isFromCamera;
    private SelAvatarDialog dialog;
    private Uri uri2;

    protected void showSelPicPopupWindow() {
        if (dialog == null) {
            dialog = new SelAvatarDialog(this, R.style.Dialog_General);
            dialog.setOnItemClickListener(new SelAvatarDialog.OnItemClickListener() {
                @Override
                public void OnClickCamera() {
                    if (!DeviceInfoUtil.isSDcardWritable()) {
                        showToast("sd卡不可用");
                        return;
                    }
                    photoFile = Skip.takePhoto((Activity) getContext(), ConfigUtil.DIR_APP_IMAGE_CAMERA, String.valueOf(System.currentTimeMillis()) + ".jpg");

                }

                @Override
                public void OnClickPhoto() {
                    if (!DeviceInfoUtil.isSDcardWritable()) {
                        showToast("sd卡不可用");
                        return;
                    }
                    Skip.selectPhoto((Activity) getContext());
                }

                @Override
                public void OnClickCancel() {

                }
            });
        }
        dialog.show();
    }

    protected void onAvatarCropped(String savePath) {
        File f = new File(savePath);
        logE(TAG, "文件大小   " + f.length() / 1024);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Skip.CODE_TAKE_CAMERA:// 拍照
                    isFromCamera = true;
                    if (null != photoFile && photoFile.exists()) {
                        DeviceInfoUtil.notifyUpdateGallary(this, photoFile);// 通知系统更新相册
                        dealPicture(photoFile);
                    } else {
                        showToast(R.string.File_does_not_exist);
                    }
                    break;
                case Skip.CODE_TAKE_ALBUM:// 本地
                    if (data != null) {
                        isFromCamera = false;
                        Uri uri = data.getData();
                        File fromFile = FileUtil.UriToFile(this, uri);
                        dealPicture(fromFile);
                    } else {
                        showToast(R.string.File_does_not_exist);
                    }
                    break;
                case Skip.CODE_PHOTO_CROP:// 裁剪头像返回
                    if (data != null) {
                        int degree = 0;
                        if (isFromCamera) {
                            if (photoFile.exists()) {
                                degree = PictureUtils.readPictureDegree(photoFile.getAbsolutePath());
                                logE("life", "拍照后的角度：" + degree);
                            } else {
                                showToast(R.string.File_does_not_exist);
                            }
                        }
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri2));
                            if (isFromCamera && degree != 0) {// 旋转图片 动作
                                bitmap = BitmapUtil.rotaingImageView(bitmap, 0);
                            }
                            String savePath = ConfigUtil.DIR_CACHE + "/" + System.currentTimeMillis() + ".image";
                            //压缩图片
                            Bitmap b = BitmapUtil.compressImage(bitmap, 100);
                            // 保存图片
                            BitmapUtil.saveBitmap(savePath, b);
                            Log.e("PersonAvatar", "图片的路径是--->" + savePath);
                            onAvatarCropped(savePath);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }

    }

    private void dealPicture(File fromFile) {
        //需要把原文件复制一份，否则会在原文件上操作
        File f = new File(ConfigUtil.DIR_CACHE, String.valueOf(System.currentTimeMillis()) + ".image");
        if (f.exists()) {
            f.delete();
        }
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        boolean isCopy = FileUtil.copyfile(fromFile, f, true);
        if (isCopy) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri2 = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", f);
            } else {
                uri2 = Uri.fromFile(f);
            }

            Log.e("PersonAvatar", "本地图片的路径-->" + uri2);
            Intent i = getCropImageIntent(uri2);
            this.startActivityForResult(i, Skip.CODE_PHOTO_CROP);
        } else {
            showToast("复制图片出错");
        }
    }


    /**
     * 获取跳到裁剪图片界面的意图
     *
     * @param uri
     */
    public static Intent getCropImageIntent(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 声明需要的零时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //裁剪框大小
        intent.putExtra("aspectX", 200);
        intent.putExtra("aspectY", 200);
        //保存图片的大小,一定不能比 aspectX aspectY大，否则会闪退
        intent.putExtra("outputX", 160);
        intent.putExtra("outputY", 160);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //为true直接返回bitmap数据，但在
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        return intent;
    }
    /*方法1：如果你将return-data设置为“true”，你将会获得一个与内部数据关联的Action，并且bitmap以此方式返回：(Bitmap)extras.getParcelable("data")。注意：如果你最终要获取的图片非常大，那么此方法会给你带来麻烦，所以你要控制outputX和outputY保持在较小的尺寸。鉴于此原因，在我的代码中没有使用此方法（(Bitmap)extras.getParcelable("data")）。

    下面是CropImage.java的源码片段：

            1
// Return the cropped image directly or save it to the specified URI.
            2
    Bundle myExtras = getIntent().getExtras();
3
        if (myExtras != null && (myExtras.getParcelable("data") != null|| myExtras.getBoolean("return-data")))
            4
    {
        5
        Bundle extras = new Bundle();
        6
        extras.putParcelable("data", croppedImage);
        7
        setResult(RESULT_OK,(new Intent()).setAction("inline-data").putExtras(extras));
        8
        finish();
        9
    }          方法2： 如果你将return-data设置为“false”，那么在onActivityResult的Intent数据中你将不会接收到任何Bitmap，相反，你需要将MediaStore.EXTRA_OUTPUT关联到一个Uri，此Uri是用来存放Bitmap的。
    但是还有一些条件，首先你需要有一个短暂的与此Uri相关联的文件地址，当然这不是个大问题（除非是那些没有sdcard的设备）。*/
}
