package com.style.db.custom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import test.bean.User;

/**
 * Created by xj on 2016/1/19.
 */
public class DBManager {
    private static final String TAG = "DBManager";
    private static final String DB_NAME = "demo.db";
    private static final int DB_VERSION = 2;
    private SQLiteDatabase db = null;
    private MySQLiteHelper mHelper;
    private Context mContext;
    private String db_name;
    private static DBManager mInstance;

    public synchronized static DBManager getInstance() {
        if (mInstance == null) {
            mInstance = new DBManager();
        }
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.db_name = DB_NAME;
        mHelper = new MySQLiteHelper(getContext(), db_name, DB_VERSION, new TableParam(User.class, "password"));
        db = mHelper.getWritableDatabase();
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        db.close();
        mHelper = null;
        db = null;
    }

    /**
     * 删除数据库
     *
     * @return 成功返回true，否则返回false
     */
    public boolean deleteDataBase() {
        return mContext.deleteDatabase(db_name);
    }

    /**
     * 插入一条数据
     *
     * @param obj
     * @return 返回-1代表插入数据库失败，否则成功
     * @throws IllegalAccessException
     */
    public long insert(Object obj) {
        Class<?> modeClass = obj.getClass();
        Field[] fields = modeClass.getDeclaredFields();
        ContentValues values = new ContentValues();

        for (Field fd : fields) {
            fd.setAccessible(true);
            String fieldName = fd.getName();
            //剔除主键id值得保存，由于框架默认设置id为主键自动增长
            if (fieldName.equalsIgnoreCase("id") || fieldName.equalsIgnoreCase("_id")) {
                continue;
            }
            putValues(values, fd, obj);
        }
        return db.insert(DBUtils.getTableName(modeClass), null, values);
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
        Cursor cursor = db.query(clazz.getSimpleName(), null, null, null, null, null, null);
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
        Cursor cursor = db.query(clazz.getSimpleName(), null, "id=" + id, null, null, null, null);
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
        Cursor cursor = db.query(clazz.getSimpleName(), null, select, selectArgs, null, null, null);
        return getEntity(cursor, clazz);
    }

    /**
     * 删除记录一条记录
     *
     * @param clazz 需要删除的类名
     * @param id    需要删除的 id索引
     */
    public void deleteById(Class<?> clazz, long id) {
        db.delete(DBUtils.getTableName(clazz), "id=" + id, null);
    }

    /**
     * 删除数据库中指定的表
     *
     * @param clazz
     */
    public void deleteTable(Class<?> clazz) {
        db.execSQL("DROP TABLE IF EXISTS" + DBUtils.getTableName(clazz));
    }

    /**
     * 更新一条记录
     *
     * @param clazz  类
     * @param values 更新对象
     * @param id     更新id索引
     */
    public void updateById(Class<?> clazz, ContentValues values, long id) {
        db.update(clazz.getSimpleName(), values, "id=" + id, null);
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
