package com.jkm.service.ys.helper.serialize.converter;

import com.thoughtworks.xstream.converters.SingleValueConverter;

import java.lang.reflect.Field;

/**
 * Created by yulong.zhang on 2016/9/29.
 *
 * 封装基本的converter,例如：com.thoughtworks.xstream.converters.basic.IntConverter
 */
public class YsSdkWrapConverter extends AbstractYsSdkSerializeConverter {

    private final SingleValueConverter converter;

    public YsSdkWrapConverter(final SingleValueConverter converter) {
        this.converter = converter;
    }

    /**
     * {@inheritDoc}
     *
     * @param field
     * @return
     */
    @Override
    public boolean canConvert(Field field) {
        return converter.canConvert(field.getType());
    }

    /**
     * {@inheritDoc}
     *
     * @param field
     * @param target
     * @return
     */
    @Override
    public String toString(Field field, Object target) {
        return converter.toString(getFieldValue(field, target));
    }

    /**
     * {@inheritDoc}
     *
     * @param field
     * @param str
     * @return
     */
    @Override
    public Object fromString(Field field, String str) {
        return converter.fromString(str);
    }
}
