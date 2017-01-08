package com.style.db.custom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.style.bean.Friend;
import com.style.bean.User;
import com.style.db.base.CommonSQLOpenHelper;
import com.style.db.base.FriendTable;
import com.style.db.base.UserTable;

import java.util.ArrayList;
import java.util.List;

public class UserDBManager {
    private static final String TAG = "DBManager";
    public static final String DB_NAME_USER_RELATIVE = "userRelative.db";
    public static final int DB_VERSION_USER_RELATIVE = 9;//降版本会报错
    public static final String TABLE_NAME_USER = "user";
    public static final String TABLE_NAME_FRIEND = "friend";
    public static final String[] TABLE_IGNORE_USER = {"id", "password", "signKey"};
    public static final String[] TABLE_IGNORE_FRIEND = {"id"};


    private UserRelativeSQLiteHelper dbHelper;
    private static UserDBManager mInstance;
    private SQLiteDatabase db;
    private Context mContext;

    //避免同时获取多个实例
    public synchronized static UserDBManager getInstance() {
        if (mInstance == null) {
            mInstance = new UserDBManager();
        }
        return mInstance;
    }

    public void initialize(Context context) {
        mContext = context;
        dbHelper = new UserRelativeSQLiteHelper(context, DB_NAME_USER_RELATIVE, DB_VERSION_USER_RELATIVE);
        db = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        if (!db.isOpen())
            db = dbHelper.getWritableDatabase();
        return db;
    }

    public String getTableNameUser() {
        return TABLE_NAME_USER;
    }

    public String getTableNameFriend() {
        return TABLE_NAME_FRIEND;
    }

    public boolean isUserExist(long userId) {
        String sql = "SELECT * FROM " + getTableNameUser() + " WHERE " + UserTable.COL_USERID + "=?";
        String[] params = new String[]{String.valueOf(userId)};
        Cursor c = getWritableDatabase().rawQuery(sql, params);
        boolean isExist = (c.getCount() != 0);
        c.close();
        return isExist;
    }

    public void insertOrUpdateUser(User bean) {
        deleteUser(bean.getUserId());
        insertUser(bean);
    }

    public void insertUser(User bean) {
        //String INSERT_DATA = "INSERT INTO table1 (_id, num, data) values (?, ?, ?)";
        String sql = DBUtils.getInsertSql(getTableNameUser(), bean.getClass(), TABLE_IGNORE_USER);
        Object[] params = DBUtils.getInsertParams(bean, TABLE_IGNORE_USER);
        getWritableDatabase().execSQL(sql, params);//execSQL为解决特殊字符插入异常，为升级版
    }

    public void insertUserList(List<User> list) {
        for (User bean : list)
            insertUser(bean);
    }

    public void updateUserSex(long userId, String sex) {
        String sql = "UPDATE " + getTableNameUser() + " SET " + UserTable.COL_SEX + "=?" + " WHERE " + UserTable.COL_USERID + "=?";
        Object[] params = new Object[]{sex, userId};
        getWritableDatabase().execSQL(sql, params);
    }

    public void deleteUser(long userId) {
        String sql = "DELETE FROM " + getTableNameUser() + " WHERE " + UserTable.COL_USERID + "=?";
        Object[] params = new Object[]{userId};
        getWritableDatabase().execSQL(sql, params);
    }

    public List<User> queryAllUser() {
        String sql = "SELECT * FROM " + getTableNameUser();
        List<User> result = DBUtils.rawQuery(getWritableDatabase(), sql, null, User.class, TABLE_IGNORE_USER);
        return result;
    }

    public List<Friend> queryAllFriend() {
        String sql = "SELECT * FROM " + getTableNameFriend();
        List<Friend> result = DBUtils.rawQuery(getWritableDatabase(), sql, null, Friend.class, TABLE_IGNORE_FRIEND);
        return result;
    }
    public User queryUser(long userId) {
        String sql = "SELECT * FROM " + getTableNameUser() + " WHERE " + UserTable.COL_USERID + "=?";
        String[] params = new String[]{String.valueOf(userId)};
        List<User> result = DBUtils.rawQuery(getWritableDatabase(), sql, params, User.class, TABLE_IGNORE_USER);
        if (result != null && result.size() > 0)
            return result.get(0);
        else
            return null;
    }

    public void insertFriendUser(Friend bean) {
        String sql = DBUtils.getInsertSql(getTableNameFriend(), bean.getClass(), TABLE_IGNORE_FRIEND);
        Object[] params = DBUtils.getInsertParams(bean, TABLE_IGNORE_FRIEND);
        getWritableDatabase().execSQL(sql, params);//execSQL为解决特殊字符插入异常，为升级版
    }

    public void insertFriendList(List<Friend> list) {
        for (Friend bean : list)
            insertFriendUser(bean);
    }

    public List<Friend> queryAllFriend(long userId) {
        String sql = "SELECT * FROM " + getTableNameFriend() + " WHERE " + FriendTable.COL_OWNER_ID + "=?";
        String[] params = new String[]{String.valueOf(userId)};
        List<Friend> result = DBUtils.rawQuery(getWritableDatabase(), sql, params, Friend.class, TABLE_IGNORE_FRIEND);
        return result;
    }

    public List<User> queryAllFriendUser(long userId) {
        String sql = "SELECT * FROM user left outer join friend on user.userId=friend.ownerId where user.userId=?";// + UserTable.COL_USERID + "=?";
        String[] params = new String[]{String.valueOf(userId)};
        List<User> result = DBUtils.rawQuery(getWritableDatabase(), sql, params, User.class, TABLE_IGNORE_USER);
        return result;
    }

    public List<User> queryCustomerInAll(String userId) {
        String sql = "SELECT * FROM " + getTableNameUser() + " WHERE " + UserTable.COL_USERID + "=?";
        String[] params = new String[]{userId};
        Cursor c = getWritableDatabase().rawQuery(sql, params);
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
        getWritableDatabase().beginTransaction();  //开始事务
        try {
            //转账

            //收账

            getWritableDatabase().setTransactionSuccessful();  //设置事务成功完成
        } finally {
            getWritableDatabase().endTransaction();    //结束事务
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
    public List<User> queryChatMsg(String customId, int startIndex, int count) {
        String sql = "SELECT * FROM " + getTableNameUser() + " WHERE " + UserTable.COL_USERID + "=? ORDER BY "
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

    /**
     * 关闭数据库，操作完成后必须调用
     */
    public void closeDB() {
        db.close();
    }

}
