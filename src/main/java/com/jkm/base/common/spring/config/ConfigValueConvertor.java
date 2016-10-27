package com.jkm.base.common.spring.config;

/**
 * Created by hutao on 15/8/24.
 * 下午2:45
 */

public interface ConfigValueConvertor<T> {
    /**
     * 转换string －》 T
     *
     * @param value
     * @return
     */
    T convert(final String value);
}
