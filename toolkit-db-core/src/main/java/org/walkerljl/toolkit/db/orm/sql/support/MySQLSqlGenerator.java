package org.walkerljl.toolkit.db.orm.sql.support;

import java.util.List;

import org.walkerljl.toolkit.db.orm.EntityFieldValueUtils;
import org.walkerljl.toolkit.db.orm.Page;
import org.walkerljl.toolkit.db.orm.TableInfoManager;
import org.walkerljl.toolkit.db.orm.entity.ColumnInfo;
import org.walkerljl.toolkit.db.orm.entity.SqlEntry;
import org.walkerljl.toolkit.db.orm.entity.TableInfo;
import org.walkerljl.toolkit.db.orm.enums.DatabaseType;
import org.walkerljl.toolkit.db.orm.sql.SqlGenerator;
import org.walkerljl.toolkit.db.shard.parse.ShardParser;
import org.walkerljl.toolkit.lang.CollectionUtils;
import org.walkerljl.toolkit.lang.ListUtils;
import org.walkerljl.toolkit.standard.util.AssertUtils;

/**
 * SQL 生成工具类
 *
 * @author lijunlin
 */
public class MySQLSqlGenerator<KEY, Entity> implements SqlGenerator<KEY, Entity> {

    private static final String KEY_WRAPPER = "`";
    private static final String MESSAGE_ENTITY_IS_NULL = "实体为null";
    private static final String MESSAGE_ENTITIES_IS_EMPTY = "实体列表为空";
    private static final String MESSAGE_TABLE_IS_NULL = "表为null";
    private static final String MESSAGE_COLUMNS_IS_EMPTY = "字段列表为空";
    private static final String MESSAGE_KEYS_IS_EMPTY = "主键值列表为空";
    private static final String MESSAGE_WHERE_CONDITION_INVALID = "where语句无效";
    private static final String MESSAGE_SET_CLAUSE_INVALID = "set语句无效";
    private static final String MESSAGE_PAGE_PARAMS_INVALID = "分页参数无效";
    private static final String MESSAGE_PRIMARY_KEY_UNDEFINED = "主键未定义";

    private DatabaseType databaseType;
    private ShardParser  shardParser;

    public MySQLSqlGenerator(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * 生成批量添加实体sql
     *
     * @param entities
     * @return
     */
    @Override
    public SqlEntry generateBatchInsertSql(List<Entity> entities) {
        AssertUtils.assertTrue(CollectionUtils.isNotEmpty(entities), MESSAGE_ENTITIES_IS_EMPTY);
        TableInfo table = getTable(entities.get(0));

        int columnsSize = table.getColumnInfos().size();
        String columnsPlaceholder = join("?", columnsSize, ",");
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table.getName()).append("(").append(table.getColumnNameListString()).append(")VALUES");
        sql.append("(").append(columnsPlaceholder).append(")");
        int entitiesSize = entities.size();
        for (int i = 1; i < entitiesSize; i++) {
            sql.append(",(").append(columnsPlaceholder).append(")");
        }

        //参数
        Object[] params = new Object[columnsSize * entitiesSize];
        int index = 0;
        for (int i = 0; i < entitiesSize; i++) {
            Object[] singleEntityParams = EntityFieldValueUtils.getFieldValues(entities.get(i));
            if (singleEntityParams == null) {
                index += columnsSize - 1;
                continue;
            }
            for (int j = 0; j < columnsSize; j++) {
                params[index] = singleEntityParams[j];
                index++;
            }
        }
        return new SqlEntry(sql.toString(), params);
    }

    /**
     * 生成删除实体SQL
     *
     * @param entityClass
     * @param keys
     * @return
     */
    @Override
    public SqlEntry generateDeleteByKeysSql(Class<Entity> entityClass, List<KEY> keys) {
        AssertUtils.assertTrue(CollectionUtils.isNotEmpty(keys), MESSAGE_KEYS_IS_EMPTY);

        TableInfo table = getTable(entityClass);

        StringBuilder sql = new StringBuilder("DELETE FROM ").append(KEY_WRAPPER).append(table.getName()).append(KEY_WRAPPER).append(" WHERE ");
        sql.append(KEY_WRAPPER).append(table.getPrimaryKey().getName()).append(KEY_WRAPPER);
        int keysSize = keys.size();
        if (keysSize == 1) {
            sql.append(" = ").append("?");
        } else {
            sql.append(" IN (").append(join("?", keysSize, ",")).append(")");
        }
        Object[] keyArray = new Object[keysSize];
        return new SqlEntry(sql.toString(), keys.toArray(keyArray));
    }

    /**
     * 生成删除实体SQL
     *
     * @param entity
     * @return
     */
    @Override
    public SqlEntry generateDeleteSql(Entity entity) {
        TableInfo table = getTable(entity);

        SqlEntry whereClause = generateWhereClause(table.getColumnInfos(), entity, false);

        StringBuilder sql = new StringBuilder("DELETE FROM ").append(KEY_WRAPPER).append(table.getName()).append(KEY_WRAPPER).append(whereClause.getSql());
        return new SqlEntry(sql.toString(), whereClause.getParams());
    }

