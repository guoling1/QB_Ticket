package com.jkm.notifiersdk.service;


import com.jkm.notifiersdk.entity.BaseYmSdkResponse;
import com.jkm.notifiersdk.entity.SendInstantSmsParams;
import com.jkm.notifiersdk.entity.SendTimedSmsParams;

/**
 * Created on 16/7/22.
 *
 * @author hutao
 * @version 1.0
 */
public interface YmSmsSdkService {
    /**
     * 注册企业信息
     */
    BaseYmSdkResponse registerCompanyInfo();

    /**
     * 注册序列号
     */
    BaseYmSdkResponse registerCdKey();

    /**
     * 注销序列号
     */
    BaseYmSdkResponse unRegisterCdKey();

    /**
     * 发送即时短信
     *
     * @param params
     * @return
     */
    BaseYmSdkResponse sendInstantSms(final SendInstantSmsParams params);

    /**
     * 发送定时短信
     *
     * @param params
     * @return
     */
    BaseYmSdkResponse sendTimedSms(final SendTimedSmsParams params);
}
