package com.jkm.controller.helper.request;

import com.jkm.enums.EnumGrabTimeType;
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
     * appId
     */
    private String appId;

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
     * 抢票出发日期 2015-09-09
     */
    private String grabStartTime;
    /**
     * 抢票时效类型
     * {@link EnumGrabTimeType}
     */
    private int grabTimeType;
    /**
     * 最早发车时间
     */
    private String firstStartTime;
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
    private List<GrabPassenger> grabPassengers;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class GrabPassenger {
        /**
         * 联系人id
         */
        private long id;

        private String name;

        /**
         * 票的类型
         */
        private String piaoType;
    }
}
