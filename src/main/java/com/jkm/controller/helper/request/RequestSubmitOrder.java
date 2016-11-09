package com.jkm.controller.helper.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/28.
 *
 * 订票请求参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestSubmitOrder {

    /**
     * appId
     */
    private String appId;

    /**
     * 用户id
     */
    private String uid;

    /**
     * mobile
     */
    private String mobile;

    /**
     * 车票价格
     */
    private BigDecimal price;

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
     * 坐席code
     */
    private String zwCode;

    /**
     * 发车日期
     */
    private String startDate;

    /**
     * 到达日期
     */
    private String endDate;

    /**
     * 发车时间
     */
    private String startTime;

    /**
     * 到达时间
     */
    private String endTime;

    /**
     * 历时
     */
    private String runTime;

    /**
     * 车次
     */
    private String checi;

    /**
     * 出票套餐id
     */
    private int buyTicketPackageId;

    /**
     * 抢票套餐id
     */
    private int grabTicketPackageId;

    /**
     * 联系人id列表
     */
    private List<Passenger> passengers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Passenger {
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
