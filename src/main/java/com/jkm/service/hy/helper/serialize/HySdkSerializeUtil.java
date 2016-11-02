package com.jkm.service.hy.helper.serialize;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.jkm.service.hy.entity.HySdkRequest;
import com.jkm.service.hy.entity.HySdkResponse;
import com.jkm.service.hy.helper.serialize.annotation.HySdkAnnotationSupporter;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeListNeedConverter;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeNoInclude;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeNoNull;
import com.jkm.service.hy.helper.serialize.converter.ConverterLookUp;
import com.jkm.service.hy.helper.serialize.converter.DefaultConverterLookUp;
import com.jkm.service.hy.helper.serialize.converter.HySdkSerializeConverter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/1.
 */
@UtilityClass
public class HySdkSerializeUtil {

    private static final ConverterLookUp converterLookUp = new DefaultConverterLookUp();


    /**
     * request对象转成map
     *
     * @param request
     * @return
     */
    public static Map<String, String> converterSdkRequestToMap(final HySdkRequest request) {
        return convertObjectToMapImpl(request);
    }

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
            if (!field.isAnnotationPresent(HySdkSerializeNoInclude.class)) {
                final String key = HySdkAnnotationSupporter.getFieldAlias(field);
                if (List.class.equals(field.getType())) {
                    if (!field.isAnnotationPresent(HySdkSerializeListNeedConverter.class)) {
                        result.put(key, getFieldValue(field, object).toString());
                        continue;
                    } else {
//                        fieldList.add(field);
                        continue;
                    }
                }
                final Optional<HySdkSerializeConverter> converterOptional = converterLookUp.lookUp(field);
                Preconditions.checkState(converterOptional.isPresent(),  "could not convert field type[%s]", field.getType());
                final String value = converterOptional.get().toString(field, object);
                if (null != value) {
                    result.put(key, value);
                } else {
                    Preconditions.checkState(!field.isAnnotationPresent(HySdkSerializeNoNull.class),
                            "field[%s] could not be null", field.getName());
                }
            }
        }
        return result;
    }


    /**
     * map对象转成T
     *
     * @param map
     * @param target
     * @param <T>
     * @return
     */
    public static <T extends HySdkResponse> T convertMapToResponse(final Map<String, String> map, final T target) {
        final List<Field> fields = FieldUtils.getAllFieldsList(target.getClass());
        for (Field field : fields) {
            if (!field.isAnnotationPresent(HySdkSerializeNoInclude.class)) {
                final String key = HySdkAnnotationSupporter.getFieldAlias(field);
                if (map.containsKey(key)) {
                    final Optional<HySdkSerializeConverter> converterOptional = converterLookUp.lookUp(field);
                    Preconditions.checkState(converterOptional.isPresent(),
                            "could not convert field type[%s]", field.getType());
                    final Object value = converterOptional.get().fromString(field, map.get(key));
                    HySdkAnnotationSupporter.writeField(field, target, value);
                } else {
                    Preconditions.checkState(!field.isAnnotationPresent(HySdkSerializeNoNull.class),
                            "field[%s] could not be null", field.getName());
                }
            }
        }
        return target;
    }


    /**
     * JsonObject对象转成T
     *
     * @param jo
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T convertJsonObjectToResponse(final JSONObject jo, final T target) {
        final List<Field> fields = FieldUtils.getAllFieldsList(target.getClass());
        for (Field field : fields) {
            if (!field.isAnnotationPresent(HySdkSerializeNoInclude.class)) {
                final String key = HySdkAnnotationSupporter.getFieldAlias(field);
                if (jo.containsKey(key)) {
                    if (field.isAnnotationPresent(HySdkSerializeListNeedConverter.class)) {
                        continue;
                    }
                    final Optional<HySdkSerializeConverter> converterOptional = converterLookUp.lookUp(field);
                    Preconditions.checkState(converterOptional.isPresent(),
                            "could not convert field type[%s]", field.getType());
                    final Object value = converterOptional.get().fromString(field, jo.getString(key));
                    HySdkAnnotationSupporter.writeField(field, target, value);
                } else {
                    Preconditions.checkState(!field.isAnnotationPresent(HySdkSerializeNoNull.class),
                            "field[%s] could not be null", field.getName());
                }
            }
        }
        return target;
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
