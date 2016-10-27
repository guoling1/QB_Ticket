package com.jkm.common.third.channel.ys.service.impl;

import com.google.common.base.Preconditions;
import com.jkm.base.common.spring.http.client.HttpClientFacade;
import com.jkm.base.common.spring.util.JsonUtil;
import com.jkm.common.third.channel.ys.service.YsSdkService;
import com.jkm.common.third.channel.ys.service.entity.YsTrainStationQueryRequest;
import com.jkm.common.third.channel.ys.service.entity.YsTrainStationQueryResponse;
import com.jkm.common.third.channel.ys.service.helper.serialize.YsSdkSerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuxiang on 2016-09-29.
 */
@Slf4j
@Service
public class YsSdkServiceImpl implements YsSdkService {

    private static final String CHARSET = "UTF-8";

    @Autowired
    private HttpClientFacade httpClientFacade;

    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    public YsTrainStationQueryResponse queryTrainStation(YsTrainStationQueryRequest request) {

        final Map<String, String> requestParamMap = YsSdkSerializeUtil.converterObjectToMap(request);
        addSign(requestParamMap);
        final StopWatch stopWatch = new StopWatch();
       /* final String responseContent = requestImpl(requestParamMap, EnumYxtSdkServiceCode.CHECK_BANK_CARD, stopWatch, 0, "", request.getOrderNo());
        final YxtSdkBindUserCardResponse response = JsonUtil.parseObject(responseContent, YxtSdkBindUserCardResponse.class);
        this.postHandle(EnumYxtSdkServiceCode.CHECK_BANK_CARD,
                requestParamMap,
                EnumPayCompany.Yxt.getName(),
                0,
                responseContent,
                "",
                request.getOrderNo(),
                DateFormatUtil.format(new Date(stopWatch.getStartTime()), DateFormatUtil.yyyy_MM_dd_HH_mm_ss),
                DateFormatUtil.format(new Date(stopWatch.getStartTime() +stopWatch.getTime()), DateFormatUtil.yyyy_MM_dd_HH_mm_ss),
                stopWatch.getTime(),
                EnumRequestThirdChannelStatus.REQUEST_SUCCESS.getId()
        );
        // this.checkSignIfNeed(requestParamMap);
        return response;*/
       return new YsTrainStationQueryResponse();
    }


    private void addSign(final Map<String, String> requestParamMap) {
        requestParamMap.put(YxtSdkSignUtil.SIGN_KEY, YxtSdkSignUtil.sign(requestParamMap, YxtSdkConstants.SIGN_KEY));

    }

    /**
     * @param enumYxtSdkServiceCode   业务类型
     * @param requestParams      请求参数
     * @param paymentCompany    支付公司
     * @param businessId        业务id
     * @param returnParams      响应结果
     * @param businessOrderNo    业务订单号
     * @param businessOrderNo    流水号
     * @param requestTime    请求时间
     * @param returnTime    请求时间
     * @param millisecond         响应时长(毫秒单位)
     * @param requestStatus       请求状态
     */
    private void postHandle(final EnumYxtSdkServiceCode enumYxtSdkServiceCode,
                            final Map<String, String> requestParams,
                            final String paymentCompany,
                            final long businessId,
                            final String returnParams,
                            final String businessOrderNo,
                            final String sn,
                            final String requestTime,
                            final String returnTime,
                            final long millisecond,
                            final int requestStatus) {
        this.yxtSdkRequestPostProcessor.handle(enumYxtSdkServiceCode, requestParams,
                paymentCompany, businessId, returnParams, businessOrderNo, sn, requestTime, returnTime,
                millisecond, requestStatus);
    }

    private String requestImpl(final Map<String, String> requestParamMap,
                               final EnumYxtSdkServiceCode serviceCode,
                               final StopWatch stopWatch,
                               final long businessId,
                               final String businessOrderNo,
                               final String sn) {
        stopWatch.start();
        try {
            final String response = this.httpClientFacade.post(YxtSdkConstants.SERVICE_GATEWAY_URL, requestParamMap, CHARSET);
            stopWatch.stop();
            return response;
        } catch (final Throwable e) {
            stopWatch.stop();
            this.postHandle(serviceCode,
                    requestParamMap,
                    EnumPayCompany.Yxt.getName(),
                    businessId,
                    "error",
                    businessOrderNo,
                    sn,
                    DateFormatUtil.format(new Date(stopWatch.getStartTime()), DateFormatUtil.yyyy_MM_dd_HH_mm_ss),
                    DateFormatUtil.format(new Date(stopWatch.getStartTime() +stopWatch.getTime()), DateFormatUtil.yyyy_MM_dd_HH_mm_ss),
                    stopWatch.getTime(),
                    EnumRequestThirdChannelStatus.UNKNOWN.getId());
            throw e;
        }
    }


    private Map<String, String> analyseResponse(final String responseContent) {
        try {
            final Map<String, String> result = new HashMap<>();
            final String[] pairParams = StringUtils.split(responseContent.replace("{", "").replace("}", ""), ',');
            for (final String pairParam : pairParams) {
                final int first = pairParam.indexOf(':');
                final String key = pairParam.substring(0, first);
                final String value = pairParam.length() == (first + 1) ? "" :
                        pairParam.substring(first + 1, pairParam.length());
                result.put(key, value);
            }
            return result;
        } catch (final Throwable e) {
            log.error("analyse response error:[{}]", responseContent, e);
            throw e;
        }
    }

    private void checkSignIfNeed(final Map<String, String> requestParamMap) {
        if (requestParamMap.containsKey(YxtSdkSignUtil.SIGN_KEY)) {
            Preconditions.checkState(YxtSdkSignUtil.checkSign(
                    requestParamMap, YxtSdkConstants.SIGN_KEY,
                    requestParamMap.get(YxtSdkSignUtil.SIGN_KEY)), "签名不正确");
        }
    }
}
