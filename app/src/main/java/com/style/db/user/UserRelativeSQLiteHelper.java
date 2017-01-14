package com.style.db.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by xiajun on 2017/1/7.
 * 用户相关数据库帮助类
 * 为方便操作，最好一个类对应一个数据库实例
 * 由于SQLiteOpenHelper内部只缓存一个数据库的连接（即一个SQLiteDatabase 实例mDatabase），
 * 所以，当SQLiteOpenHelper被多个线程调用的时候，就需要注意了。
 * SQLite最大的特点是你可以把各种类型的数据保存到任何字段中，而不用关心字段声明的数据类型是什么。
 * <p>
 * 例如：可以在Integer类型的字段中存放字符串，或者在布尔型字段中存放浮点数，或者在字符型字段中存放日期型值。
 * <p>
 * 但有一种情况例外：定义为INTEGER PRIMARY KEY的字段只能存储64位整数， 当向这种字段保存除整数以外的数据时，将会产生错误。
 * <p>
 * 另外， SQLite 在解析CREATE TABLE 语句时，会忽略 CREATE TABLE 语句中跟在字段名后面的数据类型信息，如下面语句会忽略 name字段的类型信息：
 * CREATE TABLE person (personid integer primary key autoincrement, name varchar(20))
 * SQLite可以解析大部分标准SQL语句，
 */

public class UserRelativeSQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = "UserSQLiteHelper";
    private SQLiteHelperListener helperListener;

    private int tagChange;//数据库升级时决定怎么数据库表

    public UserRelativeSQLiteHelper(Context context, String name, int version, SQLiteHelperListener helperListener) {
        this(context, name, null, version);
        this.helperListener = helperListener;

    }

    public UserRelativeSQLiteHelper(Context context, String name, int version, int tagChange) {
        this(context, name, null, version);
        this.tagChange = tagChange;

    }

    public UserRelativeSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate");
        helperListener.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* if (oldVersion==1 && newVersion==2) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3
            db.execSQL("ALTER TABLE restaurants ADD phone TEXT;");
        }*/
        Log.e(TAG, "onUpgrade");
        helperListener.onUpgrade(db, oldVersion, newVersion);
    }
}