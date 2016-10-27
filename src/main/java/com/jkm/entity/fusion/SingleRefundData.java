package com.jkm.entity.fusion;


public class SingleRefundData {
    private String reqSn;//交易流水号
    private String signedMsg;//签名信息
    private String orgSn;//原订单流水号
    private String orgDate;//原商户订单日期
    private String refundAmount;//退款金额
    private String orgAmount;//原订单金额
    private String mercId;//商户编号
    private String refundOrdNo;//商户退款订单编号
    private String ordDate;//商户退款订单日期
    private String refundReason;//退款原因
    private String transType;//退款类型

    public String getReqSn() {
        return reqSn;
    }

    public void setReqSn(String reqSn) {
        this.reqSn = reqSn;
    }

    public String getSignedMsg() {
        return signedMsg;
    }

    public void setSignedMsg(String signedMsg) {
        this.signedMsg = signedMsg;
    }

    public String getOrgSn() {
        return orgSn;
    }

    public void setOrgSn(String orgSn) {
        this.orgSn = orgSn;
    }

    public String getOrgDate() {
        return orgDate;
    }

    public void setOrgDate(String orgDate) {
        this.orgDate = orgDate;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getOrgAmount() {
        return orgAmount;
    }

    public void setOrgAmount(String orgAmount) {
        this.orgAmount = orgAmount;
    }

    public String getMercId() {
        return mercId;
    }

    public void setMercId(String mercId) {
        this.mercId = mercId;
    }

    public String getRefundOrdNo() {
        return refundOrdNo;
    }

    public void setRefundOrdNo(String refundOrdNo) {
        this.refundOrdNo = refundOrdNo;
    }

    public String getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(String ordDate) {
        this.ordDate = ordDate;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
