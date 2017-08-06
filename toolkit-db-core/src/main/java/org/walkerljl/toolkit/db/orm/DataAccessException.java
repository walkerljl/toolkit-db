package org.walkerljl.toolkit.db.orm;

import org.walkerljl.toolkit.standard.exception.AppException;
import org.walkerljl.toolkit.standard.exception.ErrorCode;

/**
 * 数据访问异常
 * 
 * @author lijunlin
 */
public class DataAccessException extends AppException {

	private static final long serialVersionUID = -6786549876849535944L;

	/**
	 * 默认构造函数
	 */
	public DataAccessException() {
		super();
	}

	/**
	 * 构造函数
	 *
	 * @param message 异常消息
	 */
	public DataAccessException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 *
	 * @param e 异常对象
	 */
	public DataAccessException(Throwable e) {
		super(e);
	}

	/**
	 * 构造函数
	 *
	 * @param code 异常码
	 */
	public DataAccessException(ErrorCode code) {
		super(code.getDescription());
		this.code = code;
	}

	/**
	 * 构造函数
	 *
	 * @param code 异常码
	 * @param message 异常消息
	 */
	public DataAccessException(ErrorCode code, String message) {
		super(code, message);
	}

	/**
	 * 构造函数
	 *
	 * @param message 异常消息
	 * @param e 异常对象
	 */
	public DataAccessException(String message, Throwable e) {
		super(message, e);
	}

	/**
	 * 构造函数
	 *
	 * @param code 异常码
	 * @param message 异常消息
	 * @param e 异常对象
	 */
	public DataAccessException(ErrorCode code, String message, Throwable e) {
		super(code, message, e);
	}
}