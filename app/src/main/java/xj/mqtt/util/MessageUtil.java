package xj.mqtt.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicLong;

import xj.mqtt.bean.IMMessage;
import xj.mqtt.util.StringUtil;

/**
 * Created by xiajun on 2017/6/20.
 */

public class MessageUtil {
    /**
     * A prefix helps to make sure that ID's are unique across multiple instances.
     */
    private static final String PREFIX = StringUtil.randomString(5) + "-";

    /**
     * Keeps track of the current increment, which is appended to the prefix to
     * forum a unique ID.
     */
    private static final AtomicLong ID = new AtomicLong();

    public static String newMessageId() {
        return PREFIX + Long.toString(ID.incrementAndGet());
    }


    public static String toJson(IMMessage m) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgId", m.msgId);
        jsonObject.put("type", m.type);
        jsonObject.put("senderId", m.senderId);
        jsonObject.put("receiverId", m.receiverId);
        jsonObject.put("state", m.state);
        jsonObject.put("body", m.body);
        jsonObject.put("createTime", m.createTime);
        return jsonObject.toString();
    }

    public static IMMessage toObject(String json) throws JSONException{
        JSONObject o = new JSONObject(json);
        IMMessage m = new IMMessage();
        m.setMsgId(o.optString("msgId"));
        m.setType(o.optInt("type"));
        m.setSenderId(o.optString("senderId"));
        m.setReceiverId(o.optString("receiverId"));
        m.setBody(o.optString("body"));
        m.setCreateTime(o.optLong("createTime"));
        m.setState(o.optInt("state"));
        return m;
    }
}
