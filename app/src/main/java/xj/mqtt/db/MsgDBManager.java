package xj.mqtt.db;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.style.db.user.SQLiteHelperListener;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import xj.mqtt.bean.IMMessage;
import xj.mqtt.bean.MsgAction;
import xj.mqtt.util.ThreadPoolUtil;

public class MsgDBManager {
    private static final String TAG = "MsgDBManager";
    public static final String DB_NAME_MSG_RELATIVE = "im.db";
    public static final int DB_VERSION_MSG_RELATIVE = 8;////最低为1，降版本会报错

    private MsgDBHelperListener helperListener;
    private MsgSQLOpenHelper dbHelper;
    private static MsgDBManager msgDBManager;
    private SQLiteDatabase db;
    private Context mContext;

    public synchronized static MsgDBManager getInstance() {
        if (msgDBManager == null) {
            msgDBManager = new MsgDBManager();
        }
        return msgDBManager;
    }

    private class MsgDBHelperListener implements SQLiteHelperListener {

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(getCreateTableMsgSql());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS message");
            db.execSQL(getCreateTableMsgSql());
        }

        private String getCreateTableMsgSql() {
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("CREATE TABLE message (");
            sBuffer.append("id").append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
            sBuffer.append("msgId").append(" STRING,");
            sBuffer.append("type").append(" int,");
            sBuffer.append("senderId").append(" STRING,");
            sBuffer.append("receiverId").append(" STRING,");
            sBuffer.append("body").append(" text,");
            sBuffer.append("state").append(" int,");
            sBuffer.append("createTime").append(" LONG)");
            String sql = sBuffer.toString();
            return sql;
        }
    }

    public void init(Context context) {
        mContext = context;
        helperListener = new MsgDBHelperListener();
        dbHelper = new MsgSQLOpenHelper(mContext, DB_NAME_MSG_RELATIVE, DB_VERSION_MSG_RELATIVE, helperListener);
        db = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        if (!db.isOpen())
            db = dbHelper.getWritableDatabase();
        return db;
    }

    public void insert(final IMMessage o) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                //设置创建时间
                o.setCreateTime(System.currentTimeMillis());
                //这样同样可以使用execSQL方法来执行一条“插入”的SQL语句，代码如下：
                String sql = "INSERT INTO message (msgId, senderId, receiverId, type, body, createTime, state) values (?, ?, ?, ?, ?, ?, ?)";
                Object[] bindArgs = new Object[]{o.getMsgId(), o.getSenderId(), o.getReceiverId(), o.getType(), o.getBody(), o.getCreateTime(), o.getState()};
                getWritableDatabase().execSQL(sql, bindArgs);
                Log.e(TAG, "insert" + o.toString());
                EventBus.getDefault().post(o, MsgAction.MSG_NEW);
                //发送新消息广播
                //mContext.sendBroadcast(new Intent(MsgAction.MSG_NEW).putExtra("msg", o));
            }
        });
    }

    public void update2SendFailed(String msgId) {
        String sql = "UPDATE message SET state=? WHERE msgId=?";
        Object[] bindArgs = new Object[]{IMMessage.State.SEND_FAILED.value, msgId};
        getWritableDatabase().execSQL(sql, bindArgs);
        //发送消息更新广播
        IMMessage msg = getMsg(msgId);
        //mContext.sendBroadcast(new Intent(MsgAction.MSG_UPDATE).putExtra("msg", msg));
        EventBus.getDefault().post(msg, MsgAction.MSG_UPDATE);
    }

    public void update2Readed(String msgId) {
        String sql = "UPDATE message SET state=? WHERE msgId=?";
        Object[] bindArgs = new Object[]{IMMessage.State.READ.value, msgId};
        getWritableDatabase().execSQL(sql, bindArgs);
        //发送消息更新广播
        IMMessage msg = getMsg(msgId);
        //mContext.sendBroadcast(new Intent(MsgAction.MSG_UPDATE).putExtra("msg", msg));
        EventBus.getDefault().post(msg, MsgAction.MSG_UPDATE);
    }

    public void updateReaded2User(final String senderId, final String receiverId) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                String sql = "UPDATE message SET state=? WHERE senderId=? and receiverId=?";
                Object[] bindArgs = new Object[]{IMMessage.State.READ.value, senderId, receiverId};
                getWritableDatabase().execSQL(sql, bindArgs);
            }
        });
    }

    public IMMessage getMsg(String msgId) {
        String sql = "SELECT * FROM message where msgId=?";
        String[] selectionArgs = new String[]{msgId};
        List<IMMessage> result = queryMsg(sql, selectionArgs);
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else
            return null;
    }

    public List<IMMessage> getAllMsg() {
        String sql = "SELECT * FROM message";
        List<IMMessage> result = queryMsg(sql, null);
        return result;
    }

    public void deleteMsg(String senderId, String receiverId) {
        //注意包括我发送的我接收的
        String sql = "DELETE FROM message WHERE (senderId=? AND receiverId=?) OR (senderId=? AND receiverId=?)";
        String[] bindArgs = new String[]{senderId, receiverId, receiverId, senderId};
        getWritableDatabase().execSQL(sql, bindArgs);
    }

    public int getUnreadCount(String myselfId, String senderId) {
        int count = 0;
        String sql = "SELECT count(1) as count FROM message WHERE receiverId=? and senderId=? AND state=?";
        String[] selectionArgs = new String[]{myselfId, senderId, String.valueOf(IMMessage.State.UNREAD.value)};
        Cursor cursor = getWritableDatabase().rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }
        return count;
    }

    public int getMyUnreadAllCount(String myselfId) {
        int count = 0;
        String sql = "SELECT count(1) as count FROM message WHERE receiverId=? AND state=?";
        String[] selectionArgs = new String[]{myselfId, String.valueOf(IMMessage.State.UNREAD.value)};
        Cursor cursor = getWritableDatabase().rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }
        return count;
    }

    public List<IMMessage> getMsgOfFriend(String myselfId, String friendId) {
        String sql = "SELECT * FROM message WHERE (senderId=? AND receiverId=?) OR (senderId=? AND receiverId=?)";
        String[] selectionArgs = new String[]{myselfId, friendId, friendId, myselfId};
        List<IMMessage> result = queryMsg(sql, selectionArgs);
        return result;
    }

    /**
     * 查询聊天记录 分页
     * PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
     *
     * @param startIndex //注意这个索引是针对条件过滤后的结果中索引
     * @param friendId
     * @param startIndex
     * @return
     */
    public List<IMMessage> getMsgByPage(String myselfId, String friendId, int startIndex, int count) {
        String sql = "SELECT * FROM message WHERE (senderId=? AND receiverId=?) OR (senderId=? AND receiverId=?) ORDER BY createTime DESC limit ?,?";
        String[] selectionArgs = new String[]{myselfId, friendId, friendId, myselfId, startIndex + "", count + ""};
        //sql = prepareSql(sql, startIndex, count);
        List<IMMessage> result = queryMsg(sql, selectionArgs);
        return result;
    }

    public IMMessage getLastMessageOfFriend(String myselfId, String friendId) {
        String sql = "SELECT * FROM message WHERE (senderId=? AND receiverId=?) OR (senderId=? AND receiverId=?) ORDER BY createTime DESC limit ?,?";
        String[] selectionArgs = new String[]{myselfId, friendId, friendId, myselfId, "0", "1"};
        List<IMMessage> result = queryMsg(sql, selectionArgs);
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else
            return null;
    }

    private List<IMMessage> queryMsg(String sql, String[] selectionArgs) {
        List<IMMessage> result = null;
        Cursor c = getWritableDatabase().rawQuery(sql, selectionArgs);
        if (c != null && c.getCount() > 0) {
            result = new ArrayList<>();
            while (c.moveToNext()) {
                IMMessage msg = getMsgFromCursor(c);
                Log.e(TAG, "queryMsg" + msg.toString());
                result.add(msg);
            }
        }
        c.close();
        return result;
    }

    private IMMessage getMsgFromCursor(Cursor c) {
        IMMessage o = new IMMessage();
        o.setId(c.getLong(c.getColumnIndex("id")));
        o.setMsgId(c.getString(c.getColumnIndex("msgId")));
        o.setSenderId(c.getString(c.getColumnIndex("senderId")));
        o.setReceiverId(c.getString(c.getColumnIndex("receiverId")));
        o.setType(c.getInt(c.getColumnIndex("type")));
        o.setBody(c.getString(c.getColumnIndex("body")));
        o.setState(c.getInt(c.getColumnIndex("state")));
        o.setCreateTime(c.getLong(c.getColumnIndex("createTime")));
        return o;
    }

    /**
     * 给查询语句加上分页的代码
     *
     * @param sql
     * @param startIndex
     * @return
     */
    private String prepareSql(String sql, int startIndex, int count) {
        StringBuffer sqlBuf = new StringBuffer(50 + sql.length());
        sqlBuf.append(sql);
        sqlBuf.append(" limit ");
        sqlBuf.append(startIndex);
        sqlBuf.append(",");
        sqlBuf.append(count);
        return sqlBuf.toString();
    }

    public void addCustomer(IMMessage customer) {
        db.beginTransaction();  //开始事务
        try {
            //转账

            //收账

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * 关闭数据库，操作完成后必须调用
     */
    public void closeDB() {
        db.close();
    }

}
