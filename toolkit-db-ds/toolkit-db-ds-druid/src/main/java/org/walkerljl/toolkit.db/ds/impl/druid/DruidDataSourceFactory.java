package org.walkerljl.toolkit.db.ds.impl.druid;

import com.alibaba.druid.pool.DruidDataSource;

import org.walkerljl.toolkit.db.ds.abstracts.AbstractDataSourceFactory;
import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;

import java.sql.SQLException;

/**
 *
 * 阿里巴巴 Druid 数据源
 *
 * @author lijunlin
 */
public class DruidDataSourceFactory extends AbstractDataSourceFactory<DruidDataSource> {

	private static final Logger LOG = LoggerFactory.getLogger(DruidDataSourceFactory.class);
	
	@Override
	public DruidDataSource createDataSource() {
		return new DruidDataSource();
	}

	@Override
	public void setDriver(DruidDataSource ds, String driver) {
		ds.setDriverClassName(driver);
	}

	@Override
	public void setUrl(DruidDataSource ds, String url) {
		ds.setUrl(url);
	}

	@Override
	public void setUsername(DruidDataSource ds, String username) {
		ds.setUsername(username);
	}

	@Override
	public void setPassword(DruidDataSource ds, String password) {
		ds.setPassword(password);
	}

	@Override
	public void setAdvancedConfig(DruidDataSource ds) {
		try {
            ds.setFilters("stat");
        } catch (SQLException e) {
        	LOG.error("错误：设置 Stat Filter 失败！", e);
        }
	}
}