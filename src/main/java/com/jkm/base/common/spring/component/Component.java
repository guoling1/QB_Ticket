package com.jkm.base.common.spring.component;

/**
 * Created by hutao on 15/8/24.
 * 上午10:30
 */
public interface Component {
    /**
     * 启动
     */
    void init();

    /**
     * 关闭
     */
    void shutdown();
}
