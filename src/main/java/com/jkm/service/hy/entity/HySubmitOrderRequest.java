package com.jkm.service.hy.entity;

import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeAlias;
import lombok.Data;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/31.
 *
 * 订单提交 -- 请求参数
 */
@Data
public class HySubmitOrderRequest extends HySdkRequest {

    /**
     * 我方订单号
     */
    @HySdkSerializeAlias(name = "orderid")
    private String orderId;

    /**
     * 车次
     */
    private String checi;

    /**
     * 出发站简码
     */
    @HySdkSerializeAlias(name = "from_station_code")
    private String fromStationCode;

    /**
     * 出发站名称
     */
    @HySdkSerializeAlias(name = "from_station_name")
    private String fromStationName;

    /**
     * 到达站简码
     */
    @HySdkSerializeAlias(name = "to_station_code")
    private String toStationCode;

    /**
     * 到达站名称
     */
    @HySdkSerializeAlias(name = "to_station_name")
    private String toStationName;

    /**
     * 乘车日期
     */
    @HySdkSerializeAlias(name = "train_date")
    private String trainDate;

    /**
     * 回到url
     */
    @HySdkSerializeAlias(name = "callbackurl")
    private String callbackUrl;

    /**
     * 12306用户名
     */
    @HySdkSerializeAlias(name = "loginusername")
    private String loginUserName;

    /**
     * 12306密码
     */
    @HySdkSerializeAlias(name = "loginuserpassword")
    private String loginUserPassword;

    /**
     * 是否要无座（true: 要； false:不要）
     */
    private boolean isAcceptStanding;

    /**
     * 乘客集合
     */
    private List<passenger> passengers;

    /**
     * 乘客
     */
    @Data
    public class passenger {

        /**
         * 乘客的顺序号
         */
        @HySdkSerializeAlias(name = "passengerid")
        private int passengerId;

        /**
         * 票号（此票在本订单中的唯一标识）
         */
        @HySdkSerializeAlias(name = "ticket_no")
        private String ticketNo;

        /**
         * 乘客姓名
         */
        @HySdkSerializeAlias(name = "passengername")
        private String passengerName;

        /**
         * 乘客证件号码
         */
        @HySdkSerializeAlias(name = "passportseno")
        private String passportSeNo;

        /**
         * 证件类型 ID
         *
         * {@link com.jkm.enums.EnumCertificatesType}
         */
        @HySdkSerializeAlias(name = "passporttypeseid")
        private String passportTypeSeId;

        /**
         * 证件类型名称
         *
         * {@link com.jkm.enums.EnumCertificatesType}
         */
        @HySdkSerializeAlias(name = "passporttypeseidname")
        private String passportTypeSeIdName;

        /**
         * 票种 ID
         *
         * {@link com.jkm.enums.EnumTrainTicketType}
         */
        private String piaoType;

        /**
         * 票种名称
         *
         * {@link com.jkm.enums.EnumTrainTicketType}
         */
        @HySdkSerializeAlias(name = "piaotypename")
        private String piaoTypeName;

        /**
         * 座位编码
         *
         * 注意：当最低的一种座位，无票时，购买选择该座位种类，
         * 买下的就是无座(也就说买无座的席别编码就是该车次的 最低席别的编码)，
         * 另外，当最低席别的票卖完了的时候 才可以卖无座的票。
         *
         * {@link com.jkm.enums.EnumTrainTicketSeatType}
         */
        @HySdkSerializeAlias(name = "zwcode")
        private String zwCode;

        /**
         * 座位名称
         *
         * {@link com.jkm.enums.EnumTrainTicketSeatType}
         */
        @HySdkSerializeAlias(name = "zwname")
        private String zwName;

        /**
         * 几车厢几座（在订票成功后才会有值，如：‘15车厢，20号上铺’）
         */
        private String cxin;

        /**
         * 票件
         */
        private String price;

        /**
         * 身份核验状态 0：正常 1：待审核 2：未通过（占座结果回调才有）
         */
        private int reason;
//        province_name	string	省份名称：参考附件5.4 的provinces
//        请求占座且车票类型piaotype为学生票时需要填写，注：该批字段仅供输入，不用于接口输出
//        province_code	string	省份编号：参考附件5.4 的provinces
//        school_code	string	学校代号
//        school_name	string	学校名称
//        student_no	string	学号
//        school_system	string	学制
//        enter_year	string	入学年份：yyyy
//        preference_from_station_name	string	优惠区间起始地名称【选填】
//        preference_from_station_code	string	优惠区间起始地代号
//        preference_to_station_name	string	优惠区间到达地名称【选填】
//        preference_to_station_code	string	优惠区间到达地代号

        /**
         * 是否需要保险
         */
        private boolean isNeedInsure;

//        province	String	省
//        city	String	市
//        distric	String	区
//        detailedaddress	String	具体地址
//        zipcode	String	邮政编码
//        recipientname	String	收件人姓名
//        recipientphone	String	收件人电话

    }
}
