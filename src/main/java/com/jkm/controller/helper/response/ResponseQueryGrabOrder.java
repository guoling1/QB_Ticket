package com.jkm.controller.helper.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-13.
 */
@Data
public class ResponseQueryGrabOrder {


    /**
     * 乘客
     */
    private List<ResponseQueryOrderForm.passenger> passengers;

    @Data
    public class passenger{
        /**
         *  乘车人姓名
         */
        private String name;

        /**
         * 证件号
         */
        private String passportSeNo;

        /**
         *  票类型
         *
         *  {@link com.jkm.enums.EnumTrainTicketType}
         */
        private String piaoType;

        /**
         *  票类型名
         *
         *  {@link com.jkm.enums.EnumTrainTicketType}
         */
        private String piaoTypeName;

        /**
         *车厢，座位 （Unicode 编码） 14 车厢，084 座
         */
        private String cxin;

        /**
         *火车站出票价格
         */
        private BigDecimal price;

        /**
         * 状态
         */
        private int status;
    }
}
