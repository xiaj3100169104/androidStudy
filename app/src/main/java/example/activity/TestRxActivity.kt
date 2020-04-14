package example.activity;

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test_rx.*

class TestRxActivity : BaseTitleBarActivity() {

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_test_rx)
        val menu = addRightMenu("menu", true)

        btn_just.setOnClickListener { testJust() }
        btn_map.setOnClickListener { testMap() }
        btn_flat_map.setOnClickListener { testFlatMap() }
    }

    override fun onClickTitleOption() {
        super.onClickTitleOption()
        logE(TAG, "menu")

    }

    @SuppressLint("CheckResult")
    private fun testJust() {
        val str = arrayOf("one", "two", "three", "four", "five")
        Observable.fromArray(*str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: String) {
                        logE(TAG, "onNext-$t")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        //Flowable是RxJava2.x中新增的，专门用于应对背压（Backpressure）问题。所谓背压，即生产者的速度大于消费者的速度带来的问题，
        // 比如在Android中常见的点击事件，点击过快则经常会造成点击两次的效果。其中，Flowable默认队列大小为128.
        Flowable.just("hello1", "hello2", "hello3", "hello4", "hello5", "hello6")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    println(s)
                }, { t ->
                    t.printStackTrace()
                    //注意：当mDisposable.isCanceled()时抛出的异常，这里不会补货，因为已经取消了订阅
                })
    }

    @SuppressLint("CheckResult")
    private fun testMap() {
        Flowable.just("hello RxJava 2")
                .subscribeOn(Schedulers.newThread())
                .map { t -> t.hashCode() }
                .observeOn(Schedulers.io())
                .map { value -> "hashCode=" + value }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { s -> println(s) }
    }

    @SuppressLint("CheckResult")
    private fun testFlatMap() {
        val students = arrayOfNulls<Student>(3)
        for (i in 0..2) {
            students[i] = Student()
            students[i]?.name = "Student$i"
            val courses = arrayOfNulls<Course>(10)
            for (j in 0..9) {
                courses[j] = Course()
                courses[j]?.name = students[i]?.name + "->" + "course$j"
            }
            students[i]?.courses = courses
        }
        Observable.fromArray<Student>(*students)
                .subscribeOn(Schedulers.io())
                .flatMap { f ->
                    return@flatMap Observable.fromArray<Course>(*f.courses)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ c -> println(c.name) }, { t -> })
    }

    companion object {
        class Student {
            var name = ""
            var courses = arrayOfNulls<Course>(0)
        }

        class Course {
            var name = ""
        }
    }
}
