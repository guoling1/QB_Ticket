package com.jkm.entity.fusion;


public class CardAuthData {
    /**
     * 商户号
     */
    private String mercId;
    /**
     * 交易流水号
     */
    private String reqSn;
    /**
     * 同步通知页面路径
     */
    private String retUrl;
    /**
     * 异步通知页面路径
     */
    private String notifyUrl;
    /**
     * 卡号
     */
    private String crdNo;
    /**
     *银行账户名
     */
    private String accountName;
    /**
     * 证件类型
     */
    private String idType;
    /**
     * 证件号
     */
    private String idNo;
    /**
     * 手机号
     */
    private String phoneNo;
    /**
     * 信用卡有效期
     */
    private String creValDate;
    /**
     * 令牌
     */
    private String creCvn2;

    public String getMercId() {
        return mercId;
    }

    public void setMercId(String mercId) {
        this.mercId = mercId;
    }

    public String getReqSn() {
        return reqSn;
    }

    public void setReqSn(String reqSn) {
        this.reqSn = reqSn;
    }

    public String getRetUrl() {
        return retUrl;
    }

    public void setRetUrl(String retUrl) {
        this.retUrl = retUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCrdNo() {
        return crdNo;
    }

    public void setCrdNo(String crdNo) {
        this.crdNo = crdNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCreValDate() {
        return creValDate;
    }

    public void setCreValDate(String creValDate) {
        this.creValDate = creValDate;
    }

    public String getCreCvn2() {
        return creCvn2;
    }

    public void setCreCvn2(String creCvn2) {
        this.creCvn2 = creCvn2;
    }
}
