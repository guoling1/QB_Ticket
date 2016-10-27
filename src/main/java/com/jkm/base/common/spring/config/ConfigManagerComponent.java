package com.jkm.base.common.spring.config;

import com.jkm.base.common.spring.component.RefreshableComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hutao on 15/8/24.
 * 上午11:12
 */
@Component
public class ConfigManagerComponent implements RefreshableComponent {
    @Autowired
    private ConfigManager configManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh() {
        ConfigManager.refresh();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        ConfigManager.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        //DO nothing
    }
}
