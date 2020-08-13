package com.style.data.db

import androidx.room.*
import com.style.entity.UserBean
import java.util.*

/**
 * INSERT:
 * 返回的类型是Long也只能是Long，否则无法通过编译。
 * 返回的Long值，是指的插入的rowId。如果参数是一个数组或者集合，那么应该返回long[]或者List。
 * UPDATE:
 * 返回的类型为Integer也只能是Integer，否则无法通过编译。
 * 返回的Integer值，指的是该次操作影响到的总行数，比如该次操作更新了5条，就返回5。
 * DELETE:
 * 你可以让这个方法返回一个int类型的值，表示从数据库中被删除的行数，虽然通常并没有这个必要。
 * 关于@Query返回Single和Maybe类型的Bug:
 * 目前改版本被我试出来一个bug，那就是用@Query的操作，在进行UPDATE，DELETE和INSERT操作时，直接通过RxJava进行线程切换是没用的，像下面这样写：
 */

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
    fun save(entity: UserBean)//: Single<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: UserBean)//: Single<Int>

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
