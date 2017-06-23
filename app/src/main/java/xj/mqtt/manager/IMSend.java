package xj.mqtt.manager;

import com.style.manager.AccountManager;

import xj.mqtt.bean.IMMessage;
import xj.mqtt.bean.MsgType;
import xj.mqtt.bean.TextMsg;
import xj.mqtt.db.MsgDBManager;
import xj.mqtt.util.MessageUtil;

/**
 * Created by xiajun on 2017/6/21.
 */

public class IMSend {

    public static void sendText(String otherId, String content){
        IMMessage message = new IMMessage();
        message.setType(MsgType.MSG_TYPE_TEXT);
        TextMsg textMsg = new TextMsg();
        textMsg.content = content;
        message.setBody(textMsg.toJson());
        sendMessage(otherId, message);
    }

    private static void sendMessage(String otherId, IMMessage message) {
        message.setSenderId(String.valueOf(AccountManager.getInstance().getCurrentUser().getUserId()));
        message.setReceiverId(String.valueOf(otherId));
        message.setMsgId(MessageUtil.newMessageId());
        message.setState(IMMessage.State.SEND_ING.value);
        message.setCreateTime(System.currentTimeMillis());
        //存数据库
        MsgDBManager.getInstance().insert(message);
        boolean success = IMManagerImpl.getInstance().sendMessage(message);
        //发送失败
        if (!success){
            message.setState(IMMessage.State.SEND_FAILED.value);
            MsgDBManager.getInstance().update2SendFailed(message.getMsgId());
        }

    }
}
