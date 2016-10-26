package com.jkm.entity.request;


import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/25.
 */
@Data
public class TrainBookingResultsRequest {
    /**
     * 请求流水号
     */
    private String termTransID;

    /**
     * 平台交易流水
     */
    private String transID;

    /**
     * 车次号。 值如 "G101"
     */
    private String trainNo;

    /**
     *出发站点Code，值如 "beijingbei"
     */
    private String departStationCode;

    /**
     * 目的站点Code，值如 "nanjingnan"
     */
    private String arriveStationCode;

    /**
     *出发日期，值如 "2016-05-18 07:00"
     */
    private Date departDateTime;

    /**
     * 到达时间，值如 "2016-05-18 11:14"
     */
    private Date arriveDateTime;

    /**
     *取票号，值如 "EA12345678"
     */
    private String pickNo;

    /**
     * 实扣保险费
     */
    private int insuranceFee;

    /**
     * 实扣技术服务费
     */
    private int technicalFee;

    /**
     * 出票乘客列表， JSON格式，
     */
    private Map<String ,String> passengers;









}
