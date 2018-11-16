package com.style.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.db.room.TestRoomDao;

import example.bean.TestRoom;

/**
 * 单例和静态常量不用kotlin，写法麻烦
 */
@Database(entities = {TestRoom.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "TestRoomDataBase";

    private static AppDatabase instance = null;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "room_database.db").addCallback(new RoomDatabase.Callback() {
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
        return instance;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE test_room ADD COLUMN sex INTEGER  NOT NULL DEFAULT 1");
        }
    };

    /**
     * 处理跃迁：但用户从app版本1.0直接升级到4.0或者更高版本，这种几率很高。
     * 会碰到 1 -> 10 , 2 -> 10 , 3 -> 10 , 4 -> 10 , 5 -> 10 , 6 -> 10 , 7 -> 10 , 8 -> 10 , 9 -> 10
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

    public abstract TestRoomDao getTestRoomDao();

}
