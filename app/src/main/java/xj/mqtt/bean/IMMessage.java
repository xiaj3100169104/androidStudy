package xj.mqtt.bean;

import org.json.JSONException;
import org.json.JSONObject;


/*******************************************************
 * Created by 夏军 on 2017/6/20
 *******************************************************/

public class IMMessage {

    private int id; //主键，子增长
    private String messageId; //每一条消息在网络上发送时都表现为一个消息包，这个packetId与每条消息的消息Id是一致的
    private String toUserId; //自己;
    private String fromUserId; //对方;
    private String msg; //对应表中msg字段
    private int type; //消息类型
    private long date; //发送消息时间
    private int direction; // 发出去还是收到了消息
    private int state; //判断消息是否已发送或者已读
    private Msg msgObj; //msg字段对应的对象

    public IMMessage() {
    }


    public String toSendJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", getMsgObj().toSendJsonObject());
        jsonObject.put("type", type);
        return jsonObject.toString();
    }

    public enum State {
        NEW(0), SEND_SUCCESS(1), SEND_FAILED(2), READ(3);

        public int value;

        State(int value) {
            this.value = value;
        }
    }

    public enum Direction {
        INCOMING(0), OUTGOING(1);
        public int value;

        Direction(int value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "[id: " + id + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setMsgObj(Msg msgObj) {
        this.msgObj = msgObj;
    }

    public String getMsg() {
        return msg;
    }

    //应先设置消息类型，不然会有强制类型转换报错，除非不加类型转换
    public void setMsg(String msg) {
        this.msg = msg;
        //msgObj = Msg.fromJson(msg, type);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTypeDesc() {
        return Msg.getTypeDesc(type, getMsgObj());
    }

    public Msg getMsgObj() {
        if (msgObj == null) {
            msgObj = Msg.fromJson(msg, type);
        }
        return msgObj;
    }
}
