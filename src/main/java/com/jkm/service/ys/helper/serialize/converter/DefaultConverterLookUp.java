package com.jkm.service.ys.helper.serialize.converter;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.xstream.converters.basic.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/9/29.
 */
public class DefaultConverterLookUp implements ConverterLookUp {

    private static final ImmutableMap<Class, YsSdkSerializeConverter> converter_map;
    static {
        converter_map = ImmutableMap.<Class, YsSdkSerializeConverter>builder()
                .put(Integer.class, new YsSdkWrapConverter(new IntConverter()))
                .put(int.class, new YsSdkWrapConverter(new IntConverter()))
                .put(Double.class, new YsSdkDoubleConverter())
                .put(double.class, new YsSdkDoubleConverter())
                .put(Float.class, new YsSdkWrapConverter(new FloatConverter()))
                .put(float.class, new YsSdkWrapConverter(new FloatConverter()))
                .put(Long.class, new YsSdkWrapConverter(new LongConverter()))
                .put(long.class, new YsSdkWrapConverter(new LongConverter()))
                .put(BigDecimal.class, new YsSdkWrapConverter(new BigDecimalConverter()))
                .put(Date.class, new YsSdkDateConverter())
                .put(String.class, new YsSdkWrapConverter(new StringConverter()))
                .put(boolean.class, new YsSdkWrapConverter(new BooleanConverter()))
                .build();
    }

    /**
     * {@inheritDoc}
     *
     * @param field
     * @return
     */
    @Override
    public Optional<YsSdkSerializeConverter> lookUp(Field field) {
        return Optional.fromNullable(converter_map.get(field.getType()));
    }
}
