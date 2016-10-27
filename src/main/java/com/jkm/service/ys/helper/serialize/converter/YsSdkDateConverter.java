package com.jkm.service.ys.helper.serialize.converter;

import com.google.common.base.Strings;
import com.jkm.service.ys.helper.serialize.annotation.YsSdkSerializeProperty;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/9/29.
 */
public class YsSdkDateConverter extends AbstractYsSdkSerializeConverter {

    private final static String DEFAULT_DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final static DateTimeFormatter DEFAULT_DATA_FORMATTER = DateTimeFormat.forPattern(DEFAULT_DATA_PATTERN);


    /**
     * {@inheritDoc}
     *
     * @param field
     * @return
     */
    @Override
    public boolean canConvert(Field field) {
        return Date.class == field.getType();
    }

    /**
     * {@inheritDoc}
     *
     * @param field
     * @param target
     * @return
     */
    public String toString(Field field, Object target) {
        final Object fieldValue = getFieldValue(field, target);
        if (null == fieldValue) {
            return  null;
        }
        final YsSdkSerializeProperty annotation = field.getAnnotation(YsSdkSerializeProperty.class);
        if (null != annotation) {
            final String datePattern = annotation.datePattern();
            if (!Strings.isNullOrEmpty(datePattern)) {
                return new DateTime(fieldValue).toString(DateTimeFormat.forPattern(datePattern));
            }
        }
        return new DateTime(fieldValue).toString(DEFAULT_DATA_FORMATTER);
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
            final String datePattern = annotation.datePattern();
            DateTimeFormat.forPattern(datePattern).parseDateTime(str).toDate();
        }
        return DEFAULT_DATA_FORMATTER.parseDateTime(str).toDate();
    }
}
