package com.jkm.base.common.spring.config;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hutao on 15/8/20.
 * 下午3:47
 */
@Component
public class MapConfigResource implements ConfigResource {
    @Setter
    private Map<String, String> map = new HashMap<>();

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
        return map.containsKey(key);
    }

    /**
     * 添加
     *
     * @param key
     * @param value
     */
    public MapConfigResource add(final String key, final String value) {
        map.put(key, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(final String key) {
        return map.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(final String key, final String defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }
}
