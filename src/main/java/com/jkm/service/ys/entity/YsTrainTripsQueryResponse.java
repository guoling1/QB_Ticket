package com.jkm.service.ys.entity;

import lombok.Data;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/25.
 */
@Data
public class YsTrainTripsQueryResponse {
    /**
     * 响应时间，格式： YYYYMMDDHHMMSS
     */
    private String srvDateTime;

    /**
     * 火车站站点信息 json格式
     */
    private Map<String,String> data;
}
