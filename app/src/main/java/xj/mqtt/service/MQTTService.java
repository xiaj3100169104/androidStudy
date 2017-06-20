package xj.mqtt.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import xj.mqtt.MQTTConfig;
import xj.mqtt.listener.IMMessageListener;
import xj.mqtt.manager.IMManagerImpl;

/**
 * MQTT长连接服务
 */
public class MQTTService extends Service {

    public static final String TAG = MQTTService.class.getSimpleName();

    public static final String ACTION_LOGIN = "login";

    @Override
    public void onCreate() {
        super.onCreate();
        IMManagerImpl.getInstance().init(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleAction(intent);
        return START_STICKY;
    }

    /**
     * 处理各种action
     *
     * @param intent
     */
    private void handleAction(Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) return;
        if (ACTION_LOGIN.equals(action)) {
            IMManagerImpl.getInstance().connect();
        }
    }

    @Override
    public void onDestroy() {
        IMManagerImpl.getInstance().disConnect();
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}