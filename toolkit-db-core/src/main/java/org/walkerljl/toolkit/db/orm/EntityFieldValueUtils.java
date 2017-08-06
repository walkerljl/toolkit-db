package org.walkerljl.toolkit.db.orm;

import org.walkerljl.toolkit.db.orm.entity.Column;
import org.walkerljl.toolkit.db.orm.entity.Table;
import org.walkerljl.toolkit.db.orm.parse.TableManager;
import org.walkerljl.toolkit.lang.ReflectUtils;
import org.walkerljl.toolkit.standard.exception.AppException;

/**
 *
 * EntityFieldValueUtils
 *
 * @author lijunlin
 */
public class EntityFieldValueUtils {
	
	/**
	 * 获取字段值
	 * @param entity
	 * @return
	 */
	public static Object[] getFieldValues(Object entity) {
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null) {
			return null;
		}
		int columnsSize = table.getColumns().size();
		if (columnsSize == 0) {
			return null;
		}
		
		Object[] fieldValues = new Object[columnsSize];
		for (int i = 0; i < columnsSize; i++) {
			fieldValues[i] = getFieldValue(entity, table.getColumns().get(i));
		}
		return fieldValues;
	}
	
	/**
	 * 获取指定字段值
	 * @param entity
	 * @param column
	 * @return
	 */
	public static Object getFieldValue(Object entity, Column column) {
		if (column == null) {
			return null;
		}
		try {
			return ReflectUtils.invoke(entity, column.getGetterName());
		} catch (Exception e) {
			throw new AppException(e);
		}
	}
}