package org.walkerljl.toolkit.db.transaction;

import org.walkerljl.toolkit.standard.exception.AppException;
import org.walkerljl.toolkit.standard.exception.ErrorCode;

/**
 * 事务异常
 * 
 * @author lijunlin
 */
public class TransactionException extends AppException {

	private static final long serialVersionUID = -6786549876849535944L;

	/**
	 * 默认构造函数
	 */
	public TransactionException() {
		super();
	}

	/**
	 * 构造函数
	 *
	 * @param message 异常消息
	 */
	public TransactionException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 *
	 * @param e 异常对象
	 */
	public TransactionException(Throwable e) {
		super(e);
	}

	/**
	 * 构造函数
	 *
	 * @param code 异常码
	 */
	public TransactionException(ErrorCode code) {
		super(code.getDescription());
		this.code = code;
	}

	/**
	 * 构造函数
	 *
	 * @param code 异常码
	 * @param message 异常消息
	 */
	public TransactionException(ErrorCode code, String message) {
		super(code, message);
	}

	/**
	 * 构造函数
	 *
	 * @param message 异常消息
	 * @param e 异常对象
	 */
	public TransactionException(String message, Throwable e) {
		super(message, e);
	}

	/**
	 * 构造函数
	 *
	 * @param code 异常码
	 * @param message 异常消息
	 * @param e 异常对象
	 */
	public TransactionException(ErrorCode code, String message, Throwable e) {
		super(code, message, e);
	}
}