package com.jkm.service.ys.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2016/10/25.
 *
 * 火车车票订票受理接口  -- 响应参数
 */
@Data
public class TrainTicketsBookingResponse {

    /**
     *  状态: "1004"
     *
     *  {@link com.jkm.enums.EnumErrorCode}
     */
    private String status;

    /**
     * "msg": "交易受理成功",
     */
    private String msg;

    /**
     * 签名
     */
    private String sign;

    /**
     *响应时间，格式： YYYYMMDDHHMMSS
     */
    private String srvDateTime;

    /**
     * 请求流水号
     */
    private String termTransID;

    /**
     * 响应流水
     */
    private String transID;

}
