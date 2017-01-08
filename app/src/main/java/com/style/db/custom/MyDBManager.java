package com.style.db.custom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.style.constant.ConfigUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xj on 2016/1/19.
 * 我们需要确保在没有任何一个人在使用数据库时，才去关闭它。
 *在StackOverflow上推荐的做法是永远不要关闭数据库。
 *Android会尊重你这种做法，但会给你如下的提示。所以我一点也不推荐这种做法。
 * 当然频繁操作数据库不推荐频繁close数据库
 */
public class MyDBManager {
    private static final String TAG = "MyDBManager";
    public static final String DB_NAME_USER_RELATIVE = "userRelative.db";
    public static final int DB_VERSION_USER_RELATIVE = 2;
    public static final String TABLE_NAME_USER = "user";
    public static final String[] TABLE_USER_IGNORE = {"id", "password", "signKey"};

    private UserRelativeSQLiteHelper mHelper;
    private SQLiteDatabase db = null;
    private Context mContext;
    private static MyDBManager mInstance;

    //确保只有一个数据库连接存在，我们可以使用单例模式
    public synchronized static MyDBManager getInstance() {
        if (mInstance == null) {
            mInstance = new MyDBManager();
        }
        return mInstance;
    }

    public void initialize(Context context) {
        this.mContext = context;
        mHelper = new UserRelativeSQLiteHelper(context, DB_NAME_USER_RELATIVE, DB_VERSION_USER_RELATIVE);
        db = mHelper.getWritableDatabase();
        /*SQLiteOpenHelper的子类被实例化的时候，并不会马上创建数据库，只有当用户调用上面两个方法的时候，系统才会去创建数据库。
        首次执行上述两个函数的时候，会回调SQLiteOpenHelper的onCreate(), onUpgrade(), onOpen()三个函数。*/
    }

    public Context getContext() {
        return mContext;
    }

    public SQLiteDatabase getUserRelativeDB() {
        return db;
    }

    /**
     * 关闭数据库
     * 个人觉得没必要每次操作玩都关闭db，有点消耗性能，不如弄个全局的DB，等application 结束时再关闭。
     */
    public void closeDataBase() {
        getUserRelativeDB().close();
        mHelper = null;
        db = null;
    }

    /**
     * 删除数据库
     *
     * @return 成功返回true，否则返回false
     */
    public boolean deleteUserRelativeDataBase() {
        return mContext.deleteDatabase(DB_NAME_USER_RELATIVE);
    }

    /**
     * 插入一条数据
     *
     * @param obj
     * @return 返回-1代表插入数据库失败，否则成功
     * @throws IllegalAccessException
     */
    public long insert(SQLiteDatabase db,Object obj, String[] ignore) {
        Class<?> modeClass = obj.getClass();
        Field[] fields = modeClass.getDeclaredFields();
        ContentValues values = new ContentValues();

        for (Field fd : fields) {
            fd.setAccessible(true);
            String fieldName = fd.getName();
            //剔除忽略列
            boolean isIgnore = false;
            for (String columnName : ignore) {
                if (fieldName.equalsIgnoreCase(columnName)) {
                    isIgnore = true;
                    break;
                }
            }
            if (isIgnore)
                continue;
            putValues(values, fd, obj);
        }
        //不管第三个参数是否包含数据，执行Insert()方法必然会添加一条记录，如果第三个参数为空，会添加一条除主键之外其他字段值为Null的记录。
        return db.insert(DBUtils.getTableName(modeClass), null, values);
    }

    /**
     * 更新一条记录
     *
     * @param clazz  类
     * @param values 更新对象
     * @param id     更新id索引
     */
    public void updateById(Class<?> clazz, ContentValues values, long id) {
        getUserRelativeDB().update(clazz.getSimpleName(), values, "id=" + id, null);
    }

