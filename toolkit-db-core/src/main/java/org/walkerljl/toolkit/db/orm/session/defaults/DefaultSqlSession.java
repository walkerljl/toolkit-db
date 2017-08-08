package org.walkerljl.toolkit.db.orm.session.defaults;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import org.walkerljl.toolkit.db.orm.entity.SqlEntry;
import org.walkerljl.toolkit.db.orm.executor.Executor;
import org.walkerljl.toolkit.db.orm.executor.defaults.DefaultExecutor;
import org.walkerljl.toolkit.db.orm.session.Configuration;
import org.walkerljl.toolkit.db.orm.session.SqlSession;
import org.walkerljl.toolkit.db.orm.sql.support.MySQLSqlGenerator;
import org.walkerljl.toolkit.lang.ListUtils;
import org.walkerljl.toolkit.standard.util.AssertUtils;

/**
 * DefaultSqlSession
 *
 * @param <T>
 * @param <KEY>
 * @author lijunlin
 */
public class DefaultSqlSession<T, KEY extends Serializable> implements SqlSession<T, KEY> {

    private static final String MESSAGE_SQL_IS_NULL = "生成SQL对象为NULL";
    private Configuration     configuration;
    private Executor          executor;
    private MySQLSqlGenerator sqlGenerator;

    /**
     * 构造函数
     *
     * @param configuration
     */
    public DefaultSqlSession(Configuration configuration) {
        AssertUtils.assertTrue(configuration != null, "configuration 为NULL");
        AssertUtils.assertTrue(configuration.getDataSource() != null, "dataSource 为NULL");
        AssertUtils.assertTrue(configuration.getDatabaseType() != null, "databaseType 为NULL");
        this.configuration = configuration;
        this.executor = new DefaultExecutor(this.configuration.getDataSource());
        this.sqlGenerator = new MySQLSqlGenerator(this.configuration.getDatabaseType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public KEY insertReturnPK(T entity) {
        SqlEntry sqlEntry = sqlGenerator.generateBatchInsertSql(ListUtils.newArrayList(entity));
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return (KEY) executor.insertReturnPK(sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    public int insert(List<T> entities) {
        SqlEntry sqlEntry = sqlGenerator.generateBatchInsertSql(entities);
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    public int deleteByKey(Class<T> entityClass, KEY key) {
        return deleteByKeys(entityClass, ListUtils.newArrayList(key));
    }

    @Override
    public int deleteByKeys(Class<T> entityClass, List<KEY> keys) {
        SqlEntry sqlEntry = sqlGenerator.generateDeleteByKeysSql(entityClass, keys);
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    public int delete(T condition) {
        SqlEntry sqlEntry = sqlGenerator.generateDeleteSql(condition);
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    public int updateByKey(T entity, KEY key) {
        return updateByKeys(entity, ListUtils.newArrayList(key));
    }

    @Override
    public int updateByKeys(T entity, List<KEY> keys) {
        SqlEntry sqlEntry = sqlGenerator.generateUpdateByKeysSql(entity, keys);
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    public int update(T entity, T condition) {
        SqlEntry sqlEntry = sqlGenerator.generateUpdateSql(entity, condition);
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    public T selectByKey(Class<T> entityClass, KEY key) {
        SqlEntry sqlEntry = sqlGenerator.generateSelectByKeysSql(entityClass, ListUtils.newArrayList(key));
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return executor.queryEntity(entityClass, sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    public List<T> selectListByKeys(Class<T> entityClass, List<KEY> keys) {
        SqlEntry sqlEntry = sqlGenerator.generateSelectByKeysSql(entityClass, keys);
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return executor.queryEntityList(entityClass, sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> selectList(T condition, int currentPage, int pageSize) {
        SqlEntry sqlEntry = sqlGenerator.generateSelectSql(condition, currentPage, pageSize);
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return (List<T>) executor.queryEntityList(condition.getClass(), sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    public int selectListCount(T condition) {
        SqlEntry sqlEntry = sqlGenerator.generateSelectCountSql(condition);
        AssertUtils.assertTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
        return executor.queryCount(sqlEntry.getSql(), sqlEntry.getParams());
    }

    @Override
    public void commit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollback() {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public Configuration getConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Connection getConnection() {
        // TODO Auto-generated method stub
        return null;
    }


}