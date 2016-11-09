package com.jkm.entity.fusion;

/**
 * Created by Administrator on 2016/11/8.
 */
public class QueryRefundData {
    /**
     * 商户号
     */
    private String mercId;
    /**
     * 请求流水
     */
    private String reqSn;
    /**
     * 交易流水
     */
    private String querySn;

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

    public String getQuerySn() {
        return querySn;
    }

    public void setQuerySn(String querySn) {
        this.querySn = querySn;
    }
}
