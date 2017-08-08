/*
 * Copyright (c) 2010-2015 www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.toolkit.db.orm.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.walkerljl.toolkit.lang.CollectionUtils;
import org.walkerljl.toolkit.lang.StringPool;

/**
 * 表
 *
 * @author lijunlin
 */
public class TableInfo {

    private static final String KEY_WRAPPER = "`";

    /**
     * 表名
     */
    private String name;
    /**
     * 是否分片
     */
    private boolean sharded = false;
    /**
     * 主键
     */
    private ColumnInfo primaryKey;
    /**
     * 字段列表
     */
    private List<ColumnInfo> columnInfos;
    /**
     * 分片的字段列表
     */
    private List<ColumnInfo> sharedColumnInfos;
    /**
     * 备注
     */
    private String comment;

    //扩展字段
    /**
     * 字段、属性映射Map
     */
    private Map<String, String> columnToPropertyMap = null;
    private String columnNameListString = "";

    /**
     * 默认构造函数
     */
    public TableInfo() {
    }

    //============自定义方法

    /**
     * 获取字段、属性映射Map
     *
     * @return
     */
    public Map<String, String> getColumnToPropertyMap() {
        if (columnToPropertyMap == null) {
            columnToPropertyMap = new LinkedHashMap<String, String>();
            for (ColumnInfo columnInfo : columnInfos) {
                columnToPropertyMap.put(columnInfo.getName(), columnInfo.getFieldName());
            }
        }
        return columnToPropertyMap;
    }

    public String getColumnNameListString() {
        if ("".equals(columnNameListString)) {
            Map<String, String> columnToPropertyMap = getColumnToPropertyMap();
            if (columnToPropertyMap == null) {
                return StringPool.EMPTY;
            }
            return CollectionUtils.wrap(columnToPropertyMap.keySet(), KEY_WRAPPER, KEY_WRAPPER, ",");
        } else {
            return columnNameListString;
        }
    }

    //============getter and setter

    /**
     * 获取表名
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 设置表名
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSharded() {
        return sharded;
    }

    public void setSharded(boolean sharded) {
        this.sharded = sharded;
    }

    /**
     * 获取主键
     *
     * @return
     */
    public ColumnInfo getPrimaryKey() {
        return primaryKey;
    }

    /**
     * 设置主键
     *
     * @param primaryKey
     */
    public void setPrimaryKey(ColumnInfo primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * 获取字段
     *
     * @return
     */
    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    /**
     * 设置字段
     *
     * @param columnInfos
     */
    public void setColumnInfos(List<ColumnInfo> columnInfos) {
        this.columnInfos = columnInfos;
    }

    public List<ColumnInfo> getSharedColumnInfos() {
        return sharedColumnInfos;
    }

    public void setSharedColumnInfos(List<ColumnInfo> sharedColumnInfos) {
        this.sharedColumnInfos = sharedColumnInfos;
    }

    /**
     * 获取备注
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置备注
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "name='" + name + '\'' +
                ", primaryKey=" + primaryKey +
                ", columnInfos=" + columnInfos +
                ", comment='" + comment + '\'' +
                ", columnToPropertyMap=" + columnToPropertyMap +
                ", columnNameListString='" + columnNameListString + '\'' +
                '}';
    }
}