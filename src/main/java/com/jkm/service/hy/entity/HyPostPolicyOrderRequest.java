package com.jkm.service.hy.entity;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-01.
 */
public class HyPostPolicyOrderRequest {

    private String username;
    private String method;
    private String reqtime;
    private String sign;
    private List<PassengerPolicyInfo> data;
    private String insProductNo;
    private String flightDate;
    private String flightNumber;
    private String serialNo;
    private String contractName;
    private String contractType;
    private String gender;
    private int cardType;
    private String cardNo;
    private String phone;

    public class PassengerPolicyInfo{

    }
}
