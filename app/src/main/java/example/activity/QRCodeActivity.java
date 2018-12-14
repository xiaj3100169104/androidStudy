package example.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.encoding.EncodingHandler;
import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityQrCodeScanBinding;

public class QRCodeActivity extends BaseDefaultTitleBarActivity {
    public static final String url = "https://github.com/xj913492952";
    private ActivityQrCodeScanBinding bd;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_qr_code_scan;
    }

    @Override
    protected void initData() {
        bd = getBinding();
        init();
        bd.btnScaner.setOnClickListener(v -> {
            checkCameraPermission();
        });
    }

    private void init() {
        try {
            bd.iamge1.setImageBitmap(EncodingHandler.createQRCode(url, 500));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.k);
        bd.iamge2.setImageBitmap(EncodingHandler.createQRCode(url, 500, 500, bitmap));
    }

    private void checkCameraPermission() {
        //第二个参数是需要申请的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        } else {
            //权限已经被授予，在这里直接写要执行的相应方法即可
            startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQ_QRCODE);
        }
    }

    public static final int REQ_QRCODE = 0x1111;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQ_QRCODE);
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_QRCODE && data != null) {
            byte[] result = data.getByteArrayExtra(CaptureActivity.KEY_RESULT);
            if (result == null || result.length == 0) return;
            String payCode = new String(result);
            bd.tvReuslt.setText(payCode);
        }
    }


}
