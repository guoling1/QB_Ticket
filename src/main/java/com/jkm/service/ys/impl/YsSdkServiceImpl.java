package com.jkm.service.ys.impl;

import com.google.common.base.Preconditions;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.YsRefundTicketRequest;
import com.jkm.service.ys.entity.YsRefundTicketResponse;
import com.jkm.service.ys.entity.YsTrainStationQueryRequest;
import com.jkm.service.ys.entity.YsTrainStationQueryResponse;
import com.jkm.service.ys.helper.YsSdkConstants;
import com.jkm.service.ys.helper.YsSdkSignUtil;
import com.jkm.service.ys.helper.serialize.YsSdkSerializeUtil;
import com.jkm.util.http.client.HttpClientFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     *  {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    public YsRefundTicketResponse refundTicket(YsRefundTicketRequest request) {
        return null;
    }


    private void addSign(final Map<String, String> requestParamMap) {
        requestParamMap.put(YsSdkSignUtil.SIGN_KEY, YsSdkSignUtil.sign(requestParamMap, YsSdkConstants.SIGN_KEY));

    }


/*
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
*/

    private String requestImpl(final Map<String, String> requestParamMap,
                               final StopWatch stopWatch,
                               final long businessId,
                               final String businessOrderNo,
                               final String sn) {
        stopWatch.start();
        try {
            final String response = this.httpClientFacade.post(YsSdkConstants.SERVICE_URL, requestParamMap, CHARSET);
            stopWatch.stop();
            return response;
        } catch (final Throwable e) {
            stopWatch.stop();
            throw e;
        }
    }


    private void checkSignIfNeed(final Map<String, String> requestParamMap) {
        if (requestParamMap.containsKey(YsSdkSignUtil.SIGN_KEY)) {
            Preconditions.checkState(YsSdkSignUtil.checkSign(
                    requestParamMap, YsSdkConstants.SIGN_KEY,
                    requestParamMap.get(YsSdkSignUtil.SIGN_KEY)), "签名不正确");
        }
    }
}
