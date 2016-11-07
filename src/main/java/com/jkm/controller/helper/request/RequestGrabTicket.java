package com.jkm.controller.helper.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-06.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestGrabTicket {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 出发站名称
     */
    private String fromStationName;

    /**
     * 出发站简码
     */
    private String fromStationCode;

    /**
     * 到达站名称
     */
    private String toStationName;

    /**
     * 到达站简码
     */
    private String toStationCode;

    /**
     * 抢票车次 (字符串拼接, 以,隔离)
     */
    private String trainCodes;
    /**
     * 座位类型 (字符串拼接, 以,隔离)
     */
    private String seatTypes;

    /**
     * 出票套餐id
     */
    private int buyTicketPackageId;

    /**
     * 抢票套餐id
     */
    private int grabTicketPackageId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 联系人id列表
     */
    private List<GrabPassenger> passengers;

    @Data
    private class GrabPassenger {
        /**
         * 联系人id
         */
        private long id;

        /**
         * 票的类型
         */
        private String piaoType;
    }
}
