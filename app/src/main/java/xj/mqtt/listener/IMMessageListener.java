package xj.mqtt.listener;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import xj.mqtt.service.MQTTService;

/**
 * Created by xiajun on 2017/6/19.
 */

public class IMMessageListener implements MqttCallback {
    public static final String TAG = IMMessageListener.class.getSimpleName();

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        String str1 = new String(message.getPayload());
        //EventBus.getDefault().post(msg);
        String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
        Log.e(TAG, "messageArrived:" + str1);
        Log.i(TAG, str2);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {

        try {
            Log.e(TAG, "deliveryComplete:" + arg0.getMessage().toString());
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void connectionLost(Throwable arg0) {
        // 失去连接，重连
        Log.e(TAG, "connectionLost" + arg0.getMessage());
    }
}
