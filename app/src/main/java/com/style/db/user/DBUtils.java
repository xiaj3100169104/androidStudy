package com.style.db.user;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by xj on 2016/1/23.
 */
public class DBUtils {
    private static final String TAG = "DBUtils";
    /*由此可见，
    getDeclaredMethod*()获取的是类自身声明的所有方法，包含public、protected和private方法。
    getMethod*()获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。*/

    /**
     * 得到建表语句
     *
     * @param fieldName 指定列名
     * @param ignore    忽略列数组
     * @return 是否是被忽略列
     */
    private static boolean isIgnoreColumnName(String[] ignore, String fieldName) {
        //默认不被忽略
        boolean isIgnore = false;
        for (String columnName : ignore) {
            if (fieldName.equalsIgnoreCase(columnName)) {
                isIgnore = true;
                break;
            }
        }
        return isIgnore;
    }

    /**
     * 得到建表语句
     *
     * @param clazz  指定默认类名小写作为表名
     * @param ignore 忽略列数组
     * @return sql语句
     */
    public static String getCreateTableSql(Class<?> clazz, String[] ignore) {
        String tabName = getTableName(clazz);
        String sql = getCreateTableSql(tabName, clazz, ignore);
        //Log.d(TAG, "getCreateTableSql== " + sql);
        return sql;
    }

    /**
     * 得到建表语句
     *
     * @param tabName 指定表名
     * @param clazz   指定类
     * @param ignore  忽略列数组
     * @return sql语句
     */
    public static String getCreateTableSql(String tabName, Class<?> clazz, String[] ignore) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ").append(tabName).append(" (id  INTEGER PRIMARY KEY AUTOINCREMENT, ");
        Field[] fields = clazz.getDeclaredFields();
        for (Field fd : fields) {
            String fieldName = fd.getName();
            String fieldType = fd.getType().getName();
            //剔除忽略列
            boolean isIgnore = isIgnoreColumnName(ignore, fieldName);
            if (isIgnore)
                continue;
            sb.append(fieldName).append(DBUtils.getColumnType(fieldType)).append(", ");

        }
        int len = sb.length();
        String sql = sb.replace(len - 2, len, ")").toString();
        //Log.d(TAG, "getCreateTableSql== " + sql);
        return sb.toString();
    }

    /**
     * 得到插入数据表语句
     *
     * @param db     数据库操作实例
     * @param obj    插入对象，指定默认类名小写作为表名
     * @param ignore 忽略列数组
     * @return sql语句
     */
    public static void execInsert(SQLiteDatabase db, Object obj, String[] ignore) {
        StringBuilder sb = new StringBuilder();
        String tabName = getTableName(obj.getClass());
        sb.append("INSERT INTO ").append(tabName).append(" ");
        StringBuilder sbColumns = new StringBuilder();//代替(_id, num, data)
        StringBuilder sbValues = new StringBuilder();//代替(?, ?, ?)
        sbColumns.append("(");
        sbValues.append("(");

        Field[] fields = obj.getClass().getDeclaredFields();
        int paramsLength = fields.length - ignore.length;
        Object[] bindArgs = new Object[paramsLength];

        int bindArgsIndex = 0;
        for (int i = 0, len = fields.length; i < len; i++) {
            String fieldName = fields[i].getName();
            //剔除忽略列
            boolean isIgnore = isIgnoreColumnName(ignore, fieldName);
            if (isIgnore)
                continue;
            sbColumns.append(fieldName).append(", ");
            sbValues.append("?").append(", ");
            //获取成员变量值对象
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object fieldObj = fields[i].get(obj);
                bindArgs[bindArgsIndex] = fieldObj == null ? "" : fieldObj;
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            bindArgsIndex++;
        }
        int sbColumnsLen = sbColumns.length();
        sbColumns.replace(sbColumnsLen - 2, sbColumnsLen, ")");
        int sbValuesLen = sbValues.length();
        sbValues.replace(sbValuesLen - 2, sbValuesLen, ")");
        String sql = sb.append(sbColumns.toString()).append(" values ").append(sbValues.toString()).toString();
        //Log.e(TAG, "execInsertSQL == " + sql);
        db.execSQL(sql, bindArgs);//execSQL为解决特殊字符插入异常，为升级版
    }

