package org.walkerljl.toolkit.db.orm.sql;

import java.util.List;

import org.walkerljl.toolkit.db.orm.entity.SqlEntry;
import org.walkerljl.toolkit.db.orm.enums.DatabaseType;

/**
 * SQL生成器
 *
 * @author: lijunlin
 */
public interface SqlGenerator<KEY, Entity> extends Cloneable {

    SqlEntry generateBatchInsertSql(List<Entity> entities);

    SqlEntry generateDeleteByKeysSql(Class<Entity> entityClass, List<KEY> keys);

    SqlEntry generateDeleteSql(Entity entity);

    SqlEntry generateUpdateByKeysSql(Entity entity, List<KEY> keys);

    SqlEntry generateUpdateSql(Entity entity, Entity conditionEntity);

    SqlEntry generateSelectByKeysSql(Class<Entity> entityClass, List<KEY> keys);

    SqlEntry generateSelectSql(Entity entity, int currentPage, int pageSize);

    SqlEntry generateSelectCountSql(Entity entity);

    DatabaseType getDatabaseType();
}
