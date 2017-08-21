package example.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.style.bean.User;
import com.style.db.user.SQLiteHelperListener;
import com.style.db.user.UserRelativeSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

import example.bean.TestBean;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmDBManager {
    private static final String TAG = "RealmDBManager";
    public static final String DB_NAME = "test.realm";
    public static final int DB_VERSION = 1;//降版本会报错

    private static RealmDBManager mInstance;
    Realm myRealm;
    private Context mContext;

    //避免同时获取多个实例
    public synchronized static RealmDBManager getInstance() {
        if (mInstance == null) {
            mInstance = new RealmDBManager();
        }
        return mInstance;
    }

    public void initialize(Context context) {
        mContext = context;
        Realm.init(mContext);
        //默认配置
        //RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        //Realm.setDefaultConfiguration(configuration);
        //自定义配置
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        //注意：使用默认配置，系统也会生成一个default.realm的Realm文件.
    }

    public synchronized Realm getRealm() {
        if (myRealm == null) {
            myRealm = Realm.getDefaultInstance();
        }
        return myRealm;
    }

    /**
     * 关闭数据库，操作完成后必须调用
     */
    public void closeDB() {
        myRealm.close();
    }

    public void clearTable() {
        getRealm().beginTransaction();
        getRealm().deleteAll();
        getRealm().commitTransaction();
    }


    public void insertUser(TestBean o) {
        getRealm().beginTransaction();
        TestBean b = getRealm().copyToRealm(o);
        getRealm().commitTransaction();

    }


    public void insertUser(List<TestBean> list) {
        for (TestBean bean : list)
            insertUser(bean);
    }

    public void updateUserSex(String userId, String sex) {

    }

    public void delete(String userId) {

    }

    public List<TestBean> queryAll() {
        RealmResults<TestBean> dogs = getRealm().where(TestBean.class).findAll();
        /**
         * 对查询结果，按Id进行排序，只能对查询结果进行排序
         */
        //增序排列
//        dogs=dogs.sort("id");
        //降序排列
//        dogs=dogs.sort("id", Sort.DESCENDING);
        List<TestBean> list = getRealm().copyFromRealm(dogs);
        if (list != null && list.size() > 0) {
            for (TestBean b : list) {
                Log.e(TAG, b.toString());
            }
        }
        return list;
    }
}
