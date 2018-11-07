package example.db.roomDao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import example.bean.TestRoom;

@Dao
public interface TestRoomDao {

    @Insert
    long[] save(ArrayList<TestRoom> list);

    @Insert
    long save(TestRoom entity);

    @Update
    int update(TestRoom entity);

    @Update
    void update(ArrayList<TestRoom> list);

    @Delete
    void delete(TestRoom entity);

    @Query("DELETE FROM test_room")
    void deleteAll();

    @Query("SELECT * FROM test_room ORDER BY updateTime ASC")
    List<TestRoom> getAll();

    @Query("SELECT * FROM test_room ORDER BY updateTime DESC")
    List<TestRoom> getAllDesc();

    @Query("SELECT * FROM test_room LIMIT :current,:count ")
    List<TestRoom> getByLimit(int current, int count);

    @Query("SELECT COUNT(*) FROM test_room")
    int getCount();

    @Query("SELECT * FROM test_room WHERE id IN (:userIds)")
    List<TestRoom> queryAllByIds(int[] userIds);

    @Query("SELECT * FROM test_room WHERE id = :id LIMIT 1")
    TestRoom findById(String id);

    @Insert
    void insertAll(TestRoom... users);
}