    /**
     * 得到插入数据表语句
     * -----------------------容易出现顺序不对应问题
     *
     * @param tabName 指定表名
     * @param clazz   指定类
     * @param ignore  忽略列数组
     * @return sql语句
     */
    public static String getInsertSql(String tabName, Class<?> clazz, String[] ignore) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tabName).append(" ");
        StringBuilder sbColumns = new StringBuilder();//代替(_id, num, data)
        StringBuilder sbValues = new StringBuilder();//代替(?, ?, ?)
        sbColumns.append("(");
        sbValues.append("(");
        Field[] fields = clazz.getDeclaredFields();
        for (Field fd : fields) {
            String fieldName = fd.getName();
            //剔除忽略列
            boolean isIgnore = isIgnoreColumnName(ignore, fieldName);
            if (isIgnore)
                continue;
            sbColumns.append(fieldName).append(", ");
            sbValues.append("?").append(", ");

        }
        int sbColumnsLen = sbColumns.length();
        sbColumns.replace(sbColumnsLen - 2, sbColumnsLen, ")");
        int sbValuesLen = sbValues.length();
        sbValues.replace(sbValuesLen - 2, sbValuesLen, ")");
        String sql = sb.append(sbColumns.toString()).append(" values ").append(sbValues.toString()).toString();
        //Log.e(TAG, "getInsertSql == " + sql);
        return sql;
    }

    /**
     * 得到插入数据表参数值
     *
     * @param obj    指定对象
     * @param ignore 忽略列数组
     * @return sql语句
     */
    public static Object[] getInsertParams(Object obj, String[] ignore) {
        Field[] fields = obj.getClass().getDeclaredFields();
        int paramsLength = fields.length - ignore.length;
        Object[] bindArgs = new Object[paramsLength];

        int bindArgsIndex = 0;
        for (int i = 0, len = fields.length; i < len; i++) {
            String fieldName = fields[i].getName();
            //剔除忽略列
            boolean isIgnore = isIgnoreColumnName(ignore, fieldName);

            if (isIgnore)
                continue;
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object fieldObj = fields[i].get(obj);
                bindArgs[bindArgsIndex] = fieldObj;
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            bindArgsIndex++;
        }
        return bindArgs;
    }

    /**
     * 通过id查找制定数据
     *
     * @param db    数据库操作实例
     * @param clazz 指定类
     * @param <T>   类型
     * @return 返回满足条件的对象
     */
    public static <T> List<T> rawQuery(SQLiteDatabase db, String sql, String[] selectionArgs, Class<T> clazz, String[] ignore) {
        Cursor c = db.rawQuery(sql, selectionArgs);
        List<T> result = getEntity(c, clazz, ignore);
        return result;
    }

    public static String getDropTableSql(Class<?> clazz) {
        return getDropTableSql(getTableName(clazz));
    }

    public static String getDropTableSql(String tabName) {
        return "DROP TABLE IF EXISTS " + tabName;
    }

    public static String getColumnType(String type) {
        String value = null;
        if (type.contains("String")) {
            value = " text ";
        } else if (type.contains("int")) {
            value = " integer ";
        } else if (type.contains("boolean")) {
            value = " boolean ";
        } else if (type.contains("float")) {
            value = " float ";
        } else if (type.contains("double")) {
            value = " double ";
        } else if (type.contains("char")) {
            value = " varchar ";
        } else if (type.contains("long")) {
            value = " long ";
        }
        return value;
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }

    /**
     * 从数据库得到实体类
     *
     * @param <T>
     * @param cursor
     * @param clazz
     * @param ignore
     * @return
     */
    private static <T> List<T> getEntity(Cursor cursor, Class<T> clazz, String[] ignore) {
        List<T> list = null;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Field[] fields = clazz.getDeclaredFields();
                    T modeClass = clazz.newInstance();
                    for (Field field : fields) {
                        String fieldName = field.getName();
                        Class<?> type = field.getType();
                        String typeName = type.getName();
                        boolean isIgnore = isIgnoreColumnName(ignore, fieldName);

                        if (fieldName.equalsIgnoreCase("id")) {//需要
                            isIgnore = false;
                        }
                       Object value = null;
                        if (!isIgnore){//不是忽略字段再去取值，不然会报错
                            Class<?> cursorClass = cursor.getClass();
                            String columnMethodName = getColumnMethodName(type);
                            Method cursorMethod = cursorClass.getMethod(columnMethodName, int.class);//取列名对应的游标索引方法
                            value = cursorMethod.invoke(cursor, cursor.getColumnIndex(fieldName));
                        }

                        if (type == boolean.class || type == Boolean.class) {
                            if ("0".equals(String.valueOf(value))) {
                                value = false;
                            } else if ("1".equals(String.valueOf(value))) {
                                value = true;
                            }
                        } else if (type == char.class || type == Character.class || type == String.class) {
                            if (isIgnore)
                                value = "";//如果是忽略的字符类型默认值为:""
                                /*else
                                    value = ((String) value).charAt(0);*/
                        } else if (type == Date.class) {
                            long date = (Long) value;
                            if (date <= 0) {
                                value = null;
                            } else {
                                value = new Date(date);
                            }
                        }
                        String methodName = makeSetterMethodName(field);//获取set方法名
                        Method method = clazz.getDeclaredMethod(methodName, type);//根据方法名和参数列表获取方法对象
                        method.invoke(modeClass, value);//方法赋值，并赋给对象
                    }
                    //Log.e(TAG, "getEntity=" + modeClass.toString());
                    list.add(modeClass);
                }
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

    private static String getColumnMethodName(Class<?> fieldType) {
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


    private static boolean isPrimitiveBooleanType(Field field) {
        Class<?> fieldType = field.getType();
        if ("boolean".equals(fieldType.getName())) {
            return true;
        }
        return false;
    }

    private static String makeSetterMethodName(Field field) {
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
