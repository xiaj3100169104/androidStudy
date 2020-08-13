package com.style.data.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.style.data.fileDown.FileDownloadStateBean;
import com.style.entity.UserBean;

/**
 * 单例和静态常量不用kotlin，写法麻烦
 */
@Database(entities = {UserBean.class, FileDownloadStateBean.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "TestRoomDataBase";
    private static final String DB_NAME = "room_database.db";

    private static AppDatabase instance = null;
    private static final Object mLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (mLock) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Log.i(TAG, "onCreate:" + db.getPath());
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        Log.i(TAG, "onOpen:" + db.getPath());
                    }
                }).allowMainThreadQueries().addMigrations(MIGRATION_1_2).build();
            }
        }
        return instance;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //database.execSQL("ALTER TABLE test_room ADD COLUMN sex INTEGER  NOT NULL DEFAULT 1");
        }
    };

    /**
     * 处理跃迁：但用户从app版本1.0直接升级到4.0或者更高版本，这种几率很高。
     * 会碰到 1 -> 4 , 2 -> 4 , 3 -> 4。
     * 以前的每次升级应该都有记录.
     */
    static final Migration MIGRATION_1_4 = new Migration(1, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER");
            //创建表
            database.execSQL("CREATE TABLE student_new (student_id TEXT, student_name TEXT, phone_num INTEGER, PRIMARY KEY(student_id))");
            //复制表
            database.execSQL("INSERT INTO student_new (student_id, student_name, phone_num) SELECT student_id, student_name, phone_num FROM student");
            //删除表
            database.execSQL("DROP TABLE student");
            //增加表字段
            database.execSQL("ALTER TABLE users ADD COLUMN sex INTEGER");
        }
    };

    public abstract UserBeanDao getTestRoomDao();

    public abstract FileDownloadStateDao getFileDownloadDao();
}
