package com.jkm.common.third.channel.ys.service.helper.serialize.converter;

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
    Optional<YsSdkSerializeConverter> lookUp(Field field);
}
