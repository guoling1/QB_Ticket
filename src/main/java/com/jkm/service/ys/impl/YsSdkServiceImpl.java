package com.jkm.service.ys.impl;

import com.google.common.base.Preconditions;
import com.jkm.entity.YsChannelRequestRecord;
import com.jkm.enums.EnumBusinessType;
import com.jkm.service.ys.YsSdkRequestRecordService;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.*;
import com.jkm.service.ys.helper.YsSdkConstants;
import com.jkm.service.ys.helper.YsSdkSignUtil;
import com.jkm.service.ys.helper.serialize.YsSdkSerializeUtil;
import com.jkm.util.JsonUtil;
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
    @Autowired
    private YsSdkRequestRecordService ysSdkRequestRecordService;

    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    public YsTrainStationQueryResponse queryTrainStation(YsTrainStationQueryRequest request) {


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
        final Map<String, String> requestParamMap = YsSdkSerializeUtil.converterObjectToMap(request);
        addSign(requestParamMap);
        final StopWatch stopWatch = new StopWatch();
        final String responseContent = requestImpl(requestParamMap, EnumBusinessType.REFUND_TICKET_SERVICE, stopWatch, request.getTermTransID());
        final YsRefundTicketResponse response = JsonUtil.parseObject(responseContent, YsRefundTicketResponse.class);
        this.postHandle(request.getTermTransID(),
                EnumBusinessType.REFUND_TICKET_SERVICE.getType(),
                JsonUtil.toJsonString(requestParamMap),
                responseContent,
                stopWatch.getTime()
        );
        return response;
    }


    private void addSign(final Map<String, String> requestParamMap) {
        requestParamMap.put(YsSdkSignUtil.SIGN_KEY, YsSdkSignUtil.sign(requestParamMap, YsSdkConstants.SIGN_KEY));

    }



    private void postHandle(final String sn,
                            final String businessType,
                            final String requestParams,
                            final String returnParams,
                            final long millisecond) {
        final YsChannelRequestRecord record = YsChannelRequestRecord.builder().businessType(businessType).sn(sn).request(requestParams)
                .response(returnParams).time(millisecond).build();
        this.ysSdkRequestRecordService.record(record);
    }



    private String requestImpl(final Map<String, String> requestParamMap,
                               final EnumBusinessType enumBusinessType,
                               final StopWatch stopWatch,
                               final String sn) {
        stopWatch.start();
        try {
            final String response = this.httpClientFacade.post(YsSdkConstants.SERVICE_GATEWAY_URL + enumBusinessType.getAddress(), requestParamMap, CHARSET);
            stopWatch.stop();
            return response;
        } catch (final Throwable e) {
            this.postHandle(sn,enumBusinessType.getType(),JsonUtil.toJsonString(requestParamMap),"error",stopWatch.getTime());
            stopWatch.stop();
            throw e;
        }
    }

    private String requestImpl(final String requestParam,
                               final EnumBusinessType enumBusinessType,
                               final StopWatch stopWatch,
                               final String sn) {
        stopWatch.start();
        try {
            final String response = this.httpClientFacade.post(YsSdkConstants.SERVICE_GATEWAY_URL + enumBusinessType.getAddress(), requestParam);
            stopWatch.stop();
            return response;
        } catch (final Throwable e) {
            this.postHandle(sn,enumBusinessType.getType(),requestParam,"error",stopWatch.getTime());
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
