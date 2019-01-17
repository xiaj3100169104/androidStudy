package com.style.data.db

import android.arch.persistence.room.*
import com.style.entity.TestRoom
import java.util.*

@Dao
interface TestRoomDao {

    @Query("SELECT * FROM test_room ORDER BY updateTime ASC")
    fun getAll(): List<TestRoom>

    @Query("SELECT * FROM test_room ORDER BY updateTime DESC")
    fun getAllDesc(): List<TestRoom>

    @Query("SELECT COUNT(*) FROM test_room")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(list: ArrayList<TestRoom>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: TestRoom): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: TestRoom): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(list: ArrayList<TestRoom>)

    @Delete
    fun delete(entity: TestRoom)

    @Query("DELETE FROM test_room")
    fun deleteAll()

    @Query("SELECT * FROM test_room LIMIT :current,:count ")
    fun getByLimit(current: Int, count: Int): List<TestRoom>

    @Query("SELECT * FROM test_room WHERE id IN (:userIds)")
    fun queryAllByIds(userIds: IntArray): List<TestRoom>

    @Query("SELECT * FROM test_room WHERE id = :id LIMIT 1")
    fun findById(id: String): TestRoom

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: TestRoom)
}
