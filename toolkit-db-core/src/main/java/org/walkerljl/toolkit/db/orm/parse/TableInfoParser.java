package org.walkerljl.toolkit.db.orm.parse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.walkerljl.toolkit.db.api.annotation.Column;
import org.walkerljl.toolkit.db.api.annotation.Entity;
import org.walkerljl.toolkit.db.api.annotation.Table;
import org.walkerljl.toolkit.db.orm.entity.ColumnInfo;
import org.walkerljl.toolkit.db.orm.entity.TableInfo;
import org.walkerljl.toolkit.lang.CollectionUtils;
import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;

/**
 * TableInfoParser
 *
 * @author lijunlin
 */
public class TableInfoParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableInfoParser.class);

    /**
     * 解析
     *
     * @param entityClass
     * @return
     */
    public static TableInfo parse(Class<?> entityClass) {
        return TableInfoParser.parseTableInfo(entityClass);
    }

    /**
     * 解析表名
     *
     * @param entityClass
     * @return
     */
    public static String parseTableName(Class<?> entityClass) {
        TableInfo tableInfo = TableInfoParser.parseSimpleTableByTableAnnotation(entityClass);
        if (tableInfo == null) {
            tableInfo = TableInfoParser.parseSimpleTableByEntityAnnotation(entityClass);
        }
        if (tableInfo == null) {
            return null;
        }
        return tableInfo.getName();
    }

    /**
     * 解析表
     *
     * @param entityClass
     * @return
     */
    private static TableInfo parseTableInfo(Class<?> entityClass) {
        TableInfo tableInfo = TableInfoParser.parseSimpleTableByTableAnnotation(entityClass);
        if (tableInfo == null) {
            tableInfo = TableInfoParser.parseSimpleTableByEntityAnnotation(entityClass);
        }
        if (tableInfo == null) {
            return null;
        }

        List<Class<?>> supperClasses = TableInfoParser.getAllSupperClasses(entityClass);
        if (CollectionUtils.isNotEmpty(supperClasses)) {
            for (Class<?> supperClass : supperClasses) {
                TableInfoParser.parseColumnInfo(tableInfo, supperClass);
            }
        }
        TableInfoParser.parseColumnInfo(tableInfo, entityClass);

        //字段排序
        CollectionUtils.sort(tableInfo.getColumnInfos());
        return tableInfo;
    }

    /**
     * 解析简单表(不包含字段)
     *
     * @param entityClass
     * @return
     */
    private static TableInfo parseSimpleTableByEntityAnnotation(Class<?> entityClass) {
        if (entityClass == null) {
            return null;
        }
        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("% entity has not annotation %s.", entityClass, Column.class));
            }
            return null;
        }
        String tableName = "";
        if ("".equals(entity.value())) {
            tableName = entityClass.getSimpleName();
        } else {
            tableName = entity.value();
        }

        TableInfo tableInfo = new TableInfo();
        tableInfo.setName(tableName);
        tableInfo.setComment(entity.comment());
        return tableInfo;
    }

    private static TableInfo parseSimpleTableByTableAnnotation(Class<?> entityClass) {
        if (entityClass == null) {
            return null;
        }
        Table table = entityClass.getAnnotation(Table.class);
        if (table == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("% entity has not annotation %s.", entityClass, Table.class));
            }
            return null;
        }
        String tableName = "";
        if ("".equals(table.value())) {
            tableName = entityClass.getSimpleName();
        } else {
            tableName = table.value();
        }

        TableInfo tableInfo = new TableInfo();
        tableInfo.setName(tableName);
        tableInfo.setComment(table.comment());
        tableInfo.setSharded(table.sharded());
        return tableInfo;
    }

    /**
     * 解析字段
     *
     * @param tableInfo
     * @param entityClass
     */
    private static void parseColumnInfo(TableInfo tableInfo, Class<?> entityClass) {
        if (tableInfo == null || entityClass == null) {
            return;
        }
        Field[] fields = entityClass.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("%s fields is empty.", entityClass));
            }
            return;
        }
        List<ColumnInfo> columnInfos = tableInfo.getColumnInfos();
        if (columnInfos == null) {
            columnInfos = new ArrayList<ColumnInfo>();
            tableInfo.setColumnInfos(columnInfos);
        }

        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            String columnName = "";
            if ("".equals(column.value())) {
                columnName = field.getName();
            } else {
                columnName = column.value();
            }
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfos.add(columnInfo);
            columnInfo.setPrimaryKey(column.key());
            columnInfo.setName(columnName);
            columnInfo.setFieldName(field.getName());
            columnInfo.setJavaType(field.getType());
            columnInfo.setSharded(column.sharded());

            if (columnInfo.isPrimaryKey()) {
                tableInfo.setPrimaryKey(columnInfo);
            }

            if (columnInfo.isSharded()) {
                List<ColumnInfo> shardedColumnInfos = tableInfo.getSharedColumnInfos();
                if (shardedColumnInfos == null) {
                    shardedColumnInfos = new ArrayList<ColumnInfo>();
                    tableInfo.setSharedColumnInfos(shardedColumnInfos);
                }
                shardedColumnInfos.add(columnInfo);
            }
        }
    }

    /**
     * 获取指定类的所有父类
     *
     * @param clazz
     * @return
     */
    private static List<Class<?>> getAllSupperClasses(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        Class<?> currentSupperClass = clazz.getSuperclass();
        if (currentSupperClass == null || currentSupperClass == Object.class) {
            return null;
        }
        List<Class<?>> resultSupperClasses = new ArrayList<Class<?>>();
        resultSupperClasses.add(currentSupperClass);

        //递归
        List<Class<?>> supperClasses = getAllSupperClasses(currentSupperClass);
        if (CollectionUtils.isEmpty(supperClasses)) {
            return resultSupperClasses;
        }
        for (Class<?> supperClass : supperClasses) {
            resultSupperClasses.add(supperClass);
        }
        return resultSupperClasses;
    }
}