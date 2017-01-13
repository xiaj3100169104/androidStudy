package com.style.db.custom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.style.bean.Friend;
import com.style.bean.User;

import java.util.List;

public class UserDBManager {
    private static final String TAG = "UserDBManager";
    public static final String DB_NAME_USER_RELATIVE = "userRelative.db";
    public static final int DB_VERSION_USER_RELATIVE = 5;//降版本会报错

    public static final String[] TABLE_IGNORE_USER = {"id", "password", "signKey"};
    public static final String[] TABLE_IGNORE_FRIEND = {"user", "id"};
    private static final String SQL_CREATE_TABLE_USER = DBUtils.getCreateTableSql(User.class, TABLE_IGNORE_USER);
    private static final String SQL_CREATE_TABLE_FRIEND = DBUtils.getCreateTableSql(Friend.class, TABLE_IGNORE_FRIEND);
    private static final String SQL_DROP_TABLE_USER = DBUtils.getDropTableSql(User.class);
    private static final String SQL_DROP_TABLE_FRIEND = DBUtils.getDropTableSql(Friend.class);

    private UserRelativeSQLiteHelper dbHelper;
    private SQLiteHelperListener helperListener;
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

    private class UserDBHelperListener implements SQLiteHelperListener {

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE_USER);
            db.execSQL(SQL_CREATE_TABLE_FRIEND);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DROP_TABLE_USER);
            db.execSQL(SQL_DROP_TABLE_FRIEND);

            db.execSQL(SQL_CREATE_TABLE_USER);
            db.execSQL(SQL_CREATE_TABLE_FRIEND);
        }
    }

    public void initialize(Context context) {
        mContext = context;
        helperListener = new UserDBHelperListener();
        dbHelper = new UserRelativeSQLiteHelper(mContext, DB_NAME_USER_RELATIVE, DB_VERSION_USER_RELATIVE, helperListener);
        db = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        if (!db.isOpen())
            db = dbHelper.getWritableDatabase();
        return db;
    }

    public boolean isUserExist(long userId) {
        String sql = "SELECT * FROM user WHERE userId=?";
        String[] params = new String[]{String.valueOf(userId)};
        Cursor c = getWritableDatabase().rawQuery(sql, params);
        boolean isExist = (c.getCount() != 0);
        c.close();
        return isExist;
    }

    public void insertFriend(Friend bean) {
        bean.setModifyDate(System.currentTimeMillis());
        DBUtils.execInsert(getWritableDatabase(), bean, TABLE_IGNORE_FRIEND);
    }

    public void insertFriend(List<Friend> list) {
        for (Friend bean : list)
            insertFriend(bean);
    }

    public void insertOrUpdateUser(User bean) {
        deleteUser(bean.getUserId());
        insertUser(bean);
    }

    public void insertUser(User bean) {
        DBUtils.execInsert(getWritableDatabase(), bean, TABLE_IGNORE_USER);
    }

    public void insertUser(List<User> list) {
        for (User bean : list)
            insertUser(bean);
    }

    public void updateUserSex(long userId, String sex) {
        String sql = "UPDATE user SET sex=?" + " WHERE userId=?";
        Object[] params = new Object[]{sex, userId};
        getWritableDatabase().execSQL(sql, params);
    }

    public void deleteUser(long userId) {
        String sql = "DELETE FROM user WHERE userId=?";
        Object[] params = new Object[]{userId};
        getWritableDatabase().execSQL(sql, params);
    }

    public List<User> getAllUser() {
        String sql = "SELECT * FROM user";
        List<User> result = DBUtils.rawQuery(getWritableDatabase(), sql, null, User.class, TABLE_IGNORE_USER);
        return result;
    }

    public List<Friend> getAllFriend() {
        String sql = "SELECT * FROM friend";
        List<Friend> result = DBUtils.rawQuery(getWritableDatabase(), sql, null, Friend.class, TABLE_IGNORE_FRIEND);
        return result;
    }

    public User getUser(long userId) {
        String sql = "SELECT * FROM user WHERE userId=?";
        String[] params = new String[]{String.valueOf(userId)};
        List<User> result = DBUtils.rawQuery(getWritableDatabase(), sql, params, User.class, TABLE_IGNORE_USER);
        if (result != null && result.size() > 0) {
            /*for (int i = 0; i < result.size(); i++) {
                if (i > 0)
                    deleteUser(result.get(i).getUserId());
            }*/
            return result.get(0);
        } else
            return null;
    }

    public List<Friend> getAllFriend(long userId) {
        String sql = "SELECT * FROM friend WHERE ownerId=?";
        String[] params = new String[]{String.valueOf(userId)};
        List<Friend> result = DBUtils.rawQuery(getWritableDatabase(), sql, params, Friend.class, TABLE_IGNORE_FRIEND);
        if (result != null && result.size() > 0) {
            for (Friend f : result) {
                f.setUser(getUser(f.getFriendId()));
                Log.e(TAG, f.toString());
                Log.e(TAG, f.getUser().toString());
            }
        }
        return result;
    }

    public List<User> getAllMyFriend(long userId) {
        String sql = "select * from user left join friend on friend.friendId=user.userId where friend.ownerId=?";
        String[] params = new String[]{String.valueOf(userId)};
        List<User> result = DBUtils.rawQuery(getWritableDatabase(), sql, params, User.class, TABLE_IGNORE_USER);
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
     * 关闭数据库，操作完成后必须调用
     */
    public void closeDB() {
        db.close();
    }

}
