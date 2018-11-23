package example.customView.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.style.framework.R;


public class VoiceSuspendService extends Service {

    //定义浮动窗口布局  
    RelativeLayout mFloatLayout;
    LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象  
    WindowManager mWindowManager;

    Button mFloatView;

    private static final long ONCLICK_TIME = 100;//触摸100ms内认为是点击
    private static final long ONCLICK_DISTANCE = 275;//触摸距离小于275px认为是点击

    int statuaBarhight;

    private float mDownRawX;
    private float mDownRawY;
    private float rawX;
    private float rawY;
    private long downTime;
    private long upTime;
    private CallReceiver broadCast;
    private float mDownX;
    private float mDownY;
    private boolean isAdded;

    @Override
    public void onCreate() {
        statuaBarhight = (int) getResources().getDimension(R.dimen.status_bar_height);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CALL_TIME_UPDATE);
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
        //设置window type;Unable to add window -- token null is not for an application错误
        wmParams.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明  
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）  
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶  
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity  
        wmParams.x = 0;
        wmParams.y = 200;

        //设置悬浮窗口长宽数据    
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

         /*// 设置悬浮窗口长宽数据 
        wmParams.width = 200; 
        wmParams.height = 80;*/

        final LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局  
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.layout_suspend_voice, null);
        //添加mFloatLayout  
        //mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮  
        mFloatView = (Button) mFloatLayout.findViewById(R.id.btn_float);
        mFloatView.setText("00");
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //设置监听浮动窗口的触摸移动  
        mFloatView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rawX = event.getRawX();
                rawY = event.getRawY();
                int xabs = (int) Math.abs(rawX - mDownRawX);
                int yabs = (int) Math.abs(rawY - mDownRawY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                        // 获取相对View的坐标，即以此View左上角为原点
                        downTime = System.currentTimeMillis();
                        mDownRawX = event.getRawX();
                        mDownRawY = event.getRawY();
                        mDownX = event.getX();
                        mDownY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
                        updateViewPosition();
                        break;
                    case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
                        upTime = System.currentTimeMillis();
                        long value = upTime - downTime;
                        if (value > 0 && value < ONCLICK_TIME && xabs <= ONCLICK_DISTANCE && yabs <= ONCLICK_DISTANCE) {

                            stopSelf();
                        }
                        break;
                }
                return true;
            }

            private void updateViewPosition() {
                // 更新浮动窗口位置参数 
                wmParams.x = (int) (rawX - mDownX);
                wmParams.y = (int) (rawY - statuaBarhight - mDownY);
                mWindowManager.updateViewLayout(mFloatLayout, wmParams); // 刷新显示
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

    private void updateCallingTime() {
        if (isAdded) {
            mWindowManager.removeView(mFloatLayout);
            isAdded = false;
        }
        mWindowManager.addView(mFloatLayout, wmParams);
        isAdded = true;
    }

    class CallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ACTION_CALL_HANGUP)) {
                stopSelf();
            } else if (action.equals(Constants.ACTION_CALL_TIME_UPDATE)) {
                updateCallingTime();
            }
        }
    }
}