package org.walkerljl.toolkit.db.shard.parse.support;

import java.util.List;

import javax.sql.DataSource;

import org.walkerljl.toolkit.db.DbException;
import org.walkerljl.toolkit.db.api.hash.Hashing;
import org.walkerljl.toolkit.db.orm.EntityFieldValueUtils;
import org.walkerljl.toolkit.db.orm.TableInfoManager;
import org.walkerljl.toolkit.db.orm.entity.ColumnInfo;
import org.walkerljl.toolkit.db.orm.entity.TableInfo;
import org.walkerljl.toolkit.db.shard.ShardInfo;
import org.walkerljl.toolkit.db.shard.parse.ShardParser;
import org.walkerljl.toolkit.lang.CollectionUtils;
import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;

/**
 * DefaultShardParser
 *
 * @author: lijunlin
 */
public class DefaultShardParser implements ShardParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShardParser.class);

    private ShardInfo shardInfo;
    private Hashing   hashing;

    @Override
    public DataSource parseMasterDataSource(Object entity) {
        if (entity == null) {
            throw new DbException("Entity is null.");
        }
        DataSource dataSource = shardInfo.getMasterDataSources().get(getDbIndex(entity));
        return dataSource;
    }

    @Override
    public DataSource parseSalveDataSource(Object entity) {
        if (entity == null) {
            throw new DbException("Entity is null.");
        }
        DataSource dataSource = shardInfo.getSalveDataSources().get(getDbIndex(entity));
        return dataSource;
    }

    private String getShardedKey(Object entity, List<ColumnInfo> shardedColumnInfos) {
        if (CollectionUtils.isEmpty(shardedColumnInfos)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (ColumnInfo shardedColumnInfo : shardedColumnInfos) {
            Object fieldValue = EntityFieldValueUtils.getFieldValue(entity, shardedColumnInfo);
            if (fieldValue != null) {
                stringBuilder.append(fieldValue);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String parseTableName(Object entity) {
        if (entity == null) {
            throw new DbException("Entity is null.");
        }
        TableInfo tableInfo = TableInfoManager.getInstance().getTable(entity.getClass());
        if (tableInfo == null || !tableInfo.isSharded()) {
            return null;
        }
        int index = hashing.indexFor(getShardedKey(entity, tableInfo.getSharedColumnInfos()),
                shardInfo.getAllTableQuantities(tableInfo.getName()));
        long tableIndex = index / shardInfo.getDbQuantities() + 1;//获取表序号(1-tableCount)
        return tableInfo.getName() + tableIndex;
    }

    @Override
    public void setShardInfo(ShardInfo shardInfo) {
        if (shardInfo == null) {
            throw new DbException("shardInfo is null.");
        }
        this.shardInfo = shardInfo;
    }

    @Override
    public void setHashing(Hashing hashing) {
        this.hashing = hashing;
    }

    private int getDbIndex(Object entity) {
        TableInfo tableInfo = TableInfoManager.getInstance().getTable(entity.getClass());
        if (tableInfo == null || !tableInfo.isSharded()) {
            return -1;
        }
        int index = hashing.indexFor(getShardedKey(entity, tableInfo.getSharedColumnInfos()),
                shardInfo.getAllTableQuantities(tableInfo.getName()));
        return index % shardInfo.getDbQuantities();
    }
}
