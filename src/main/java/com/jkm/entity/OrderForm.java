package com.jkm.entity;

import com.jkm.enums.EnumOrderFormStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/10/27.
 *
 * 订单
 *
 * tb_order_form
 *
 * {@link com.jkm.enums.EnumOrderFormStatus}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderForm extends BaseEntity {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 车票价格
     */
    private BigDecimal price;

    /**
     * 订单总价格
     */
    private BigDecimal totalPrice;

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
     * 车次
     */
    private String checi;

    /**
     * 12306用户名
     *
     * 当此订单为托管时有此字段
     */
    private String loginUserName;

    /**
     * 12306密码
     *
     * 当此订单为托管时有此字段
     */
    private String loginUserPassword;

    /**
     * 商户订单号
     */
    private String orderId;

    /**
     * 空铁订单号
     */
    private String outOrderId;

    /**
     * 发车日期 (yyyyMMdd)
     */
    private Date trainDate;

    /**
     * 取票单号（电子单号）
     */
    private String orderNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 客户是否 付款成功
     *
     * @return
     */
    public boolean isCustomerPaySuccess() {
        return this.getStatus() == EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_SUCCESS.getId();
    }

    /**
     * 订单是否是占座请求中
     *
     * @return
     */
    public boolean isRequestOccupySeatRequesting() {
        return this.getStatus() == EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getId();
    }

    /**
     * 订单是否是  确认出票请求成功
     *
     * @return
     */
    public boolean confirmTicketRequestSuccess() {
        return this.getStatus() == EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_SUCCESS.getId();
    }
    /**
     * 订单是否是  可以取消订单
     *
     * @return
     */
    public boolean isCanCancelOrder() {
        return this.getStatus() == EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_TRUE.getId()
                || this.getStatus() == EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_SUCCESS.getId()
                || this.getStatus() == EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_FAIL.getId();
    }
}
