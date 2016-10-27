package com.jkm.base.common.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hutao on 15/8/20.
 * 下午3:47
 */
@Component
public class PropertyConfigResource implements ConfigResource {
    @Autowired
    private ConfigurationUtils configurationUtils;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        //DO nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(final String key) {
        return ConfigurationUtils.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(final String key) {
        return ConfigurationUtils.getString(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(final String key, final String defaultValue) {
        return ConfigurationUtils.getString(key, defaultValue);
    }
}
