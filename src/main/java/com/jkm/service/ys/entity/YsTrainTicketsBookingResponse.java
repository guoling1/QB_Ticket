package com.jkm.service.ys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zhangbin on 2016/10/25.
 *
 * 火车车票订票受理接口  -- 响应参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class YsTrainTicketsBookingResponse extends YsSdkResponse{

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
