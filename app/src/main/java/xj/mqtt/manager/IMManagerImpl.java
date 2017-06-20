package xj.mqtt.manager;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.style.manager.AccountManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;

import xj.mqtt.MQTTConfig;
import xj.mqtt.bean.IMMessage;
import xj.mqtt.listener.IMMessageListener;
import xj.mqtt.util.IMNetUtil;
import xj.mqtt.util.MessageIdUtil;

/**
 * Created by xiajun on 2017/6/20.
 */

public class IMManagerImpl implements IMManager {
    public static final String TAG = IMManagerImpl.class.getSimpleName();

    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;
    private IMMessageListener messageListener;
    private String serverURI = MQTTConfig.SERVICE_URI;
    private String userName;
    private String passWord;
    private String clientId;

    private static IMManagerImpl mInstance;
    private Context context;
    private String topicSingleChat;


    public static synchronized IMManagerImpl getInstance() {
        if (mInstance == null) {
            mInstance = new IMManagerImpl();
        }
        return mInstance;
    }

    private IMManagerImpl() {
    }

    @Override
    public void init(Context context) {
        this.context = context;
        // 服务器地址（协议+地址+端口号）
        userName = String.valueOf(AccountManager.getInstance().getCurrentUser().getUserId());
        passWord = AccountManager.getInstance().getCurrentUser().getPassword();
        clientId = MQTTConfig.getClientId(userName);
        topicSingleChat = MQTTConfig.getChatSingleTopic(userName);

        client = new MqttAndroidClient(this.context, serverURI, clientId);
        // 设置MQTT监听并且接受消息
        messageListener = new IMMessageListener();
        client.setCallback(messageListener);

        conOpt = new MqttConnectOptions();
        // 清除缓存,cleansession=false的情况下会保存离线消息
        conOpt.setCleanSession(MQTTConfig.CLEAN_SESSION);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(MQTTConfig.CONN_TIME_OUT);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(MQTTConfig.KEEP_ALIVE_INTERVAL);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        conOpt.setPassword(passWord.toCharArray());

        //加密socket
        /*InputStream caInput = getResources().getAssets().open("keystore.bks");
        if(caInput!=null){
            Log.d(TAG,"do setSocketFactory");
            SSLSocketFactory sslSocketFactory = mMqttAndroidClient.getSSLSocketFactory(caInput,"password");
            options.setSocketFactory(sslSocketFactory);
        }*/
        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + clientId + "\"}";
        String topic = userName;
        Integer qos = 0;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱

            conOpt.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
        }

        if (doConnect) {
            connect();
        }

    }

    @Override
    public boolean connect() {
        if (!client.isConnected() && IMNetUtil.isNetworkAvailable(this.context)) {
            try {
                //连接前设置为自动重连
                conOpt.setAutomaticReconnect(true);
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                return false;
            }
        }
        return false;
    }

    @Override
    public void disConnect() {
        try {
            //主动断开连接，取消自动重连
            conOpt.setAutomaticReconnect(false);
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean sendMessage(IMMessage messageBean) {
        messageBean.setMessageId(MessageIdUtil.newMessageId());
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(0);
        mqttMessage.setRetained(false);
        try {
            mqttMessage.setPayload(messageBean.toSendJson().getBytes());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (client.isConnected())
                client.publish(topicSingleChat, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                // 订阅myTopic话题
                client.subscribe(topicSingleChat, 1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            Log.e(TAG, "onFailure");
            // 连接失败，重连
        }
    };

}
