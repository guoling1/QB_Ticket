package com.jkm.notifiersdk.entity;

import com.jkm.notifiersdk.config.YmSdkConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on 16/7/22.
 *
 * @author hutao
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegistrationCompanyParams extends BaseYmSdkRequest {
    /**
     * 企业名称(最多60字节)，必须输入
     */
    private final String ename = YmSdkConstants.getYmSdkConfig().ename();
    /**
     * 联系人姓名(最多20字节)，必须输入
     */
    private final String linkman = YmSdkConstants.getYmSdkConfig().linkman();
    /**
     * 联系电话(最多20字节)，必须输入
     */
    private final String phonenum = YmSdkConstants.getYmSdkConfig().phonenum();
    /**
     * 联系手机(最多15字节)，必须输入
     */
    private final String mobile = YmSdkConstants.getYmSdkConfig().mobile();
    /**
     * 电子邮件(最多60字节)，必须输入
     */
    private final String email = YmSdkConstants.getYmSdkConfig().email();
    /**
     * 联系传真(最多20字节)，必须输入
     */
    private final String fax = YmSdkConstants.getYmSdkConfig().fax();
    /**
     * 公司地址(最多60字节)，必须输入
     */
    private final String address = YmSdkConstants.getYmSdkConfig().address();
    /**
     * 邮政编码(最多6字节)，必须输入
     */
    private final String postcode = YmSdkConstants.getYmSdkConfig().postcode();
}
