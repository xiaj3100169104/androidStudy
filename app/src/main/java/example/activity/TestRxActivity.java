package example.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.style.base.BaseActivity;
import com.style.framework.R;

import org.reactivestreams.Publisher;

import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TestRxActivity extends BaseActivity {

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_test_rx;
        super.onCreate(savedInstanceState);
    }


    @OnClick(R.id.btn_just)
    public void skip1() {
        String[] str = {"one", "two", "three", "four", "five"};
        Observable.fromArray(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });
        Flowable.just("hello1", "hello2", "hello3", "hello4", "hello5", "hello6")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }

    @OnClick(R.id.btn_map)
    public void skip2() {
        Flowable.just("hello RxJava 2")
                .subscribeOn(Schedulers.newThread())
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        return s.hashCode();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer value) throws Exception {
                        return "hashCode=" + value;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }

    @OnClick(R.id.btn_flat_map)
    public void skip3() {
        Student[] students = new Student[3];
        for (int i = 0; i < 3; i++) {
            students[i] = new Student();
            students[i].name = "Student" + i;
            Course[] courses = new Course[10];
            for (int j = 0; j < 10; j++) {
                courses[j] = new Course();
                courses[j].name = students[i].name + "->" + "course" + j;
            }
            students[i].courses = courses;
        }
        Observable.fromArray(students)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> apply(@NonNull Student s) throws Exception {
                        return Observable.fromArray(s.courses);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Course>() {
                    @Override
                    public void accept(Course s) throws Exception {
                        System.out.println(s.name);
                    }
                });
    }

    static class Student {
        String name;
        Course[] courses;

    }

    static class Course {
        String name;
    }

}
