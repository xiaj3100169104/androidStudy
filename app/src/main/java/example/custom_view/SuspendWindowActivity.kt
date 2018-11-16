package example.custom_view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.style.framework.R;

import example.custom_view.service.CallInSuspendService;
import example.custom_view.service.Constants;
import example.custom_view.service.VideoSuspendService;
import example.custom_view.service.VoiceSuspendService;

/**
 * Created by xiajun on 2017/8/1.
 */

public class SuspendWindowActivity : AppCompatActivity() {


    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_suspend_window);
        openService(VoiceSuspendService::class.java);
    }

    fun test(v: View) {
        when (v.getId()) {
            R.id.btn1 -> {
                //openService(VoiceSuspendService.class);
                sendBroadcast(Intent(Constants.ACTION_CALL_TIME_UPDATE));
            }
            R.id.btn2 -> {
                openService(VideoSuspendService::class.java);
            }
            R.id.btn3 -> {
                openService(CallInSuspendService::class.java);
            }
        }
    }

    fun openService(cls: Class<*>) {
        startService(Intent(this, cls));
    }

    companion object {
        //参考自http://stackoverflow.com/questions/32061934/permission-from-manifest-doesnt-work-in-android-6
        const val OVERLAY_PERMISSION_REQ_CODE = 1234;

    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestDrawOverLays() {
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
