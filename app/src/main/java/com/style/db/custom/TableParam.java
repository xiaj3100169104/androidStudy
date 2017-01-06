package com.style.db.custom;

/**
 * Created by 850302 on 2016/1/19.
 * 根据实体生成创建数据库表语句
 */
public class TableParam {
    public Class mClazz;
    public String[] ignureColumns;

    public TableParam(Class clz, String... ignureColumns) {
        this.mClazz = clz;
        this.ignureColumns = ignureColumns;
    }
}