    /**
     * 查询数据库中所有的数据
     *
     * @param clazz
     * @param <T>   以 List的形式返回数据库中所有数据
     * @return 返回list集合
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public <T> List<T> findAll(Class<T> clazz) {
        Cursor cursor = getUserRelativeDB().query(clazz.getSimpleName(), null, null, null, null, null, null);
        return getEntity(cursor, clazz);
    }

    /**
     * 通过id查找制定数据
     *
     * @param clazz 指定类
     * @param id    条件id
     * @param <T>   类型
     * @return 返回满足条件的对象
     */
    public <T> T findById(Class<T> clazz, int id) {
        Cursor cursor = getUserRelativeDB().query(clazz.getSimpleName(), null, "id=" + id, null, null, null, null);
        List<T> list = getEntity(cursor, clazz);
        if (list != null && list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    /**
     * 根据指定条件返回满足条件的记录
     *
     * @param clazz      类
     * @param select     条件语句 ：（"id>？"）
     * @param selectArgs 条件(new String[]{"0"}) 查询id=0的记录
     * @param <T>        类型
     * @return 返回满足条件的list集合
     */
    public <T> List<T> findByArgs(Class<T> clazz, String select, String[] selectArgs) {
        Cursor cursor = getUserRelativeDB().query(clazz.getSimpleName(), null, select, selectArgs, null, null, null);
        return getEntity(cursor, clazz);
    }

    /**
     * 删除记录一条记录
     *
     * @param clazz 需要删除的类名
     * @param id    需要删除的 id索引
     */
    public void deleteById(Class<?> clazz, long id) {
        getUserRelativeDB().delete(DBUtils.getTableName(clazz), "id=" + id, null);
    }

    /**
     * 删除数据库中指定的表
     *
     * @param clazz
     */
    public void deleteTable(Class<?> clazz) {
        getUserRelativeDB().execSQL("DROP TABLE IF EXISTS" + DBUtils.getTableName(clazz));
    }

    /**
     * put value to ContentValues for Database
     *
     * @param values ContentValues object
     * @param fd     the Field
     * @param obj    the value
     */
    private void putValues(ContentValues values, Field fd, Object obj) {
        Class<?> clazz = values.getClass();
        try {
            Object[] parameters = new Object[]{fd.getName(), fd.get(obj)};
            Class<?>[] parameterTypes = getParameterTypes(fd, fd.get(obj), parameters);
            Method method = clazz.getDeclaredMethod("put", parameterTypes);
            method.setAccessible(true);
            method.invoke(values, parameters);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到反射方法中的参数类型
     *
     * @param field
     * @param fieldValue
     * @param parameters
     * @return
     */
    private Class<?>[] getParameterTypes(Field field, Object fieldValue, Object[] parameters) {
        Class<?>[] parameterTypes;
        if (isCharType(field)) {
            parameters[1] = String.valueOf(fieldValue);
            parameterTypes = new Class[]{String.class, String.class};
        } else {
            if (field.getType().isPrimitive()) {
                parameterTypes = new Class[]{String.class, getObjectType(field.getType())};
            } else if ("java.util.Date".equals(field.getType().getName())) {
                parameterTypes = new Class[]{String.class, Long.class};
            } else {
                parameterTypes = new Class[]{String.class, field.getType()};
            }
        }
        return parameterTypes;
    }

    /**
     * 是否是字符类型
     *
     * @param field
     * @return
     */
    private boolean isCharType(Field field) {
        String type = field.getType().getName();
        return type.equals("char") || type.endsWith("Character");
    }

    /**
     * 得到对象的类型
     *
     * @param primitiveType
     * @return
     */
    private Class<?> getObjectType(Class<?> primitiveType) {
        if (primitiveType != null) {
            if (primitiveType.isPrimitive()) {
                String basicTypeName = primitiveType.getName();
                if ("int".equals(basicTypeName)) {
                    return Integer.class;
                } else if ("short".equals(basicTypeName)) {
                    return Short.class;
                } else if ("long".equals(basicTypeName)) {
                    return Long.class;
                } else if ("float".equals(basicTypeName)) {
                    return Float.class;
                } else if ("double".equals(basicTypeName)) {
                    return Double.class;
                } else if ("boolean".equals(basicTypeName)) {
                    return Boolean.class;
                } else if ("char".equals(basicTypeName)) {
                    return Character.class;
                }
            }
        }
        return null;
    }


    /**
     * 从数据库得到实体类
     *
     * @param cursor
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> List<T> getEntity(Cursor cursor, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Field[] fields = clazz.getDeclaredFields();
                    T modeClass = clazz.newInstance();
                    for (Field field : fields) {
                        Class<?> cursorClass = cursor.getClass();
                        String columnMethodName = getColumnMethodName(field.getType());
                        Method cursorMethod = cursorClass.getMethod(columnMethodName, int.class);

                        Object value = cursorMethod.invoke(cursor, cursor.getColumnIndex(field.getName()));

                        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                            if ("0".equals(String.valueOf(value))) {
                                value = false;
                            } else if ("1".equals(String.valueOf(value))) {
                                value = true;
                            }
                        } else if (field.getType() == char.class || field.getType() == Character.class) {
                            value = ((String) value).charAt(0);
                        } else if (field.getType() == Date.class) {
                            long date = (Long) value;
                            if (date <= 0) {
                                value = null;
                            } else {
                                value = new Date(date);
                            }
                        }
                        String methodName = makeSetterMethodName(field);
                        Method method = clazz.getDeclaredMethod(methodName, field.getType());
                        method.invoke(modeClass, value);
                    }
                    list.add(modeClass);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName;
        if (fieldType.isPrimitive()) {
            typeName = DBUtils.capitalize(fieldType.getName());
        } else {
            typeName = fieldType.getSimpleName();
        }
        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getInt";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }
        return methodName;
    }


    private boolean isPrimitiveBooleanType(Field field) {
        Class<?> fieldType = field.getType();
        if ("boolean".equals(fieldType.getName())) {
            return true;
        }
        return false;
    }

    private String makeSetterMethodName(Field field) {
        String setterMethodName;
        String setterMethodPrefix = "set";
        if (isPrimitiveBooleanType(field) && field.getName().matches("^is[A-Z]{1}.*$")) {
            setterMethodName = setterMethodPrefix + field.getName().substring(2);
        } else if (field.getName().matches("^[a-z]{1}[A-Z]{1}.*")) {
            setterMethodName = setterMethodPrefix + field.getName();
        } else {
            setterMethodName = setterMethodPrefix + DBUtils.capitalize(field.getName());
        }
        return setterMethodName;
    }

}
