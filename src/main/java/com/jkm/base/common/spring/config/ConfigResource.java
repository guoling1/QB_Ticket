package com.jkm.base.common.spring.config;

/**
 * Created by hutao on 15/8/20.
 * 下午3:43
 */
public interface ConfigResource {
    /**
     * 初始化
     */
    void init();

    /**
     * 是否有key
     *
     * @param key
     * @return
     */
    boolean containsKey(final String key);

    /**
     * 获取
     *
     * @param key
     * @return
     */
    String get(final String key);

    /**
     * 获取
     *
     * @param key
     * @param defaultValue
     * @return
     */
    String get(final String key, final String defaultValue);
}
