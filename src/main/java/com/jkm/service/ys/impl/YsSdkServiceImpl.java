package com.jkm.service.ys.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.jkm.entity.YsChannelRequestRecord;
import com.jkm.enums.EnumBusinessType;
import com.jkm.service.ys.YsSdkRequestRecordService;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.*;
import com.jkm.service.ys.helper.YsSdkConstants;
import com.jkm.service.ys.helper.YsSdkSignUtil;
import com.jkm.service.ys.helper.serialize.YsSdkSerializeUtil;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.JsonUtil;
import com.jkm.util.http.client.HttpClientFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    public YsTrainTicketsBookingResponse bookingTicket(final YsTrainTicketsBookingRequest request) {
        final HashMap<String, String> requestMap = this.request2Map(request);
        addSign(requestMap, request.getSignStr());
        final StopWatch stopWatch = new StopWatch();
        final String responseContent = requestImpl(requestMap, EnumBusinessType.BOOK_TICKET_SERVICE, stopWatch, request.getTermTransID());
        final YsTrainTicketsBookingResponse response = JSON.parseObject(responseContent, YsTrainTicketsBookingResponse.class);
        this.postHandle(request.getTermTransID(),
                EnumBusinessType.REFUND_TICKET_SERVICE.getType(),
                JsonUtil.toJsonString(requestMap),
                responseContent,
                stopWatch.getTime()
        );
        checkSignResponse(response.getSign(), response.getSignStr());
        return response;
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

    private void addSign(final Map<String, String> requestMap, final String signString) {
        requestMap.put(YsSdkSignUtil.SIGN_KEY, YsSdkSignUtil.sign(signString));
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

    private void checkSignResponse(final String responseSignStr, final String needSignStr) {
        Preconditions.checkState(YsSdkSignUtil.checkSign(needSignStr, responseSignStr), "响应签名不正确");
    }

    private final HashMap<String, String> request2Map(final YsSdkRequest request) {
        final HashMap<String, String> map = new HashMap<>();
        final JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(request));
        final Set<String> keySet = jsonObject.keySet();
        final Iterator<String> iterator = keySet.iterator();
        while(iterator.hasNext()) {
            final String key = iterator.next();
            map.put(key, jsonObject.getString(key));
        }
        return map;
    }

}
