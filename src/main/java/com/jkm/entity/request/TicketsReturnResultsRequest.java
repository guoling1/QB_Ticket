package com.jkm.entity.request;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/25.
 */
@Data
public class TicketsReturnResultsRequest {
    /**
     * 请求流水号
     */
    private String termTransID;

    /**
     * 平台交易流水
     */
    private String transID;

    /**
     * 退票时间， 值如 "2016-03-18 12:12:12"
     */
    private Date refundTime;

    /**
     * 退票方式，
     * 1.线上退票退款
     * 2.车站退票退款
     * 3.出票失败退款
     * 4.其它原因退款
     */
    private String refundType;

    /**
     * 出票乘客列表， JSON格式，
     */
    private Map<String, String> passengers;



}
