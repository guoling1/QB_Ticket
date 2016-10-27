package com.jkm.service.ys.helper.serialize.converter;

import com.google.common.base.Strings;
import com.jkm.service.ys.helper.serialize.annotation.YsSdkSerializeProperty;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * Created by yulong.zhang on 2016/9/29.
 */
public class YsSdkDoubleConverter extends AbstractYsSdkSerializeConverter {

    final SingleValueConverter converter = new DoubleConverter();


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
        final YsSdkSerializeProperty annotation = field.getAnnotation(YsSdkSerializeProperty.class);
        if (null != annotation) {
            final String doublePattern = annotation.doublePattern();
            if (!Strings.isNullOrEmpty(doublePattern)) {
                return new DecimalFormat(doublePattern).format(getFieldValue(field, target));
            }
        }
        return super.toString(field, target);
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
        final YsSdkSerializeProperty annotation = field.getAnnotation(YsSdkSerializeProperty.class);
        if (null != annotation) {
            final String doublePattern = annotation.doublePattern();
            if (!Strings.isNullOrEmpty(doublePattern)) {
                return converter.fromString(new DecimalFormat(doublePattern).format(converter.fromString(str)));
            }
        }
        return converter.fromString(str);
    }
}
