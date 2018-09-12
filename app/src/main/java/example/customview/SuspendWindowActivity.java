package example.customview;

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

import example.customview.service.CallInSuspendService;
import example.customview.service.Constants;
import example.customview.service.VideoSuspendService;
import example.customview.service.VoiceSuspendService;

/**
 * Created by xiajun on 2017/8/1.
 */

public class SuspendWindowActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_suspend_window);
        openService(VoiceSuspendService.class);
    }

    public void test(View v){
        switch (v.getId()){
            case R.id.btn1:
                //openService(VoiceSuspendService.class);
                sendBroadcast(new Intent(Constants.ACTION_CALL_TIME_UPDATE));
                break;
            case R.id.btn2:
                openService(VideoSuspendService.class);
                break;
            case R.id.btn3:
                openService(CallInSuspendService.class);
                break;
        }
    }
    private void openService(Class cls){
        startService(new Intent(this, cls));
    }

    //参考自http://stackoverflow.com/questions/32061934/permission-from-manifest-doesnt-work-in-android-6
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @TargetApi(Build.VERSION_CODES.M)
    public void requestDrawOverLays() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "can not DrawOverlays", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
