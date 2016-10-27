package com.jkm.base.common.spring.config;


/**
 * Created by hutao on 15/8/20.
 * 下午3:43
 */
public interface ConfigListener {
    /**
     * 处理config name的key值变化
     *
     * @param event
     */
    void onConfigChange(final ConfigChangeEvent event);
}
