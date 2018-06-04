package example.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
}
