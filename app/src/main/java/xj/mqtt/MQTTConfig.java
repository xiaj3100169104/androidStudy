package xj.mqtt;

/**
 * Created by xiajun on 2017/6/20.
 */

public class MQTTConfig {
    public static final String SERVICE_URI = "tcp://192.168.1.104:61613";
    public static final boolean CLEAN_SESSION = false;
    public static final int CONN_TIME_OUT = 10;
    public static final int KEEP_ALIVE_INTERVAL = 20;

    public static final String CHAT_SINGLE_PRE = "1";

    public static String getClientId(String userName) {
        return userName + "@mobile.client";
    }

    public static String getChatSingleTopic(String userName) {
        return CHAT_SINGLE_PRE + "/" + userName;
    }
}
