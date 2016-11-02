package com.jkm.service.hy.impl;


import com.jkm.entity.HyChannelRequestRecord;
import com.jkm.service.hy.HySdkRequestRecordService;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.*;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.service.hy.helper.HySdkSignUtil;
import com.jkm.util.HttpMethod;
import com.jkm.util.JsonUtil;
import com.jkm.util.MD5Util;
import com.jkm.util.http.client.HttpClientFacade;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    public HySubmitOrderResponse submitOrder(final HySubmitOrderRequest request) {
        addSign(request);
        final Map<String, String> resultMap = request.converterToMap();
        final JSONObject jsonObject = JSONObject.fromObject(resultMap);
        final StopWatch stopWatch = new StopWatch();


        final HySubmitOrderResponse response = new HySubmitOrderResponse();
        this.postHandle(request.getOrderId(),
                request.getMethod(),
                response.getCode(),
                resultMap.toString(),
                response.toString(),
                stopWatch.getTime());
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    public HyReturnTicketResponse returnTicket(final HyReturnTicketRequest request) {
        JSONObject jsonObject = null;
        try {
            String sign = MD5Util.MD5(request.getPartnerId() + request.getMethod() + request.getReqTime() + MD5Util.MD5(HySdkConstans.SIGN_KEY));
            jsonObject = new JSONObject();
            jsonObject.put("sign", sign);
            jsonObject.put("partnerid", request.getPartnerId());
            jsonObject.put("method", request.getMethod());
            jsonObject.put("callbackurl", HySdkConstans.REFUND_TICKET_NOTIFY_URL);
            jsonObject.put("reqtime", request.getReqTime());
            jsonObject.put("orderid", request.getOrderId());
            jsonObject.put("LoginUserName", request.getLoginUserName());
            jsonObject.put("LoginUserPassword", request.getLoginUserPassword());
            jsonObject.put("tickets", request.getTickets());
            jsonObject.put("ordernumber", request.getOrderNumber());
            jsonObject.put("transactionid", request.getTransactionId());
            jsonObject.put("reqtoken", request.getReqToken());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        final StopWatch stopWatch = new StopWatch();
        json = HttpMethod.httpClient(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL);
        final Object obj = JSONObject.toBean(json, HyReturnTicketResponse.class);
        final HyReturnTicketResponse response = (HyReturnTicketResponse)obj;
        this.postHandle(request.getOrderId(),
                        request.getMethod(),
                        response.getCode(),
                        json.toString(),
                        response.toString(),
                        stopWatch.getTime());
        return response;
    }

    private void addSign(final HySdkRequest request) {
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
