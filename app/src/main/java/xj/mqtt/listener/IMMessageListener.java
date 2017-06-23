package xj.mqtt.listener;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import xj.mqtt.manager.HandleMessageManager;

/**
 * Created by xiajun on 2017/6/19.
 */

public class IMMessageListener implements MqttCallback {
    public static final String TAG = IMMessageListener.class.getSimpleName();

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
        Log.i(TAG, str2);
        String s = message.toString();
        Log.e(TAG, "messageArrived:" + s);
        HandleMessageManager.getInstance().handleNewMessage(s);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        try {
            MqttMessage message = arg0.getMessage();
            String s = message.toString();
            Log.e(TAG, "deliveryComplete:" + s);
            HandleMessageManager.getInstance().handleMessageSucceed(s);

        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void connectionLost(Throwable arg0) {
        //直接关闭网络开关,不用重连
        if (arg0 == null)
            Log.e(TAG, "connectionLost=直接关闭网络开关");
        else {            // 服务器主动断开连接，重连
            Log.e(TAG, "connectionLost" + arg0.getMessage());

        }
    }
}

