package example.db

import android.app.Application
import com.style.base.BaseAndroidViewModel
import example.bean.TestRoom
import java.util.*
import kotlin.collections.ArrayList

class TestRoomViewModel(application: Application) : BaseAndroidViewModel(application) {

    fun saveOne() {
        val b = TestRoom(UUID.randomUUID().toString(), "one", "one", System.currentTimeMillis(), 1)
        getAppDataBase().testRoomDao.save(b)
    }

    fun saveList() {
        val list = ArrayList<TestRoom>()
        for (i in 0..10) {
            val b = TestRoom(UUID.randomUUID().toString(), i.toString(), i.toString(), System.currentTimeMillis(), i)
            list.add(b)
        }
        getAppDataBase().testRoomDao.save(list)
    }

    fun queryAll() {
        val list = getAppDataBase().testRoomDao.all
        for (i in list.indices)
            logE(TAG, list[i].toString())
    }

    fun deleteAll() {
        getAppDataBase().testRoomDao.deleteAll()
    }

    fun findById() {
        val list = getAppDataBase().testRoomDao.all
        val b = getAppDataBase().testRoomDao.findById(list[list.lastIndex].id)
        logE(TAG, b.toString())
    }

}