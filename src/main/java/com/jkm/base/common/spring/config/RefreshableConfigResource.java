package com.jkm.base.common.spring.config;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by hutao on 15/8/20.
 * 下午3:43
 */
public interface RefreshableConfigResource extends ConfigResource {
    /**
     * 重现载入
     *
     * @return 发生了变化的键值对
     */
    List<Pair<String, String>> refresh();
}
