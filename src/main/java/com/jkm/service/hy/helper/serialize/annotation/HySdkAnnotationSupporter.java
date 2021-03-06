package com.jkm.service.hy.helper.serialize.annotation;

import com.google.common.base.Throwables;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * Created by yulong.zhang on 2016/9/29.
 */
@UtilityClass
public class HySdkAnnotationSupporter {

    public static String getFieldAlias(final Field field) {
        final HySdkSerializeAlias annotation = field.getAnnotation(HySdkSerializeAlias.class);
        if (null == annotation) {
            return field.getName();
        }
        return annotation.name();
    }

    public static void writeField(final Field field,
                                  final Object target,
                                  final Object value) {
        try {
            FieldUtils.writeField(field, target, value, true);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}