    /**
     * 生成更新实体SQL
     *
     * @param entity
     * @param keys
     * @return
     */
    @Override
    public SqlEntry generateUpdateByKeysSql(Entity entity, List<KEY> keys) {
        AssertUtils.assertTrue(CollectionUtils.isNotEmpty(keys), MESSAGE_KEYS_IS_EMPTY);

        TableInfo table = getTable(entity);
        SqlEntry setClause = generateSetClause(entity, table.getColumnInfos());

        //SQL
        int keysSize = keys.size();
        StringBuilder sql = new StringBuilder("UPDATE ").append(KEY_WRAPPER).append(table.getName()).append(KEY_WRAPPER);
        sql.append(setClause.getSql()).append(" WHERE ").append(KEY_WRAPPER).append(table.getPrimaryKey().getName()).append(KEY_WRAPPER);
        if (keysSize == 1) {
            sql.append(" = ?");
        } else {
            sql.append(" IN (").append(join("?", keysSize, ",")).append(")");
        }

        //参数
        int setClauseParamsLength = setClause.getParams().length;
        Object[] params = new Object[keysSize + setClauseParamsLength];
        int index = 0;
        for (int i = 0; i < setClauseParamsLength; i++) {
            params[i] = setClause.getParams()[i];
            index++;
        }
        for (int i = 0; i < keysSize; i++) {
            params[index + i] = keys.get(i);
        }
        return new SqlEntry(sql.toString(), params);
    }

    /**
     * 更新
     *
     * @param entity
     * @param conditionEntity
     * @return
     */
    @Override
    public SqlEntry generateUpdateSql(Entity entity, Entity conditionEntity) {
        AssertUtils.assertTrue(conditionEntity != null, MESSAGE_ENTITY_IS_NULL);

        TableInfo table = getTable(entity);
        SqlEntry setClause = generateSetClause(entity, table.getColumnInfos());
        SqlEntry whereClause = generateWhereClause(table.getColumnInfos(), conditionEntity, false);

        StringBuilder sql = new StringBuilder("UPDATE ").append(KEY_WRAPPER).append(table.getName()).append(KEY_WRAPPER).append(setClause.getSql()).append(whereClause.getSql());
        int setClauseParamsLength = setClause.getParams().length;
        int whereClauseParamsLength = whereClause.getParams().length;
        Object[] params = new Object[setClauseParamsLength + whereClauseParamsLength];
        int index = 0;
        for (int i = 0; i < setClauseParamsLength; i++) {
            params[i] = setClause.getParams()[i];
            index++;
        }
        for (int i = 0; i < whereClauseParamsLength; i++) {
            params[index + i] = whereClause.getParams()[i];
        }
        return new SqlEntry(sql.toString(), params);
    }

    /**
     * 查询
     *
     * @param entityClass
     * @param keys
     * @return
     */
    @Override
    public SqlEntry generateSelectByKeysSql(Class<Entity> entityClass, List<KEY> keys) {
        AssertUtils.assertTrue(CollectionUtils.isNotEmpty(keys), MESSAGE_KEYS_IS_EMPTY);

        TableInfo table = getTable(entityClass);

        int keysSize = keys.size();
        StringBuilder sql = new StringBuilder("SELECT ").append(table.getColumnNameListString()).append(" FROM ").append(KEY_WRAPPER).append(table.getName()).append(KEY_WRAPPER);
        sql.append(" WHERE ").append(KEY_WRAPPER).append(table.getPrimaryKey().getName()).append(KEY_WRAPPER);
        if (keysSize == 1) {
            sql.append(" = ").append("?").append(" LIMIT 0,1");
        } else {
            sql.append(" IN(").append(join("?", keysSize, ",")).append(")");
        }
        Object[] keyArray = new Object[keysSize];
        return new SqlEntry(sql.toString(), keys.toArray(keyArray));
    }

    /**
     * 查询
     *
     * @param entity
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public SqlEntry generateSelectSql(Entity entity, int currentPage, int pageSize) {
        AssertUtils.assertTrue((currentPage > 0 && pageSize > 0), MESSAGE_PAGE_PARAMS_INVALID);

        TableInfo table = getTable(entity);
        SqlEntry whereClause = generateWhereClause(table.getColumnInfos(), entity, true);

        Page<Entity> page = new Page<Entity>(currentPage, pageSize);
        StringBuilder sql = new StringBuilder("SELECT ").append(table.getColumnNameListString()).append(" FROM ").append(KEY_WRAPPER).append(table.getName()).append(KEY_WRAPPER);
        if (whereClause != null) {
            sql.append(whereClause.getSql());
        }
        sql.append(" ORDER BY ").append(KEY_WRAPPER).append(table.getPrimaryKey().getName()).append(KEY_WRAPPER).append(" DESC");
        sql.append(" LIMIT ").append(page.getStartIndex()).append(",").append(page.getPageSize());
        if (whereClause == null) {
            return new SqlEntry(sql.toString(), null);
        } else {
            return new SqlEntry(sql.toString(), whereClause.getParams());
        }
    }

    /**
     * 查询
     *
     * @param entity
     * @return
     */
    @Override
    public SqlEntry generateSelectCountSql(Entity entity) {
        AssertUtils.assertTrue(entity != null, MESSAGE_ENTITY_IS_NULL);

        TableInfo table = getTable(entity);
        SqlEntry whereClause = generateWhereClause(table.getColumnInfos(), entity, true);

        StringBuilder sql = new StringBuilder("SELECT COUNT(1)").append(" FROM ").append(KEY_WRAPPER).append(table.getName()).append(KEY_WRAPPER);
        if (whereClause != null) {
            sql.append(whereClause.getSql());
        }

        if (whereClause == null) {
            return new SqlEntry(sql.toString(), null);
        } else {
            return new SqlEntry(sql.toString(), whereClause.getParams());
        }
    }

