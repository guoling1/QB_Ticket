package com.jkm.service.hy.helper.serialize.converter;

import com.google.common.base.Optional;

import java.lang.reflect.Field;

/**
 * Created by yulong.zhang on 2016/9/29.
 */
public interface ConverterLookUp {

    /**
     * 查找转换器
     *
     * @param field
     * @return
     */
    Optional<HySdkSerializeConverter> lookUp(Field field);
}
