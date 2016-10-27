package com.jkm.base.common.spring.config;

import java.util.Objects;

/**
 * Created by hutao on 15/8/21.
 * 下午1:33
 */
public class SingleConfigEventFilter implements ConfigEventFilter {
    private final String key;

    /**
     * @param key
     */
    public SingleConfigEventFilter(final String key) {
        this.key = key;
    }

    /**
     * 不做过滤
     *
     * @param event
     * @return
     */
    @Override
    public boolean filter(final ConfigChangeEvent event) {
        return !Objects.equals(event.getKey(), key);
    }
}
