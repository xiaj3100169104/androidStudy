package com.style.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.style.db.custom.SQLiteHelperListener;
import com.style.db.custom.UserDBManager;

public class MsgSQLOpenHelper extends SQLiteOpenHelper {
    protected String TAG = getClass().getSimpleName();
    private SQLiteHelperListener helperListener;

    public MsgSQLOpenHelper(Context context, String name, int version, SQLiteHelperListener helperListener) {
        super(context, name, null, version);
        this.helperListener = helperListener;
        // 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时
        // CursorFactory设置为null,使用系统默认的工厂类
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 调用时间：数据库第一次创建时onCreate()方法会被调用
        // onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
        // 这个方法中主要完成创建数据库后对数据库的操作
        Log.d(TAG, "onCreate");
        this.helperListener.onCreate(db);
        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）

        // 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, " onUpgrade");
        this.helperListener.onUpgrade(db, oldVersion, newVersion);
        // 调用时间：如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade
        // onUpgrade方法的三个参数，一个 SQLiteDatabase对象，一个旧的版本号和一个新的版本号
        // 这样就可以把一个数据库从旧的模型转变到新的模型
        // 这个方法中主要完成更改数据库版本的操作

        /*db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);*/
        // 上述做法简单来说就是，通过检查常量值来决定如何，升级时删除旧表，然后调用onCreate来创建新表
        // 一般在实际项目中是不能这么做的，正确的做法是在更新数据表结构时，还要考虑用户存放于数据库中的数据不丢失

    }

}
