package example.im;

import com.style.bean.Friend;
import com.style.bean.IMsg;

import java.io.Serializable;

/**
 * Created by xiajun on 2017/1/12.
 */

public class MsgItem implements Serializable{
    private Friend friend;
    private IMsg msg;
    private int unreadCount;

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public IMsg getMsg() {
        return msg;
    }

    public void setMsg(IMsg msg) {
        this.msg = msg;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
