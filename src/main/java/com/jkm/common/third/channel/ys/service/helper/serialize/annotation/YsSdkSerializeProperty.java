package com.jkm.common.third.channel.ys.service.helper.serialize.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yulong.zhang on 2016/9/29.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface YsSdkSerializeProperty {
    /**
     * 日期格式化
     *
     * @return
     */
    String datePattern() default "yyyy-MM-dd HH:mm:ss";

    /**
     * double 精度
     *
     * @return
     */
    String doublePattern() default "";

    /**
     * 是否需要加密
     *
     * @return
     */
    boolean needEncrypt() default false;
}
