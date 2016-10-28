package com.jkm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yuxiang on 2016-10-28.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YsChannelRequestRecord extends BaseEntity{

    /**
     * 流水号
     */
    private String sn;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 返回码
     */
    private String retCode;
    /**
     * 请求参数
     */
    private String request;
    /**
     * 响应参数
     */
    private String response;
    /**
     * 备注
     */
    private String remark;
    /**
     * 请求时间
     */
    private long time;

}
