package com.style.bean;

import java.io.Serializable;

/**
 * Created by xiajun on 2017/1/9.
 */

public class IMsg implements Serializable{
    private long id;
    private long msgId;
    private long senderId;
    private long receiverId;
    private String content;
    private long createTime;
    private int state;//0：未读；1：已读

    public IMsg() {
    }

    public IMsg(long id, long msgId, long senderId, long receiverId, String content, long createTime, int state) {
        this.id = id;
        this.msgId = msgId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.createTime = createTime;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "IMsg{" +
                "id=" + id +
                ", msgId=" + msgId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", state=" + state +
                '}';
    }
}
