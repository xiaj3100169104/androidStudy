package xj.mqtt.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 王者 on 2016/8/3.
 */
public abstract class Msg {

    public String toJson(){
        return JSON.toJSONString(this);
    }

    public abstract JSONObject toSendJsonObject()  throws JSONException;

    public static Msg fromJson(String json, int type){
        Msg msg = null;
        switch (type){
            case MsgType.MSG_TYPE_TEXT:
                msg = parseObject(json, TextMsg.class);
                break;
            case MsgType.MSG_TYPE_PICTURE:
                msg = parseObject(json, PictureMsg.class);
                break;
            case MsgType.MSG_TYPE_VOICE:
                msg = parseObject(json, VoiceMsg.class);
                break;
            case MsgType.MSG_TYPE_OFFLINE_VIDEO:
                msg = parseObject(json, OfflineVideoMsg.class);
                break;
            case MsgType.MSG_TYPE_SEND_INVITE:
                break;
            case MsgType.MSG_TYPE_REPLY_INVITE:
                msg = parseObject(json, InviteMsg.class);
                break;
            case MsgType.MSG_TYPE_LOCATION:
                msg = parseObject(json, LocationMsg.class);
                break;
            default:
                break;
        }
        return msg;
    }

    public static final <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    public static String getTypeDesc(int type, Msg msgObj){
        String msg = null;
        switch (type){
            case MsgType.MSG_TYPE_TEXT:
                TextMsg textMsg = (TextMsg) msgObj;
                msg = textMsg.content;
                break;
            case MsgType.MSG_TYPE_PICTURE:
                msg = MsgType.MSG_TYPE_DESC_PICTURE;
                break;
            case MsgType.MSG_TYPE_VOICE:
                msg = MsgType.MSG_TYPE_DESC_VOICE;
                break;
            case MsgType.MSG_TYPE_OFFLINE_VIDEO:
                msg = MsgType.MSG_TYPE_DESC_OFFLINE_VIDEO;
                break;
            case MsgType.MSG_TYPE_LOCATION:
                msg = MsgType.MSG_TYPE_DESC_LOCATION;
                break;
            default:
                msg = "";
                break;
        }
        return msg;
    }
}
