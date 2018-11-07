package example.db.roomDao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import example.bean.TestRoom;

@Database(entities = {TestRoom.class}, version = 1)
public abstract class TestRoomDataBase extends RoomDatabase {
    private static final String TAG = "TestRoomDataBase";

    private static TestRoomDataBase instance = null;

    public static TestRoomDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, TestRoomDataBase.class, "room_database.db").addCallback(new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    Log.i(TAG, "onCreate:" + db.getPath());
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    Log.i(TAG, "onOpen:" + db.getPath());
                }
            }).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract TestRoomDao getTestRoomDao();

}
