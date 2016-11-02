package com.jkm.service.hy.entity;

import lombok.Data;

/**
 * Created by yuxiang on 2016-11-01.
 */
@Data
public class HyCancelPolicyOrderRequest {

    private String username;
    private String method;
    private String reqtime;
    private String sign;

    private String policyNo;
}
