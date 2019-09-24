package example.customView;

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.style.base.activity.BaseTitleBarActivity
import com.style.framework.R
import com.style.service.suspendWindow.CallInSuspendService
import com.style.service.suspendWindow.VideoSuspendService
import com.style.service.suspendWindow.VoiceSuspendService
import kotlinx.android.synthetic.main.activity_suspend_window.*

/**
 * Created by xiajun on 2017/8/1.
 */
class SuspendWindowActivity : BaseTitleBarActivity() {

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_suspend_window)
        requestDrawOverLays()
        btn_voice_suspend.setOnClickListener {
            openService(VoiceSuspendService::class.java);
            //sendBroadcast(Intent(Constants.ACTION_CALL_TIME_UPDATE));
        }
        btn_video_suspend.setOnClickListener {
            openService(VideoSuspendService::class.java);
        }
        btn_incoming_calling.setOnClickListener {
            openService(CallInSuspendService::class.java);
        }
    }

    private fun openService(cls: Class<*>) {
        startService(Intent(this, cls));
    }

    companion object {
        const val OVERLAY_PERMISSION_REQ_CODE = 1234;
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestDrawOverLays() {
        //这种权限需要手动去设置里面修改
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "can not DrawOverlays", Toast.LENGTH_SHORT).show();
            var intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
                Toast.makeText(this, "Permission Denieddd by user.Please Check it in Settings", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Allowed", Toast.LENGTH_SHORT).show();
                // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
            }
        }
    }
}
