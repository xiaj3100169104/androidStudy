package com.style.db.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.style.bean.User;

public class CommonDBManager {
    private static final String TAG = "DBManager";

    private CommonSQLOpenHelper dbHelper;
    private static CommonDBManager commonDBManager;
    private SQLiteDatabase db;
    private Context mContext;

    public synchronized static CommonDBManager getInstance() {
        if (commonDBManager == null) {
            commonDBManager = new CommonDBManager();
        }
        return commonDBManager;
    }

    public void init(Context context) {
        mContext = context;
        dbHelper = new CommonSQLOpenHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean isUserExist(String userId) {
        String sql = "SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.COL_USERID + "=?";
        String[] params = new String[]{userId};
        Cursor c = db.rawQuery(sql, params);
        boolean isExistCustomer = (c.getCount() != 0);
        c.close();
        return isExistCustomer;
    }

    public long addUser(User bean) {
        ContentValues cv = new ContentValues();
        cv.put(UserTable.COL_USERID, bean.getUserId());
        cv.put(UserTable.COL_ACCOUNT, bean.getAccount());
        cv.put(UserTable.COL_TELEPHONE, bean.getTelPhone());
        cv.put(UserTable.COL_USERNAME, bean.getUserName());
        return db.insert(UserTable.TABLE_NAME, null, cv);
    }

    public void insertUser(User bean) {
        //这样同样可以使用execSQL方法来执行一条“插入”的SQL语句，代码如下：
        String  INSERT_DATA = "INSERT INTO table1 (_id, num, data) values (1, 1, '通过SQL语句插入')" ;
        db.execSQL(INSERT_DATA);
    }

    public void updateUser(String userId, String avatarPath) {
        String sql = "UPDATE " + UserTable.TABLE_NAME + " SET " + UserTable.COL_AVATAR + "=?"+" WHERE " + UserTable.COL_USERID + "=?";
        String[] params = new String[]{userId, avatarPath};
        db.execSQL(sql, params);
    }

    public void deleteUser(String userId, String phone) {
        String sql = "DELETE FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.COL_USERID + "=? AND " + UserTable.COL_TELEPHONE + "=?";
        String[] params = new String[]{userId, phone};
        db.execSQL(sql, params);
    }

    public List<User> queryCustomerInAll(String userId) {
        String sql = "SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.COL_USERID + "=?";
        String[] params = new String[]{userId};
        Cursor c = db.rawQuery(sql, params);
        List<User> result = new ArrayList<>();
        while (c.moveToNext()) {
            User customer = new User();
            customer.setAccount(c.getString(c.getColumnIndex(UserTable.COL_ACCOUNT)));
            customer.setUserId(c.getLong(c.getColumnIndex(UserTable.COL_USERID)));
            result.add(customer);
        }
        c.close();
        return result;
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
     * 查询聊天记录 分页
     * PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
     *
     * @param customId
     * @param startIndex
     * @return
     */
    public List<User> queryChatMsg(String customId,  int startIndex,int count) {
        String sql = "SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.COL_USERID + "=? ORDER BY "
                + UserTable.TIME + " DESC";
        String[] params = new String[]{customId};
        Cursor c = db.rawQuery(prepareSql(sql, startIndex, count), params);
        List<User> result = new ArrayList<>();
        while (c.moveToNext()) {
            User msg = new User();
            msg.setUserId(c.getLong(c.getColumnIndex(UserTable.COL_USERID)));
            msg.setAccount(c.getString(c.getColumnIndex(UserTable.COL_ACCOUNT)));
            result.add(msg);
        }
        c.close();
        return result;
    }
    /**
     * 给查询语句加上分页的代码
     * @param sql
     * @param startIndex
     * @return
     */
    private String prepareSql(String sql, int startIndex,int count) {
        StringBuffer sqlBuf = new StringBuffer(50 + sql.length());
        sqlBuf.append(sql);
        sqlBuf.append(" limit ");
        sqlBuf.append(startIndex);
        sqlBuf.append(",");
        sqlBuf.append(count);
        return sqlBuf.toString();
    }

    /**
     * 关闭数据库，操作完成后必须调用
     */
    public void closeDB() {
        db.close();
    }

}
