package org.walkerljl.toolkit.db.shard.parse;

import javax.sql.DataSource;

import org.walkerljl.toolkit.db.api.hash.Hashing;
import org.walkerljl.toolkit.db.shard.ShardInfo;

/**
 * ShardParser
 *
 * @author lijunlin
 */
public interface ShardParser {

    DataSource parseMasterDataSource(Object entity);

    DataSource parseSalveDataSource(Object entity);

    String parseTableName(Object entity);

    void setShardInfo(ShardInfo shardInfo);

    void setHashing(Hashing hashing);
}
