package com.jkm.controller.helper.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/28.
 *
 * 订票请求参数
 */
@Data
@Builder
public class RequestSubmitOrder {

    /**
     * 用户id
     */
    private String uid;

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
     * 坐席code
     */
    private String zwName;

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
     * 发车日期 (yyyy-MM-dd)
     */
    private String trainDate;

    /**
     * 联系人id列表
     */
    private List<Passenger> passengers;

    @Data
    public class Passenger {
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
