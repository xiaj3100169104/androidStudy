package example.db

import android.app.Application
import android.util.Log
import com.style.base.BaseViewModel
import example.bean.TestRoom
import com.style.data.db.AppDatabase
import java.util.*
import kotlin.collections.ArrayList

class TestRoomViewModel(application: Application) : BaseViewModel(application) {

    fun saveOne() {
        val b = TestRoom(UUID.randomUUID().toString(), "one", "one", 1, System.currentTimeMillis(), 1)
        getAppDataBase().testRoomDao.save(b)
    }

    private fun getAppDataBase(): AppDatabase {
        return AppDatabase.getInstance(getApplication())
    }

    fun saveList() {
        val list = ArrayList<TestRoom>()
        for (i in 0..10) {
            val b = TestRoom(UUID.randomUUID().toString(), i.toString(), i.toString(), i, System.currentTimeMillis(), i)
            list.add(b)
        }
        getAppDataBase().testRoomDao.save(list)
    }

    fun queryAll() {
        val list = getAppDataBase().testRoomDao.getAll()
        for (i in list.indices)
            Log.e(TAG, list[i].toString())
    }

    fun deleteAll() {
        getAppDataBase().testRoomDao.deleteAll()
    }

    fun findById() {
        val list = getAppDataBase().testRoomDao.getAll()
        val b = getAppDataBase().testRoomDao.findById(list[list.lastIndex].id)
        Log.e(TAG, b.toString())
    }

}