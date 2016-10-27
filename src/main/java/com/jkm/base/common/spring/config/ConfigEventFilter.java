package com.jkm.base.common.spring.config;

/**
 * Created by hutao on 15/8/21.
 * 下午12:05
 */
public interface ConfigEventFilter {
    /**
     * 是否过滤事件
     *
     * @param event 事件
     * @return 返回true表示屏蔽，false表示不屏蔽
     */
    boolean filter(final ConfigChangeEvent event);
}
