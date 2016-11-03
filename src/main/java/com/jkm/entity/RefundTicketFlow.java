package com.jkm.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuxiang on 2016-11-02.
 * tb_refund_ticket_flow
 * {@}
 */
@Data
public class RefundTicketFlow extends BaseEntity{

    private long orderFormId;

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
