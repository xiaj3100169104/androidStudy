package xj.mqtt.bean;

import org.json.JSONException;
import org.json.JSONObject;


/*******************************************************
 * Created by 夏军 on 2017/6/20
 *******************************************************/

public class IMMessage {

    private long id; //主键，子增长
    private String msgId; //为了更新自己发送的消息，因为插入数据库时取不到id
    private int type; //消息类型
    private String senderId; //发送方;
    private String receiverId; //接收方;
    private String body; //对应表中msg字段
    private int state; //判断消息是否已发送或者已读
    private long createTime; //创建时间

    private BaseMsg bodyObj; //msg字段对应的对象

    public IMMessage() {
    }

    public String toSendJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgId", msgId);
        jsonObject.put("type", type);
        jsonObject.put("senderId", senderId);
        jsonObject.put("receiverId", receiverId);

        jsonObject.put("state", state);
        jsonObject.put("body", getBodyObj().toSendJsonObject());
        jsonObject.put("createTime", createTime);
        return jsonObject.toString();
    }

    public enum State {
        NEW(0), SEND_SUCCESS(1), SEND_FAILED(2), READ(3);

        public int value;

        State(int value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "[id: " + id + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BaseMsg getBodyObj() {
        if (bodyObj == null) {
            bodyObj = BaseMsg.fromJson(body, type);
        }
        return bodyObj;    }

    public void setBodyObj(BaseMsg bodyObj) {
        this.bodyObj = bodyObj;
    }

    public String getTypeDesc() {
        return BaseMsg.getTypeDesc(type, getBodyObj());
    }

}
