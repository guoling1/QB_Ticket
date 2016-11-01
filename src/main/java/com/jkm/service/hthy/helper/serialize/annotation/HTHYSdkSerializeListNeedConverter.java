package com.jkm.service.hthy.helper.serialize.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yulong.zhang on 2016/10/31.
 *
 * 针对filed是list的属性：
 * 如果是基本数据类型集合，直接读；
 * 如果是引用类型集合，转化；
 *
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HTHYSdkSerializeListNeedConverter {
}
