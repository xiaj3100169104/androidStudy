package com.style.db.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.style.bean.IMsg;
import com.style.bean.User;
import com.style.db.custom.SQLiteHelperListener;

public class MsgDBManager {
    private static final String TAG = "MsgDBManager";
    public static final String DB_NAME_MSG_RELATIVE = "message.db";
    public static final int DB_VERSION_MSG_RELATIVE = 3;//降版本会报错

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
            db.execSQL("DROP TABLE IF EXISTS msg");

            db.execSQL(getCreateTableMsgSql());
        }

        private String getCreateTableMsgSql() {
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("CREATE TABLE msg (");
            sBuffer.append("id").append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
            sBuffer.append("msgId").append(" LONG,");
            sBuffer.append("senderId").append(" LONG,");
            sBuffer.append("receiverId").append(" LONG,");
            sBuffer.append("content").append(" text,");
            sBuffer.append("createTime").append(" LONG,");
            sBuffer.append("state").append(" int)");
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

    public boolean isUserExist(String userId) {

        boolean isExistCustomer = true;
        return isExistCustomer;
    }

    public void insertMsg(IMsg o) {
        //设置创建时间
        o.setCreateTime(System.currentTimeMillis());
        //这样同样可以使用execSQL方法来执行一条“插入”的SQL语句，代码如下：
        String sql = "INSERT INTO msg (msgId, senderId, receiverId, content, createTime, state) values (?, ?, ?, ?, ?, ?)";
        Object[] bindArgs = new Object[]{o.getMsgId(), o.getSenderId(), o.getReceiverId(), o.getContent(), o.getCreateTime(), o.getState()};
        getWritableDatabase().execSQL(sql, bindArgs);
        Log.e(TAG, o.toString());
    }

    public void updateMsgState2Readed(IMsg o) {
        String sql = "UPDATE msg SET state=? WHERE id=?";
        Object[] bindArgs = new Object[]{o.getState(), o.getId()};
        getWritableDatabase().execSQL(sql, bindArgs);
    }

    public List<IMsg> queryAllMsg() {
        String sql = "SELECT * FROM msg";
        List<IMsg> result = queryMsg(sql, null);
        return result;
    }

    public void deleteOneFriendMsg(long senderId, long receiverId) {
        //注意包括我发送的我接收的
        String sql = "DELETE FROM msg WHERE (senderId=? AND receiverId=?) OR (senderId=? AND receiverId=?)";
        Object[] bindArgs = new Object[]{senderId, receiverId, receiverId, senderId};
        getWritableDatabase().execSQL(sql, bindArgs);
    }

    public List<IMsg> queryOneFriendMsg(long userId, long friendId) {
        String sql = "SELECT * FROM msg WHERE (senderId=? AND receiverId=?) OR (senderId=? AND receiverId=?)";
        String[] selectionArgs = new String[]{String.valueOf(userId), String.valueOf(friendId), String.valueOf(friendId), String.valueOf(userId)};
        List<IMsg> result = queryMsg(sql, selectionArgs);
        return result;
    }

    /**
     * 查询聊天记录 分页
     * PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
     *
     * @param friendId
     * @param startIndex
     * @return
     */
    public List<IMsg> queryFriendMsgByPage(long userId, String friendId, int startIndex, int count) {
        String sql = "SELECT * FROM msg WHERE (senderId=? AND receiverId=?) OR (senderId=? AND receiverId=?) ORDER BY createTime DESC";
        String[] selectionArgs = new String[]{String.valueOf(userId), String.valueOf(friendId), String.valueOf(friendId), String.valueOf(userId)};
        sql = prepareSql(sql, startIndex, count);
        List<IMsg> result = queryMsg(sql, selectionArgs);
        return result;
    }

    private List<IMsg> queryMsg(String sql, String[] selectionArgs) {
        List<IMsg> result = null;
        Cursor c = getWritableDatabase().rawQuery(sql, selectionArgs);
        if (c != null && c.getCount() > 0) {
            result = new ArrayList<>();
            while (c.moveToNext()) {
                IMsg msg = getMsgFromCursor(c);
                Log.e(TAG, msg.toString());
                result.add(msg);
            }
        }
        c.close();
        return result;
    }

    private IMsg getMsgFromCursor(Cursor c) {
        IMsg o = new IMsg();
        o.setId(c.getLong(c.getColumnIndex("id")));
        o.setMsgId(c.getLong(c.getColumnIndex("msgId")));
        o.setSenderId(c.getLong(c.getColumnIndex("senderId")));
        o.setReceiverId(c.getLong(c.getColumnIndex("receiverId")));
        o.setContent(c.getString(c.getColumnIndex("content")));
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

    public void addCustomer(User customer) {
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
