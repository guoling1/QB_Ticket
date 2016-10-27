package com.jkm.base.common.spring.config;

import lombok.Data;

/**
 * Created by hutao on 15/8/21.
 * 下午12:04
 */
@Data
public class ConfigChangeEvent {
    private final String key;
    private final String value;
}
