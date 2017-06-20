package xj.mqtt.manager;

import android.content.Context;

import xj.mqtt.bean.IMMessage;

/**
 * Created by xiajun on 2017/6/20.
 */

public interface IMManager {
    void init(Context context);
    boolean connect();
    void disConnect();
    boolean isConnected();
    boolean sendMessage(IMMessage messageBean);
}
