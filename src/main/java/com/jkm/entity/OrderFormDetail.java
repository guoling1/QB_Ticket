package com.jkm.entity;

import com.jkm.entity.helper.UserBankCardSupporter;
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
     * 抢票单ID
     */
    private long grabTicketFormId;
    /**
     * 是否是抢票单(0(正常代购单)否  1是)
     */
    private int isGrab;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 乘客id
     */
    private long passengerId;

    /**
     * 乘客名字
     */
    private String passengerName;

    /**
     * 乘客证件号码
     */
    private String passportSeNo;

    /**
     * 乘客证件类型id
     *
     * {@link com.jkm.enums.EnumCertificatesType}
     */
    private String passportTypeSeId;

    /**
     * 乘客证件类型名称
     *
     * {@link com.jkm.enums.EnumCertificatesType}
     */
    private String passportTypeSeName;

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

    /**
     * 获得解密后的证件号码
     *
     * @return
     */
    public String getPassportSeNoPlain() {
        return UserBankCardSupporter.decryptCardId(this.passportSeNo);
    }
}
