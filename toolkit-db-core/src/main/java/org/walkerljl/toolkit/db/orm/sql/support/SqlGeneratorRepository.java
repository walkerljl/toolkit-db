package org.walkerljl.toolkit.db.orm.sql.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.walkerljl.toolkit.db.DbException;
import org.walkerljl.toolkit.db.orm.enums.DatabaseType;
import org.walkerljl.toolkit.db.orm.sql.SqlGenerator;
import org.walkerljl.toolkit.lang.ObjectUtils;
/**
 * SQL生成器仓库
 *
 * @author: lijunlin
 */
public class SqlGeneratorRepository {

    private static final Map<DatabaseType, SqlGenerator> RESOURCES_MAP = new HashMap<DatabaseType, SqlGenerator>();

    /**
     * 绑定
     *
     * @param databaseType
     * @param sqlGenerator
     */
    public static void bind(DatabaseType databaseType, SqlGenerator sqlGenerator) {
        if (databaseType == null) {
            throw new DbException("databaseType is null.");
        }
        if (sqlGenerator == null) {
            throw new DbException("sqlGenerator is null.");
        }
        RESOURCES_MAP.put(databaseType, sqlGenerator);
    }

    /**
     * 解绑
     *
     * @param databaseType
     */
    public static void unbind(DatabaseType databaseType) {
        if (databaseType == null) {
            return;
        }
        RESOURCES_MAP.remove(databaseType);
    }

    /**
     * 查找
     *
     * @param databaseType
     * @return
     */
    public static SqlGenerator lookup(DatabaseType databaseType) {
        SqlGenerator resource = RESOURCES_MAP.get(databaseType);
        return databaseType == null ? null : (SqlGenerator) ObjectUtils.clone(resource);
    }

    /**
     * 列出所有的资源
     *
     * @return
     */
    public static Map<DatabaseType, SqlGenerator> list() {
        return Collections.unmodifiableMap(RESOURCES_MAP);
    }
}
