package example.activity;

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.activity.CaptureActivity
import com.google.zxing.encoding.EncodingHandler
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityQrCodeScanBinding

class QRCodeActivity : BaseTitleBarActivity() {
    private lateinit var bd: ActivityQrCodeScanBinding
    val url = "https://github.com/xj913492952"

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = ActivityQrCodeScanBinding.inflate(layoutInflater)
        setContentView(bd.root)
        init()
        bd.btnScaner.setOnClickListener {
            checkCameraPermission()
            //startActivity(Intent(getContext(), MainActivity::class.java))
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logE(TAG, "onNewIntent")
    }

    private fun init() {
        bd.image1.setImageBitmap(EncodingHandler.createQRCode(url, 500))
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.k)
        bd.image2.setImageBitmap(EncodingHandler.createQRCode(url, 500, 500, bitmap))
    }

    private fun checkCameraPermission() {
        //第二个参数是需要申请的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 2)
        } else {
            //权限已经被授予，在这里直接写要执行的相应方法即可
            startActivityForResult(Intent(getContext(), CaptureActivity::class.java), REQ_QRCODE)
        }
    }

    companion object {
        private const val REQ_CAMERA_PERMISSION = 2
        private const val REQ_QRCODE = 17
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(Intent(getContext(), CaptureActivity::class.java), REQ_QRCODE)
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQ_QRCODE && data != null) {
            val result = data.getByteArrayExtra(CaptureActivity.KEY_RESULT)
            if (result == null || result.isEmpty())
                return
            val payCode = String(result)
            bd.tvReuslt.text = payCode
        }
    }


}
