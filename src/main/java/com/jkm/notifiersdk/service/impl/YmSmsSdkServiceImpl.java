package com.jkm.notifiersdk.service.impl;

import com.google.common.base.Preconditions;
import com.jkm.notifiersdk.entity.*;
import com.jkm.notifiersdk.enums.EnumYmSkdRequestBusinessType;
import com.jkm.notifiersdk.helper.LogYmSdkRequestPostProcessor;
import com.jkm.notifiersdk.helper.YmSdkRequestPostProcessor;
import com.jkm.notifiersdk.service.YmSmsSdkService;
import com.jkm.util.http.client.HttpClientFacade;
import lombok.Setter;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created on 16/8/1.
 *
 * @author hutao
 * @version 1.0
 */
@Service
public class YmSmsSdkServiceImpl implements YmSmsSdkService, InitializingBean {
    @Autowired
    private HttpClientFacade httpClientFacade;
    @Setter
    private YmSdkRequestPostProcessor postProcessor = new LogYmSdkRequestPostProcessor();

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseYmSdkResponse registerCompanyInfo() {
        final RegistrationCompanyParams params = new RegistrationCompanyParams();
        final Map<String, String> requestParamsMap = params.serializeToMap();
        final EnumYmSkdRequestBusinessType businessType = EnumYmSkdRequestBusinessType.REGISTER_COMPANY;
        final StopWatch stopWatch = new StopWatch();
        final String responseContent = requestImpl(requestParamsMap, businessType, stopWatch);
        final BaseYmSdkResponse response = BaseYmSdkResponse.of(responseContent);
        this.postProcessor.handle(businessType, requestParamsMap,
                response.getCode(), responseContent, stopWatch.getTime(), response.getMessage());
        return response;
    }

    private String requestImpl(final Map<String, String> requestParamsMap,
                               final EnumYmSkdRequestBusinessType businessType,
                               final StopWatch stopWatch) {
        try {
            stopWatch.start();
            final String content = this.httpClientFacade.post(businessType.getUrl(), requestParamsMap);
            stopWatch.stop();
            return content.replaceAll("\\r\\n", "");
        } catch (final Throwable e) {
            stopWatch.stop();
            this.postProcessor.handle(businessType, requestParamsMap,
                    "error", "", stopWatch.getTime(), e.getMessage());
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseYmSdkResponse registerCdKey() {
        final BaseYmSdkRequest params = new BaseYmSdkRequest();
        final Map<String, String> requestParamsMap = params.serializeToMap();
        final EnumYmSkdRequestBusinessType businessType = EnumYmSkdRequestBusinessType.REGISTER_SN;
        final StopWatch stopWatch = new StopWatch();
        final String responseContent = requestImpl(requestParamsMap, businessType, stopWatch);
        final BaseYmSdkResponse response = BaseYmSdkResponse.of(responseContent);
        this.postProcessor.handle(businessType, requestParamsMap,
                response.getCode(), responseContent, stopWatch.getTime(), response.getMessage());
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseYmSdkResponse unRegisterCdKey() {
        final BaseYmSdkRequest params = new BaseYmSdkRequest();
        final Map<String, String> requestParamsMap = params.serializeToMap();
        final EnumYmSkdRequestBusinessType businessType = EnumYmSkdRequestBusinessType.UN_REGISTER_SN;
        final StopWatch stopWatch = new StopWatch();
        final String responseContent = requestImpl(requestParamsMap, businessType, stopWatch);
        final BaseYmSdkResponse response = BaseYmSdkResponse.of(responseContent);
        this.postProcessor.handle(businessType, requestParamsMap,
                response.getCode(), responseContent, stopWatch.getTime(), response.getMessage());
        return response;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseYmSdkResponse sendInstantSms(final SendInstantSmsParams params) {
        final Map<String, String> requestParamsMap = params.serializeToMap();
        final EnumYmSkdRequestBusinessType businessType = EnumYmSkdRequestBusinessType.SEND_INSTANT_SMS;
        final StopWatch stopWatch = new StopWatch();
        final String responseContent = requestImpl(requestParamsMap, businessType, stopWatch);
        final BaseYmSdkResponse response = BaseYmSdkResponse.of(responseContent);
        this.postProcessor.handle(businessType, requestParamsMap,
                response.getCode(), responseContent, stopWatch.getTime(), response.getMessage());
        return response;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseYmSdkResponse sendTimedSms(final SendTimedSmsParams params) {
        final Map<String, String> requestParamsMap = params.serializeToMap();
        final EnumYmSkdRequestBusinessType businessType = EnumYmSkdRequestBusinessType.SEND_TIMED_SMS;
        final StopWatch stopWatch = new StopWatch();
        final String responseContent = requestImpl(requestParamsMap, businessType, stopWatch);
        final BaseYmSdkResponse response = BaseYmSdkResponse.of(responseContent);
        this.postProcessor.handle(businessType, requestParamsMap,
                response.getCode(), responseContent, stopWatch.getTime(), response.getMessage());
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        final BaseYmSdkResponse registerCdKeyResponse = registerCdKey();
        Preconditions.checkState(registerCdKeyResponse.isSuccess(), "短信sdk注册序列号失败:" + registerCdKeyResponse);
        final BaseYmSdkResponse registerCompanyInfoResponse = registerCompanyInfo();
        Preconditions.checkState(registerCompanyInfoResponse.isSuccess(), "短信sdk注册企业信息失败" + registerCompanyInfoResponse);
    }
}
