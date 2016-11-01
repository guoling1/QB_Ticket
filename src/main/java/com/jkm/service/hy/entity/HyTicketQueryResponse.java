package com.jkm.service.hy.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2016/11/1.
 */
@Data
public class HyTicketQueryResponse extends HySdkResponse {
    /**
     * 车次信息
     */
    private String data;

//    /**
//     * 车次
//     */
//    @Data
//    public class data{
//
//        /**
//         * 车票开售时间
//         */
//        private String saleDateTime;
//
//        /**
//         * 当前是否可以接受预定（Y:可以，N:不可以）
//         */
//        private String canBuyNow;
//
//        /**
//         * 列车从出发站到达目的站的运行天数
//         * 0：当日到达，1：次日到达，2：三日到达，3：四日到达，依此 类推
//         */
//        private String arriveDays;
//
//        /**
//         * 列车从始发站出发的日期
//         */
//        private String trainStartDate;
//
//        /**
//         * 车次
//         */
//        private String trainCode;
//
//        /**
//         * 是否可凭二代身份证直接进出站
//         */
//        private String accessByIdCard;
//
//        /**
//         * 列车号
//         */
//        private String trainNo;
//
//        /**
//         * 列车类型
//         */
//        private String trainType;
//
//        /**
//         * 出发车站名
//         */
//        private String fromStationName;
//
//        /**
//         * 出发车站简码
//         */
//        private String fromStationCode;
//
//        /**
//         * 到达车站名
//         */
//        private String toStationName;
//
//        /**
//         * 到达车站简码
//         */
//        private String toStationCode;
//
//        /**
//         * 列车始发站名
//         */
//        private String startStationName;
//
//        /**
//         * 列车终到站名
//         */
//        private String endStationName;
//
//        /**
//         * 出发时刻
//         */
//        private String startTime;
//
//        /**
//         * 到达时刻
//         */
//        private String arriveTime;
//
//        /**
//         * 历时（从出发站到目的站的列车运行时间）
//         */
//        private String runTime;
//
//        /**
//         * 历时分钟合计
//         */
//        private String runTimeMinute;
//
//        /**
//         * 高级软卧余票数量
//         */
//        private String gjrwNum;
//
//        /**
//         * 其他席别余票数量
//         */
//        private String qtxbNum;
//
//        /**
//         * 软卧余票数量
//         */
//        private String rwNum;
//
//        /**
//         * 软座的余票数量
//         */
//        private String rzNum;
//
//        /**
//         * 商务座的余票数据
//         */
//        private String swzNum;
//
//        /**
//         * 特等座的余票数量
//         */
//        private String tdzNum;
//
//        /**
//         * 无座的余票数量
//         */
//        private String wzNum;
//
//        /**
//         * 硬卧的余票数量
//         */
//        private String ywNum;
//
//        /**
//         * 硬座的余票数量
//         */
//        private String yzNum;
//
//        /**
//         * 二等座的余票数量
//         */
//        private String edzNum;
//
//        /**
//         * 一等座的余票数量
//         */
//        private String ydzNum;
//
//        /**
//         * 里程数
//         */
//        private String distance;
//
//        /**
//         * 备注（起售时间）
//         */
//        private String note;
//
//    }


}
