package com.jkm.base.common.spring.component;

import com.jkm.base.common.constant.TimeConstant;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutao on 15/8/24.
 * 上午11:15
 */
@org.springframework.stereotype.Component
@Slf4j
public class ComponentManager {
    @Setter
    private List<Component> componentList = new ArrayList<>();
    private List<Component> refreshComponents;

    /**
     * 组件初始化勾子
     */
    @PostConstruct
    public void init() {
        for (final Component component : componentList) {
            component.init();
        }
    }

    /**
     * 组件关闭勾子
     */
    @PreDestroy
    public void shutdown() {
        for (final Component component : componentList) {
            try {
                component.shutdown();
            } catch (Exception e) {
                log.error("shutdown component error:{}", e.getMessage(), e);
            }
        }
    }

    /**
     * 定式刷新所有需要刷新的组件
     */
    @Scheduled(initialDelay = TimeConstant.MINUTE, fixedDelay = 5 * TimeConstant.MINUTE)
    public void refresh() {
        if (refreshComponents == null) {
            refreshComponents = new ArrayList<>();
            for (final Component component : componentList) {
                if (component instanceof RefreshableComponent) {
                    refreshComponents.add(component);
                }
            }
        }

        for (final Component component : refreshComponents) {
            ((RefreshableComponent) component).refresh();
        }
    }
}
