package xj.mqtt.manager;

import android.content.Context;

import com.style.bean.User;
import com.style.manager.AccountManager;

import org.json.JSONException;
import org.json.JSONObject;

import xj.mqtt.bean.IMMessage;
import xj.mqtt.db.MsgDBManager;
import xj.mqtt.util.MessageUtil;

/**
 * Created by xiajun on 2017/6/23.
 */

public class HandleMessageManager {

    private static HandleMessageManager instance = new HandleMessageManager();
    private Context context;
    MsgDBManager dbManager;
    User curUser;

    public static synchronized HandleMessageManager getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        dbManager = MsgDBManager.getInstance();
        curUser = AccountManager.getInstance().getCurrentUser();
    }

    /**
     * 处理新消息，不包括自己发送的
     * @param json
     */
    public void handleNewMessage(String json) {
        handleMessage(json);
    }

    /**
     * 处理自己发送成功的消息
     * @param json
     */
    public void handleMessageSucceed(String json) {
        handleMessage(json);
    }
    public void handleMessage(String json) {
        try {
            IMMessage m = MessageUtil.toObject(json);
            if (isMsgExist(m))
                return;

            //接受者是我的消息，置为未读
            if (m.getReceiverId().equals(curUser.getUserId()))
                m.setState(IMMessage.State.UNREAD.value);
            saveMsg(m);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected boolean isMsgExist(IMMessage m) {
        boolean isExist = dbManager.getMsg(m.getMsgId()) != null;
        return isExist;
    }

    protected void saveMsg(IMMessage m) {
        dbManager.insert(m);
    }

}
