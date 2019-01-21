package com.style.data.db

import android.arch.persistence.room.*
import com.style.entity.UserBean
import java.util.*

@Dao
interface UserBeanDao {

    @Query("SELECT * FROM user_table ORDER BY updateTime ASC")
    fun getAll(): List<UserBean>

    @Query("SELECT * FROM user_table ORDER BY updateTime DESC")
    fun getAllDesc(): List<UserBean>

    @Query("SELECT COUNT(*) FROM user_table")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(list: ArrayList<UserBean>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: UserBean): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: UserBean): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(list: ArrayList<UserBean>)

    @Delete
    fun delete(entity: UserBean)

    @Query("DELETE FROM user_table")
    fun deleteAll()

    @Query("SELECT * FROM user_table LIMIT :current,:count ")
    fun getByLimit(current: Int, count: Int): List<UserBean>

    @Query("SELECT * FROM user_table WHERE id IN (:userIds)")
    fun queryAllByIds(userIds: IntArray): List<UserBean>

    @Query("SELECT * FROM user_table WHERE id = :id LIMIT 1")
    fun findById(id: String): UserBean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: UserBean)
}
