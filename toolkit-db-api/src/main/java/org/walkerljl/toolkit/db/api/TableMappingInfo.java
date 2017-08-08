package org.walkerljl.toolkit.db.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库映射信息
 *
 * @author: lijunlin
 */
public class TableMappingInfo implements Serializable {

    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 主键实体字段
     */
    private String primaryKey;
    /**
     * 实体字段、数据库字段映射
     */
    private Map<String, String> fieldColumnMapping = new HashMap<String, String>();

    /**
     * 默认构造函数
     */
    public TableMappingInfo() {}

    /**
     * 构造函数
     * @param tableName 表名
     * @param primaryKey 主键字段
     * @param expressions 实体、数据字段映射表达式：["id:true-id",“user_id->userId”,"remark->remark"]
     */
    public TableMappingInfo(String tableName, String primaryKey, String... expressions) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        if (expressions != null && expressions.length != 0) {
            for (String expression : expressions) {
                parseMapping(expression);
            }
        }
    }

    /**
     * 解析映射
     *
     * "user_id:true->userId"解析结果：数据库字段为“user_id”，实体字段为“userId”，且数据库字段“user_id被设置为表的主键”。
     * @param expression
     */
    private void parseMapping(String expression) {
        if (expression == null || "".trim().equals(expression)) {
            return;
        }
        String[] mappingSegments = expression.split("->");
        if (mappingSegments == null || mappingSegments.length == 0) {
            return;
        }
        String[] columnExpression= mappingSegments[0].split(":");
        if (columnExpression == null || columnExpression.length == 0) {
            return;
        }

        //数据库字段名称
        String  columnName = columnExpression[0];
        //实体字段名称
        String fieldName = columnName;

        //解析主键字段
        if(columnExpression.length > 1 && "true".equals(columnExpression[1])) {
            this.primaryKey = columnName;
        }

        //解析实体字段
        if(mappingSegments.length > 1) {
            fieldName = mappingSegments[1];
        } else if(columnName.indexOf("_") != -1){
            fieldName = parseFieldNameByColumnName(columnName);
        }

        //设置实体、数据库字段映射
        fieldColumnMapping.put(fieldName, columnName);
    }


    /**
     * 根据数据库字段名称解析实体字段名称
     *
     * user_id->userId
     * id->id
     * @param columnName
     * @return
     */
    private String parseFieldNameByColumnName(String columnName) {
        char[] chars = columnName.toCharArray();
        char firstChar = Character.toLowerCase(chars[0]);
        StringBuffer sb = new StringBuffer();
        sb.append(firstChar);
        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];
            if (c == '_') {
                int j = i + 1;
                if (j < chars.length) {
                    sb.append(Character.toUpperCase(chars[j]));
                    i++;
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    //getters and setters

    /**
     * 获取表名
     * @return
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置表名
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取表的主键字段
     * @return
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * 设置表的主键字段
     * @param primaryKey
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * 获取实体、数据库字段映射
     *
     * @return
     */
    public Map<String, String> getFieldColumnMapping() {
        return fieldColumnMapping;
    }

    /**
     * 设置实体、数据库字段映射
     *
     * @param fieldColumnMapping
     */
    public void setFieldColumnMapping(Map<String, String> fieldColumnMapping) {
        this.fieldColumnMapping = fieldColumnMapping;
    }

    @Override
    public String toString() {
        return "TableMappingInfo{" +
                "tableName='" + tableName + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", fieldColumnMapping=" + fieldColumnMapping +
                '}';
    }
}
