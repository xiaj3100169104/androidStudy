package example.im;

import com.style.bean.Friend;

import java.io.Serializable;

import xj.mqtt.bean.IMMessage;

/**
 * Created by xiajun on 2017/1/12.
 */

public class MsgItem implements Serializable{
    private Friend friend;
    private IMMessage msg;
    private int unreadCount;

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public IMMessage getMsg() {
        return msg;
    }

    public void setMsg(IMMessage msg) {
        this.msg = msg;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
