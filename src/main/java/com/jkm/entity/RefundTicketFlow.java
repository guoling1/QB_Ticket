package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by yuxiang on 2016-11-02.
 * tb_refund_ticket_flow
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundTicketFlow extends BaseEntity{
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
     * 失败原因
     */
    private String remark;

}
