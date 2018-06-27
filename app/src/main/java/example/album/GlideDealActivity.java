package example.album;

import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.style.app.ConfigUtil;
import com.style.base.BaseActivityPresenter;
import com.style.base.BaseCropActivity;
import com.style.data.glide.GlideCircleTransform;
import com.style.data.glide.GlideRectBoundTransform;
import com.style.data.glide.GlideRoundTransform;
import com.style.dialog.SelAvatarDialog;
import com.style.framework.R;
import com.style.framework.databinding.ActivityGlideDealBinding;
import com.style.utils.DeviceInfoUtil;

import java.io.File;


public class GlideDealActivity extends BaseCropActivity {

    private ActivityGlideDealBinding bd;
    private SelAvatarDialog dialog;

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_glide_deal;
    }

    @Override
    public void initData() {
        bd = getBinding();

    }

    public void skip418(View v) {
        //第一个是上下文，第二个是圆角的弧度
        RequestOptions myOptions = new RequestOptions().transform(new GlideCircleTransform(2, 0xFFFFAEB9));
        Glide.with(this).load(R.mipmap.image_fail).apply(myOptions).into(bd.iv1);
    }

    public void skip419(View v) {
        RequestOptions myOptions = new RequestOptions().transform(new GlideRoundTransform(5)).skipMemoryCache(true);
        Glide.with(this).load(R.mipmap.ic_add_photo).apply(myOptions).into(bd.iv2);
    }

    public void skip420(View v) {
        RequestOptions myOptions = new RequestOptions().transform(new GlideRectBoundTransform(4, 0xFFFF6347)).skipMemoryCache(true);
        Glide.with(this).load(R.mipmap.empty_photo).apply(myOptions).into(bd.iv3);
    }

    public void selAvatar(View v) {
        showSelPicPopupWindow();
    }

    protected void showSelPicPopupWindow() {
        if (dialog == null) {
            dialog = new SelAvatarDialog(this);
            dialog.setOnItemClickListener(new SelAvatarDialog.OnItemClickListener() {
                @Override
                public void OnClickCamera() {
                    if (!DeviceInfoUtil.isSDcardWritable()) {
                        showToast("sd卡不可用");
                        return;
                    }
                    initPermission();
                }

                @Override
                public void OnClickPhoto() {
                    if (!DeviceInfoUtil.isSDcardWritable()) {
                        showToast("sd卡不可用");
                        return;
                    }
                    selectPhoto();
                }

                @Override
                public void OnClickCancel() {

                }
            });
        }
        dialog.show();
    }

    @Override
    protected void onAvatarCropped(String targetPath) {
        File f = new File(targetPath);
        Log.e(TAG, "文件大小   " + f.length() / 1024);
        RequestOptions myOptions = new RequestOptions();
        Glide.with(this).load(targetPath).apply(myOptions).into(bd.ivAvatar);

    }

    @Override
    protected String getCameraPhotoPath() {
        return ConfigUtil.DIR_APP_IMAGE_CAMERA + File.separatorChar + String.valueOf(System.currentTimeMillis()) + ".jpg";
    }

    @Override
    protected String getCopyFilePath() {
        return ConfigUtil.DIR_CACHE + File.separatorChar + String.valueOf(System.currentTimeMillis()) + ".image";
    }

    @Override
    protected String getTargetFilePath() {
        return ConfigUtil.DIR_CACHE + File.separatorChar + String.valueOf(System.currentTimeMillis()) + ".image";
    }

    @Override
    protected int getMaxCropSize() {
        return 100;
    }
}
