/*
 * Copyright (c) 2010-2015 www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.toolkit.db.orm.parse;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.walkerljl.toolkit.db.orm.entity.Table;
import org.walkerljl.toolkit.cache.Cache;
import org.walkerljl.toolkit.cache.LRUCache;
import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;

/**
 * TableManager 
 *
 * @author lijunlin
 */
public class TableManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TableManager.class);

	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();

	private final Cache<Class<?>, Table> tableCache = new LRUCache<Class<?>, Table>(100);
	
	private TableManager() {}
	
	public static TableManager getInstance() {
		return TableManagerHolder.instance;
	}
	
	public Table getTable(Class<?> entityClass) {
		readLock.lock();
		Table table = null;
		try {
			table = tableCache.get(entityClass);
			if (table == null) {
				readLock.unlock();
				writeLock.lock();
				try {
					if (table == null) {
						table = TableParser.parse(entityClass);
						if (table != null) {
							tableCache.put(entityClass, table);
						}
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(String.format("Parse entity:%s", entityClass));
						}
					}
				} finally {
					writeLock.unlock();
					readLock.lock();
				}
			}
		} finally {
			readLock.unlock();
		}
		return table;
	}
	
	private static class TableManagerHolder {
		private static TableManager instance = new TableManager();
	}
}