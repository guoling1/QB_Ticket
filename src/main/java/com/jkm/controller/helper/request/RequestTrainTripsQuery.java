package com.jkm.controller.helper.request;

import lombok.Data;

/**
 * Created by zhangbin on 2016/10/28.
 */
@Data
public class RequestTrainTripsQuery {
    /**
     * 出发站点。车站站点响应的
     * code 属性 值如"beijing"
     */
    private String departStationCode;

    /**
     * 目的站点。车站站点响应的code属性
     * 值如"nanjing"
     */
    private String arriveStationCode;

    /**
     * 出发日期。 格式： YYYY-MM-DD
     * 值如"2016-03-18"
     */
    private String departDate;
}
