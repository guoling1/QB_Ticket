package com.jkm.service.hy.helper.serialize.converter;

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

    private static final ImmutableMap<Class, HySdkSerializeConverter> converter_map;
    static {
        converter_map = ImmutableMap.<Class, HySdkSerializeConverter>builder()
                .put(Integer.class, new HySdkWrapConverter(new IntConverter()))
                .put(int.class, new HySdkWrapConverter(new IntConverter()))
                .put(Double.class, new HySdkDoubleConverter())
                .put(double.class, new HySdkDoubleConverter())
                .put(Float.class, new HySdkWrapConverter(new FloatConverter()))
                .put(float.class, new HySdkWrapConverter(new FloatConverter()))
                .put(Long.class, new HySdkWrapConverter(new LongConverter()))
                .put(long.class, new HySdkWrapConverter(new LongConverter()))
                .put(BigDecimal.class, new HySdkWrapConverter(new BigDecimalConverter()))
                .put(Date.class, new HySdkDateConverter())
                .put(String.class, new HySdkWrapConverter(new StringConverter()))
                .put(boolean.class, new HySdkWrapConverter(new BooleanConverter()))
                .build();
    }

    /**
     * {@inheritDoc}
     *
     * @param field
     * @return
     */
    @Override
    public Optional<HySdkSerializeConverter> lookUp(Field field) {
        return Optional.fromNullable(converter_map.get(field.getType()));
    }
}
