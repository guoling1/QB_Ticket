package com.jkm.entity;

import com.jkm.enums.EnumOrderFormDetailStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yuxiang on 2016-10-27.
 *
 * 订单详情
 *
 * tb_order_form_detail
 *
 * {@link com.jkm.enums.EnumOrderFormDetailStatus}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderFormDetail extends BaseEntity{
    /**
     * 订单ID
     */
    private long orderFormId;

    /**
     * 乘客id
     */
    private long passengerId;

    /**
     * 车票的票号
     */
    private String ticketNo;

    /**
     * 票价
     */
    private BigDecimal price;

    /**
     * 车次
     */
    private String checi;

    /**
     * 票的类型
     *
     * {@link com.jkm.enums.EnumTrainTicketType}
     */
    private String piaoType;

    /**
     * 几车厢几号
     */
    private String cxin;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否是初始化状态
     *
     * @return
     */
    public boolean isTicketInit() {
        return this.getStatus() == EnumOrderFormDetailStatus.TICKET_INITIALIZATION.getId();
    }

}
