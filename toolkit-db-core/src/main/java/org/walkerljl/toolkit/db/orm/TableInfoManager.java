/*
 * Copyright (c) 2010-2015 www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.toolkit.db.orm;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.walkerljl.toolkit.cache.Cache;
import org.walkerljl.toolkit.cache.LRUCache;
import org.walkerljl.toolkit.db.orm.entity.TableInfo;
import org.walkerljl.toolkit.db.orm.parse.TableInfoParser;
import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;

/**
 * TableInfoManager
 *
 * @author lijunlin
 */
public class TableInfoManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableInfoManager.class);

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private final Cache<Class<?>, TableInfo> tableCache = new LRUCache<Class<?>, TableInfo>(100);

    private TableInfoManager() {
    }

    public static TableInfoManager getInstance() {
        return TableManagerHolder.instance;
    }

    public TableInfo getTable(Class<?> entityClass) {
        readLock.lock();
        TableInfo tableInfo = null;
        try {
            tableInfo = tableCache.get(entityClass);
            if (tableInfo == null) {
                readLock.unlock();
                writeLock.lock();
                try {
                    if (tableInfo == null) {
                        tableInfo = TableInfoParser.parse(entityClass);
                        if (tableInfo != null) {
                            tableCache.put(entityClass, tableInfo);
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
        return tableInfo;
    }

    private static class TableManagerHolder {
        private static TableInfoManager instance = new TableInfoManager();
    }
}