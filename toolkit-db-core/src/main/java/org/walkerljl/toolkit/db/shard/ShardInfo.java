package org.walkerljl.toolkit.db.shard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 分片信息
 *
 * @author: lijunlin
 */
public class ShardInfo implements Serializable {

    /**
     * Master数据源列表
     */
    private List<DataSource> masterDataSources;
    /**
     * Salve数据源列表
     */
    private List<DataSource> salveDataSources;
    /**
     * 表名前缀和数量映射
     */
    private Map<String, Integer> tableNamePrefixAndQuantitiesMapp;

    public ShardInfo() {}

    public ShardInfo(List<DataSource> masterDataSources, List<DataSource> salveDataSources,
                     Map<String, Integer> tableNamePrefixAndQuantitiesMapp) {
        this.masterDataSources = masterDataSources;
        this.salveDataSources = salveDataSources;
        this.tableNamePrefixAndQuantitiesMapp = tableNamePrefixAndQuantitiesMapp;
    }

    public int getAllTableQuantities(String tableNamePrefix) {
        return tableNamePrefixAndQuantitiesMapp.get(tableNamePrefix) * getDbQuantities();
    }

    public int getDbQuantities() {
        return masterDataSources.size();
    }

    public List<DataSource> getMasterDataSources() {
        return masterDataSources;
    }

    public void setMasterDataSources(List<DataSource> masterDataSources) {
        this.masterDataSources = masterDataSources;
    }

    public List<DataSource> getSalveDataSources() {
        return salveDataSources;
    }

    public void setSalveDataSources(List<DataSource> salveDataSources) {
        this.salveDataSources = salveDataSources;
    }

    public Map<String, Integer> getTableNamePrefixAndQuantitiesMapp() {
        return tableNamePrefixAndQuantitiesMapp;
    }

    public void setTableNamePrefixAndQuantitiesMapp(Map<String, Integer> tableNamePrefixAndQuantitiesMapp) {
        this.tableNamePrefixAndQuantitiesMapp = tableNamePrefixAndQuantitiesMapp;
    }
}
