package com.jkm.base.common.spring.config;

/**
 * Created by hutao on 15/8/21.
 * 下午1:33
 */
public class NullConfigEventFilter implements ConfigEventFilter {
    /**
     * 不做过滤
     *
     * @param event
     * @return
     */
    @Override
    public boolean filter(final ConfigChangeEvent event) {
        return false;
    }
}
