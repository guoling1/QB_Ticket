package com.jkm.service.ys.entity;

import com.jkm.service.ys.helper.YsSdkConstants;
import lombok.Data;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Data
public class YsSdkRequest {

    /**
     *终端号；YS 分配，如“04800001”
     */
    private String terminalID = YsSdkConstants.TERMINAL_ID;
    /**
     *企业编号；YS 分配，如“1011
     */
    private String factoryID = YsSdkConstants.FACTORY_ID;
    /**
     *请求时间
     * 格式： YYYYMMDDHHMMSS
     */
    private String reqDateTime;
    /**
     *数字签名，查看
     */
    private String sign;
}
