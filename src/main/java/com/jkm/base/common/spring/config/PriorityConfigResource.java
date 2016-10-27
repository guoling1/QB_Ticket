package com.jkm.base.common.spring.config;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by hutao on 15/8/20.
 * 下午3:43
 */
@Data
@AllArgsConstructor
public class PriorityConfigResource implements RefreshableConfigResource {
    private int priority;
    private ConfigResource configResource;

    /**
     *
     */
    public PriorityConfigResource() {
        //DO nothing
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        configResource.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Pair<String, String>> refresh() {
        return configResource instanceof RefreshableConfigResource ?
                ((RefreshableConfigResource) configResource).refresh() :
                Lists.<Pair<String, String>>newArrayList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(final String key) {
        return configResource.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(final String key) {
        return configResource.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(final String key, final String defaultValue) {
        return configResource.get(key, defaultValue);
    }
}
