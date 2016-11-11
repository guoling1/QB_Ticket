package com.jkm.entity;

import com.jkm.enums.EnumRefundTicketFlowStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by yuxiang on 2016-11-02.
 * tb_refund_ticket_flow
 *
 * {@link EnumRefundTicketFlowStatus}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundTicketFlow extends BaseEntity{
    /**
     * 退票流水号(唯一),如果是线下退票,改签则自己生成
     */
    private String reqToken;
    /**
     * 退票类型【0：表示线下退票退款； 1：表示线上退票退款；2：线下改签退款；
     */
    private int returnType;
    /**
     * 大订单
     */
    private long orderFormId;
    /**
     * 抢票单
     */
    private long grabTicketFormId;
    /**
     * 小订单
     */
    private long orderFormDetailId;

    private String ticketNo;

    private Date applyTime;

    private String successTime;

    /**
     * 退款金额
     */
    private String returnmoney;

    /**
     *
     */
    private String remark;

}
