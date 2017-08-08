package org.walkerljl.toolkit.db.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列
 *
 * @author lijunlin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {

    /**
     * 字段名
     *
     * @return
     */
    String value() default "";

    /**
     * 是否为主键
     *
     * @return
     */
    boolean key() default false;

    /**
     * 是否分片字段，true：分片，默认：false
     *
     * @return
     */
    boolean sharded() default false;

    /**
     * 分片顺序
     *
     * @return
     */
    int shardedOrder() default 0;

    /**
     * 备注
     *
     * @return
     */
    String comment() default "";
}