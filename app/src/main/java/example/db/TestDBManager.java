package example.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.style.bean.User;
import com.style.data.db.user.SQLiteHelperListener;
import com.style.data.db.user.UserRelativeSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

import example.bean.TestBean;

public class TestDBManager {
    private static final String TAG = "TestDBManager";
    public static final String DB_NAME_USER_RELATIVE = "test.db";
    public static final int DB_VERSION_USER_RELATIVE = 4;//降版本会报错

    private static final String SQL_CREATE_TABLE_USER = "CREATE TABLE testbean (id  INTEGER PRIMARY KEY AUTOINCREMENT,name,phone)";
    private static final String SQL_CREATE_TABLE_USER_UP = "CREATE TABLE testbean (id  INTEGER PRIMARY KEY AUTOINCREMENT,name,phone,age)";

    private static final String SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS testbean";
    private UserRelativeSQLiteHelper dbHelper;
    private SQLiteHelperListener helperListener;
    private static TestDBManager mInstance;
    private SQLiteDatabase db;
    private Context mContext;

    //避免同时获取多个实例
    public synchronized static TestDBManager getInstance() {
        if (mInstance == null) {
            mInstance = new TestDBManager();
        }
        return mInstance;
    }

    private class UserDBHelperListener implements SQLiteHelperListener {

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE_USER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DROP_TABLE_USER);
            //db.execSQL(SQL_CREATE_TABLE_USER);
            //db.execSQL("ALTER TABLE testbean ADD COLUMN age INTEGER");
            db.execSQL(SQL_CREATE_TABLE_USER_UP);
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

    public void insertUser(TestBean bean) {
        try {
            String sql = "INSERT INTO testbean (name,phone) values (?, ?)";
            Object[] bindArgs = new Object[]{bean.getName(), bean.getPhone()};
            getWritableDatabase().execSQL(sql, bindArgs);//execSQL为解决特殊字符插入异常，为升级版
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertUserDBUp(TestBean bean) {
        try {
            String sql = "INSERT INTO testbean (name,phone,age) values (?, ?, ?)";
            Object[] bindArgs = new Object[]{bean.getName(), bean.getPhone(), bean.getAge()};
            getWritableDatabase().execSQL(sql, bindArgs);//execSQL为解决特殊字符插入异常，为升级版
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertUser(List<TestBean> list) {
        for (TestBean bean : list)
            insertUser(bean);
    }

    public void updateUserSex(String userId, String sex) {
        String sql = "UPDATE testbean SET sex=?" + " WHERE userId=?";
        Object[] params = new Object[]{sex, userId};
        getWritableDatabase().execSQL(sql, params);
    }

    public void deleteUser(String userId) {
        String sql = "DELETE FROM user WHERE userId=?";
        Object[] params = new Object[]{userId};
        getWritableDatabase().execSQL(sql, params);
    }

    public List<TestBean> queryAll() {
        String sql = "SELECT * FROM testbean";
        Cursor c = null;
        List<TestBean> list = null;
        try {
            c = db.rawQuery(sql, null);
            if (c != null && c.getCount() > 0) {
                list = new ArrayList<>();
                while (c.moveToNext()) {
                    TestBean bean = new TestBean();
                    bean.setId(c.getString(c.getColumnIndex("id")));
                    bean.setName(c.getString(c.getColumnIndex("name")));
                    bean.setPhone(c.getString(c.getColumnIndex("phone")));
                    bean.setAge(c.getInt(c.getColumnIndex("age")));
                    Log.e(TAG, "getEntity=" + bean.toString());
                    list.add(bean);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return list;
    }

    public void clearTable() {
        try {
            getWritableDatabase().execSQL("DELETE FROM testbean");
            getWritableDatabase().execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'testbean'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        getWritableDatabase().close();
    }

}
