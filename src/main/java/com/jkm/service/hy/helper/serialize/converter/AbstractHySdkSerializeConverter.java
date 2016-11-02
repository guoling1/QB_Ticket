package com.jkm.service.hy.helper.serialize.converter;

import com.google.common.base.Throwables;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * Created by yulong.zhang on 2016/9/29.
 */
public abstract class AbstractHySdkSerializeConverter implements HySdkSerializeConverter {

    @Override
    public String toString(final Field field,
                           final Object target) {
        return getFieldValue(field, target).toString();
    }

    /**
     * 从target读取field
     *
     * @param field
     * @param target
     * @return
     */
    protected final Object getFieldValue(final Field field,
                                         final Object target) {
        try {
            return FieldUtils.readField(field, target, true);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}

