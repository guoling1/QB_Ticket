package com.jkm.service.hy.impl;

import com.jkm.dao.HyChannelRequestRecordDao;
import com.jkm.entity.HyChannelRequestRecord;
import com.jkm.entity.YsChannelRequestRecord;
import com.jkm.enums.EnumBusinessType;
import com.jkm.service.hy.HySdkRequestRecordService;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.entity.HySdkRequest;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.service.hy.helper.HySdkSignUtil;
import com.jkm.service.ys.entity.YsRefundTicketResponse;
import com.jkm.service.ys.helper.YsSdkConstants;
import com.jkm.service.ys.helper.YsSdkSignUtil;
import com.jkm.util.JsonUtil;
import com.jkm.util.http.client.HttpClientFacade;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by yuxiang on 2016-11-01.
 */
@Service
public class HySdkServiceImpl implements HySdkService{
    @Autowired
    private HttpClientFacade httpClientFacade;
    @Autowired
    private HySdkRequestRecordService hySdkRequestRecordService;



    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    public HyReturnTicketResponse returnTicket(final HyReturnTicketRequest request) {
        addSign(request);
        final StopWatch stopWatch = new StopWatch();
        final String requestParams = JsonUtil.toJsonString(request);
        final String responseContent = requestImpl(requestParams, HySdkConstans.SERVICE_GATEWAY_URL,
                request.getOrderId(), request.getMethod(), stopWatch);
        final HyReturnTicketResponse response = JsonUtil.parseObject(responseContent, HyReturnTicketResponse.class);
        this.postHandle(request.getOrderId(),
                        request.getMethod(),
                        response.getCode(),
                        requestParams,
                        responseContent,
                        stopWatch.getTime());
        return response;
    }

    private void addSign(final HyReturnTicketRequest request) {
        request.setSign(HySdkSignUtil.sign(request.getMethod(),
                request.getReqTime(), HySdkConstans.SIGN_KEY));
    }



    private void postHandle(final String orderId,
                            final String method,
                            final int retCode,
                            final String requestParams,
                            final String returnParams,
                            final long millisecond) {
        final HyChannelRequestRecord record = HyChannelRequestRecord.builder().orderId(orderId).method(method).retCode(retCode).request(requestParams).response(returnParams).time(millisecond).build();
        this.hySdkRequestRecordService.record(record);
    }

    private String requestImpl(final String requestParams,
                               final String requestUrl,
                               final String orderId,
                               final String method,
                               final StopWatch stopWatch) {
        stopWatch.start();
        try {
            final String response = this.httpClientFacade.post(requestUrl, requestParams);
            stopWatch.stop();
            return response;
        } catch (final Throwable e) {
            this.postHandle(orderId, method ,0,requestParams,"error",stopWatch.getTime());
            stopWatch.stop();
            throw e;
        }
    }
}
