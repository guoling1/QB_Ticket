package com.jkm.service.ys.entity;

import com.jkm.service.ys.helper.serialize.annotation.YsSdkSerializeNoInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Data
public class YsRefundCallbackResponse  extends CanCheckSignResponse{

    /**
     *交易状态
     */
    private String status;

    /**
     *交易结果描述
     */
    private String msg;

    /**
     *数字签名
     */
    private String sign;

    private String termTransID;

    private String transID;

    private String refurndTime;

    private String refundType;

    @YsSdkSerializeNoInclude
    private List<Passanger> passengers;


    @Data
    @NoArgsConstructor
    private class Passanger{

        /**
         * 乘车人乘客 ID 如 TA
         */
        private String id;
        /**
         * 乘客类型，1 为成人 2 为儿童
         */
        private String type;
        /**
         * 乘车人姓名
         */
        private String name;
        /**
         * 证件号码
         */
        private String cardNo;
        /**
         * 火车站实际退票价格（扣除了退票手续费）100.00
         */
        private String refundPrice;
    }
}
