package com.jkm.service.hy.entity;

import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeAlias;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeListNeedConverter;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeNoInclude;
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
     * 是否要无座（true: 要； false:不要）
     */
    @HySdkSerializeAlias(name = "is_accept_standing")
    private boolean isAcceptStanding = false;

    /**
     * 回到url
     */
    @HySdkSerializeAlias(name = "callbackurl")
    private String callbackUrl;

    /**
     * 出发站名称
     */
    @HySdkSerializeAlias(name = "from_station_name")
    private String fromStationName;

    /**
     * 出发站简码
     */
    @HySdkSerializeNoInclude
    @HySdkSerializeAlias(name = "from_station_code")
    private String fromStationCode;

    /**
     * 车次
     */
    private String checi;

    /**
     * 到达站名称
     */
    @HySdkSerializeAlias(name = "to_station_name")
    private String toStationName;

    /**
     * 到达站简码
     */
    @HySdkSerializeNoInclude
    @HySdkSerializeAlias(name = "to_station_code")
    private String toStationCode;

    /**
     * 12306用户名
     *
     * 当此订单为托管时有此字段
     */
    @HySdkSerializeAlias(name = "loginusername")
    private String loginUserName;

    /**
     * 12306密码
     *
     * 当此订单为托管时有此字段
     */
    @HySdkSerializeAlias(name = "loginuserpassword")
    private String loginUserPassword;

    /**
     * 商户订单号
     */
    @HySdkSerializeAlias(name = "orderid")
    private String orderId;

    /**
     * 发车日期
     */
    @HySdkSerializeAlias(name = "train_date")
    private String trainDate;

    /**
     * 乘客集合
     */
    @HySdkSerializeListNeedConverter
    private List<passenger> passengers;

    /**
     * 乘客
     */
    @Data
    public class passenger {

        /**
         * 乘客姓名
         */
        @HySdkSerializeAlias(name = "passengersename")
        private String passengerSeName;

        /**
         * 乘客证件号码
         */
        @HySdkSerializeAlias(name = "passportseno")
        private String passportSeNo;

        /**
         * 证件类型 ID
         * <p/>
         * {@link com.jkm.enums.EnumCertificatesType}
         */
        @HySdkSerializeAlias(name = "passporttypeseid")
        private String passportTypeSeId;

        /**
         * 证件类型名称
         * <p/>
         * {@link com.jkm.enums.EnumCertificatesType}
         */
        @HySdkSerializeAlias(name = "passporttypeseidname")
        private String passportTypeSeIdName;

        /**
         * 乘客唯一标识
         */
        @HySdkSerializeAlias(name = "passengerid")
        private int passengerId;

        /**
         * 座位编码
         * <p/>
         * 注意：当最低的一种座位，无票时，购买选择该座位种类，
         * 买下的就是无座(也就说买无座的席别编码就是该车次的 最低席别的编码)，
         * 另外，当最低席别的票卖完了的时候 才可以卖无座的票。
         * <p/>
         * {@link com.jkm.enums.EnumTrainTicketSeatType}
         */
        @HySdkSerializeAlias(name = "zwcode")
        private String zwCode;

        /**
         * 座位名称
         * <p/>
         * {@link com.jkm.enums.EnumTrainTicketSeatType}
         */
        @HySdkSerializeAlias(name = "zwname")
        private String zwName;

        /**
         * 票价
         */
        private String price;

        /**
         * 票种 ID
         * <p/>
         * {@link com.jkm.enums.EnumTrainTicketType}
         */
        @HySdkSerializeAlias(name = "piaotype")
        private String piaoType;

        /**
         * 几车厢几座（在订票成功后才会有值，如：‘15车厢，20号上铺’）
         * <p/>
         * 传空字符串
         */
        private String cxin = "";
    }
}
