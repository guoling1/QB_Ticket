package com.jkm.service.ys.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/25.
 */
@Data
public class YsTrainStationQueryRequest {
    /**
     * 请求流水号(接入商流水)
     * 合法字符[a-z A-Z 0-9]
     * 最少16位，最大32位
     * 用factoryID+定长的序列，请保持唯一
     */
    private String termTransID;

    private Map<String, List<Object>> data;
}
