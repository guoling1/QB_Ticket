package com.jkm.service.ys.helper.serialize;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.jkm.service.ys.helper.serialize.annotation.YsSdkAnnotationSupporter;
import com.jkm.service.ys.helper.serialize.annotation.YsSdkSerializeNoInclude;
import com.jkm.service.ys.helper.serialize.converter.ConverterLookUp;
import com.jkm.service.ys.helper.serialize.converter.DefaultConverterLookUp;
import com.jkm.service.ys.helper.serialize.converter.YsSdkSerializeConverter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/9/29.
 */
@UtilityClass
@Slf4j
public class YsSdkSerializeUtil {

    private static final ConverterLookUp converterLookUp = new DefaultConverterLookUp();



    /**
     * object对象转成map
     *
     * @param object
     * @return
     */
    public static <T> Map<String, String> converterObjectToMap(final T object) {
        return convertObjectToMapImpl(object);
    }


    private static <T> Map<String, String> convertObjectToMapImpl(final T object) {

        final List<Field> fields = FieldUtils.getAllFieldsList(object.getClass());
        final Map<String, String> result = new HashMap<>();
        for(Field field : fields) {
            if (!field.isAnnotationPresent(YsSdkSerializeNoInclude.class)) {
                final String key = YsSdkAnnotationSupporter.getFieldAlias(field);
                if (List.class.equals(field.getType())) {
                    result.put(key, getFieldValue(field, object).toString());
                    System.out.print(getFieldValue(field, object).toString());
                    continue;
                }
                final Optional<YsSdkSerializeConverter> converterOptional = converterLookUp.lookUp(field);
                Preconditions.checkState(converterOptional.isPresent(),  "could not convert field type[%s]", field.getType());
                final String value = converterOptional.get().toString(field, object);
                if (null != value) {
                    result.put(key, value);
                } else {
//                    Preconditions.checkState(!field.isAnnotationPresent(YxtSdkSerializeNoNull.class),
//                            "field[%s] could not be null", field.getName());
                }
            }
        }
        return result;
    }


    private static Object getFieldValue(final Field field,
                                         final Object target) {
        try {
            return FieldUtils.readField(field, target, true);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}
