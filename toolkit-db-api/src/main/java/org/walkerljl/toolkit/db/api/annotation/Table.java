package org.walkerljl.toolkit.db.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注一个POJO为数据库对应的表
 *
 * @author lijunlin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {

    /**
     * 表名
     *
     * @return
     */
    String value() default "";

    /**
     * 是否分片，true：分片，默认：false
     */
    boolean sharded() default false;

    /**
     * 备注
     *
     * @return
     */
    String comment() default "";
}