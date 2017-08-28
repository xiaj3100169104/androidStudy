package example.greendao.dao;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.Property;

import java.util.List;

import example.bean.TestGreenBean;

public class GreenDaoManager {
    private static final String TAG = "GreenDaoManager";
    public static final String DB_NAME = "green.db";
    public static final int DB_VERSION = 1;//降版本会报错

    private static GreenDaoManager mInstance;
    private Context mContext;
    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoSession daoSession;
    private TestGreenBeanDao greenBeanDao;

    //避免同时获取多个实例
    public synchronized static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }
        return mInstance;
    }

    public void initialize(Context context) {
        mContext = context;
        devOpenHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        greenBeanDao = daoSession.getTestGreenBeanDao();
    }

    /**
     * 关闭数据库，操作完成后必须调用
     */
    public void closeDB() {
        devOpenHelper.close();
    }

    public void clearGreenTable() {
        greenBeanDao.deleteAll();
    }


    public void insertUser(TestGreenBean o) {
        greenBeanDao.insertOrReplace(o);
    }


    public void insertUser(List<TestGreenBean> list) {
        greenBeanDao.insertOrReplaceInTx(list);
    }

    public void update(TestGreenBean o) {
        greenBeanDao.update(o);
    }

    public void delete(String userId) {

    }

    public List<TestGreenBean> queryAll() {
        List<TestGreenBean> list = greenBeanDao.loadAll();
        logList(list);

        return list;
    }

    public List<TestGreenBean> queryAsc() {
        List<TestGreenBean> list = greenBeanDao.queryBuilder()  // 查询 User
                //.where(Properties.FirstName.eq("Joe"))  // 首名为 Joe
                .orderAsc(TestGreenBeanDao.Properties.Id)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }

    private void logList(List<TestGreenBean> list) {
        if (list != null && list.size() > 0) {
            for (TestGreenBean b : list) {
                Log.e(TAG, b.toString());
            }
        }
    }

    public List<TestGreenBean> queryDesc() {
        List<TestGreenBean> list = greenBeanDao.queryBuilder()  // 查询 User
                //.where(Properties.FirstName.eq("Joe"))  // 首名为 Joe
                .orderDesc(TestGreenBeanDao.Properties.Id)  // 末名升序排列
                .list();  // 返回集合
        logList(list);

        return list;
    }

    public List<TestGreenBean> queryWhereAnd() {
        List<TestGreenBean> list = greenBeanDao.queryBuilder()  // 查询 User
                .where(TestGreenBeanDao.Properties.Name.eq("name1"))  // 首名为 Joe
                .where(TestGreenBeanDao.Properties.Phone.eq("phone2"))  // 首名为 Joe
                .orderDesc(TestGreenBeanDao.Properties.Id)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }

    public List<TestGreenBean> queryWhereOr() {
        List<TestGreenBean> list = greenBeanDao.queryBuilder()  // 查询 User
                .whereOr(TestGreenBeanDao.Properties.Name.eq("name1"), TestGreenBeanDao.Properties.Phone.eq("phone2"))  // 首名为 Joe
                .orderDesc(TestGreenBeanDao.Properties.Id)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }

    public List<TestGreenBean> queryWhereBetween() {
        List<TestGreenBean> list = greenBeanDao.queryBuilder()  // 查询 User
                .where(TestGreenBeanDao.Properties.Id.between("0", "5"))  // 首名为 Joe
                .orderDesc(TestGreenBeanDao.Properties.Id)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }
}
