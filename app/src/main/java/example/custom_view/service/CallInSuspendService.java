package example.custom_view.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.style.framework.R;

public class CallInSuspendService extends Service {

    //定义浮动窗口布局  
    private View mFloatLayout;
    private View mLeftLayout;
    private ImageView ivCallType;
    private TextView tvName;

    private Button btnHangup;
    private Button btnAnswer;

    LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象  
    WindowManager mWindowManager;

    int statuaBarhight;

    private CallReceiver broadCast;

    @Override
    public void onCreate() {
        statuaBarhight = (int) getResources().getDimension(R.dimen.status_bar_height);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CALL_HANGUP);
        broadCast = new CallReceiver();
        registerReceiver(broadCast, intentFilter);
        createFloatView();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub  
        return null;
    }

    private void createFloatView() {
        wmParams = new LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper  
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明  
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）  
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶  
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity  
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据    
        wmParams.width = LayoutParams.MATCH_PARENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

         /*// 设置悬浮窗口长宽数据 
        wmParams.width = 200; 
        wmParams.height = 80;*/

        final LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局  
        mFloatLayout = inflater.inflate(R.layout.layout_suspend_call_in, null);
        //添加mFloatLayout  
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮  
        mLeftLayout = mFloatLayout.findViewById(R.id.layout_customer);
        ivCallType = (ImageView) mFloatLayout.findViewById(R.id.iv_call_type);
        tvName = (TextView) mFloatLayout.findViewById(R.id.tv_name);
        btnHangup = (Button) mFloatLayout.findViewById(R.id.btn_hangup);
        btnAnswer = (Button) mFloatLayout.findViewById(R.id.btn_answer);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadCast);
        if (mFloatLayout != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
        super.onDestroy();
    }

    class CallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.ACTION_CALL_HANGUP:
                    stopSelf();
                    break;
            }
        }
    }
}