    /**
     * 生成Where语句
     *
     * @param columns
     * @param entity
     * @param allowInvalid
     * @return
     */
    private SqlEntry generateWhereClause(List<ColumnInfo> columns, Entity entity, boolean allowInvalid) {
        StringBuilder whereCondition = new StringBuilder();
        List<Object> params = ListUtils.newArrayList();
        int counter = 0;
        for (ColumnInfo column : columns) {
            Object entityFieldValue = EntityFieldValueUtils.getFieldValue(entity, column);
            if (entityFieldValue == null) {
                continue;
            }
            if (counter == 0) {
                whereCondition.append(" WHERE ");
            } else {
                whereCondition.append(" AND ");
            }
            whereCondition.append(KEY_WRAPPER).append(column.getName()).append(KEY_WRAPPER).append(" = ?");
            params.add(entityFieldValue);
            counter++;
        }

        //校验
        if (allowInvalid) {
            if (CollectionUtils.isEmpty(params)) {
                return null;
            }
        } else {
            AssertUtils.assertTrue(CollectionUtils.isNotEmpty(params), MESSAGE_WHERE_CONDITION_INVALID);
        }

        Object[] paramsArray = new Object[params.size()];
        return new SqlEntry(whereCondition.toString(), params.toArray(paramsArray));
    }

    /**
     * 生成SET 语句
     *
     * @param entity
     * @param columns
     * @return
     */
    private SqlEntry generateSetClause(Entity entity, List<ColumnInfo> columns) {
        StringBuilder setClause = new StringBuilder();
        int counter = 0;
        List<Object> params = ListUtils.newArrayList();
        for (ColumnInfo column : columns) {
            Object entityFieldValue = EntityFieldValueUtils.getFieldValue(entity, column);
            if (entityFieldValue == null) {
                continue;
            }
            if (counter == 0) {
                setClause.append(" SET ");
            } else {
                setClause.append(",");
            }
            setClause.append(KEY_WRAPPER).append(column.getName()).append(KEY_WRAPPER).append(" = ?");
            params.add(entityFieldValue);
            counter++;
        }

        //校验
        AssertUtils.assertTrue(CollectionUtils.isNotEmpty(params), MESSAGE_SET_CLAUSE_INVALID);

        Object[] paramsArray = new Object[]{counter};
        return new SqlEntry(setClause.toString(), params.toArray(paramsArray));
    }

    /**
     * 拼接字符串
     *
     * @param text
     * @param count
     * @param seperator
     * @return
     */
    private String join(String text, int count, String seperator) {
        StringBuilder buffer = new StringBuilder(text.length() * count);
        buffer.append(text);
        for (int i = 1; i < count; i++) {
            buffer.append(seperator).append(text);
        }
        return buffer.toString();
    }

    /**
     * 获取Table
     *
     * @param entity
     * @return
     */
    private TableInfo getTable(Entity entity) {
        //校验实体对象是否有效
        AssertUtils.assertTrue(entity != null, MESSAGE_ENTITY_IS_NULL);

        return getTable((Class<Entity>)entity.getClass());
    }

    private String getTableName(Entity entity) {
        //校验实体对象是否有效
        AssertUtils.assertTrue(entity != null, MESSAGE_ENTITY_IS_NULL);

        return shardParser.parseTableName(entity);
    }

    /**
     * 获取Table
     *
     * @param entityClass
     * @return
     */
    private TableInfo getTable(Class<Entity> entityClass) {
        //校验根据实体是否能得到表对象
        TableInfo table = TableInfoManager.getInstance().getTable(entityClass);
        AssertUtils.assertTrue(table != null, MESSAGE_TABLE_IS_NULL);

        //校验字段列表是否为空
        AssertUtils.assertTrue(CollectionUtils.size(table.getColumnInfos()) != 0, MESSAGE_COLUMNS_IS_EMPTY);

        //校验主键是否存在
        AssertUtils.assertTrue(table.getPrimaryKey() != null, MESSAGE_PRIMARY_KEY_UNDEFINED);

        return table;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return databaseType;
    }
}
