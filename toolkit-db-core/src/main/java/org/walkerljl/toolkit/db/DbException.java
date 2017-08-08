package org.walkerljl.toolkit.db;

import org.walkerljl.toolkit.standard.exception.AppException;

/**
 * 数据库异常
 *
 * @author: lijunlin
 */
public class DbException extends AppException {

    /**
     * 默认构造函数
     */
    public DbException() {
        super();
    }

    /**
     * 构造函数
     *
     * @param message
     */
    public DbException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param e
     */
    public DbException(Throwable e) {
        super(e);
    }

    /**
     * 构造函数
     *
     * @param msg
     * @param e
     */
    public DbException(String msg, Throwable e) {
        super(msg, e);
    }
}
