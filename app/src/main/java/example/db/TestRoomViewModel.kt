package example.db

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.style.base.BaseViewModel
import com.style.entity.UserBean
import java.util.*
import kotlin.collections.ArrayList

class TestRoomViewModel(application: Application) : BaseViewModel(application) {

    @SuppressLint("CheckResult")
    fun saveOne() {
        val b = UserBean(UUID.randomUUID().toString(), "one", "one", 1, System.currentTimeMillis(), 1)
        getDataBase().testRoomDao.save(b)
             /*   .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { id->

                }, Consumer { e->

                })*/
    }

    fun saveList() {
        val list = ArrayList<UserBean>()
        for (i in 0..10) {
            val b = UserBean(UUID.randomUUID().toString(), i.toString(), i.toString(), i, System.currentTimeMillis(), i)
            list.add(b)
        }
        getDataBase().testRoomDao.save(list)
    }

    fun queryAll() {
        val list = getDataBase().testRoomDao.getAll()
        for (i in list.indices)
            Log.e(TAG, list[i].toString())
    }

    fun deleteAll() {
        getDataBase().testRoomDao.deleteAll()
    }

    fun findById() {
        val list = getDataBase().testRoomDao.getAll()
        val b = getDataBase().testRoomDao.findById(list[list.lastIndex].id)
        Log.e(TAG, b.toString())
    }

}