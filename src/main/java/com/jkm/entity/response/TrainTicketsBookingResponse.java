package com.jkm.entity.response;

import lombok.Data;

/**
 * Created by zhangbin on 2016/10/25.
 */
@Data
public class TrainTicketsBookingResponse {
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
