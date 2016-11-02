package com.jkm.service.hy.impl;


import com.jkm.entity.HyChannelRequestRecord;
import com.jkm.entity.OrderForm;
import com.jkm.enums.EnumHTHYMethodCode;
import com.jkm.service.hy.HySdkRequestRecordService;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.*;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.service.hy.helper.HySdkSignUtil;
import com.jkm.util.HttpMethod;
import com.jkm.util.MD5Util;
import com.jkm.util.http.client.HttpClientFacade;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by yuxiang on 2016-11-01.
 */
@Service
public class HySdkServiceImpl implements HySdkService{

    private Logger log = Logger.getLogger(HySdkServiceImpl.class);

    @Autowired
    private HttpClientFacade httpClientFacade;
    @Autowired
    private HySdkRequestRecordService hySdkRequestRecordService;


    /**
     * {@inheritDoc}
     *
     * @param orderform
     * @param passengers
     * @return
     */
    public JSONObject submitOrderImpl(final OrderForm orderform, final JSONArray passengers) {
        JSONObject jsonObject = new JSONObject();
        String reqTime = this.getCurrentDateString();
        try {
            jsonObject.put("sign", this.getSign(EnumHTHYMethodCode.SUBMIT_ORDER_FORM.getCode(), reqTime));
            jsonObject.put("partnerid", HySdkConstans.PARTNERID);
            jsonObject.put("passengers", passengers);
            jsonObject.put("method", EnumHTHYMethodCode.SUBMIT_ORDER_FORM.getCode());
            jsonObject.put("is_accept_standing", false);
            jsonObject.put("to_station_code", orderform.getToStationCode());
            jsonObject.put("train_date", orderform.getTrainDate());
            jsonObject.put("callbackurl", HySdkConstans.SUBMIT_TICKET_NOTIFY_URL);
            jsonObject.put("reqtime", reqTime);
            jsonObject.put("from_station_name", orderform.getFromStationName());
            jsonObject.put("checi", orderform.getCheci());
            jsonObject.put("orderid", orderform.getOrderId());
            jsonObject.put("from_station_code", orderform.getFromStationCode());
            jsonObject.put("to_station_name", orderform.getToStationCode());
            jsonObject.put("LoginUserName", orderform.getLoginUserName());
            jsonObject.put("LoginUserPassword", orderform.getLoginUserPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final StopWatch stopWatch = new StopWatch();
        final JSONObject resultJsonObject = HttpMethod.httpClient(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL);
        this.postHandle(orderform.getOrderId(),
                EnumHTHYMethodCode.SUBMIT_ORDER_FORM.getCode(),
                resultJsonObject.getInt("code"),
                jsonObject.toString(),
                resultJsonObject.toString(),
                stopWatch.getTime());
        return resultJsonObject;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param transactionId
     * @return
     */
    public JSONObject confirmTrainTicket(final String orderId, final String transactionId) {
        JSONObject jsonObject = new JSONObject();
        String reqTime = this.getCurrentDateString();
        try {
            jsonObject.put("sign", this.getSign(EnumHTHYMethodCode.CONFIRM_ORDER_FORM.getCode(), reqTime));
            jsonObject.put("partnerid", HySdkConstans.PARTNERID);
            jsonObject.put("method", EnumHTHYMethodCode.CONFIRM_ORDER_FORM.getCode());
            jsonObject.put("orderid", orderId);
            jsonObject.put("reqtime", reqTime);
            jsonObject.put("transactionid", transactionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final StopWatch stopWatch = new StopWatch();
        final JSONObject resultJsonObject = HttpMethod.httpClient(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL);
        this.postHandle(orderId,
                EnumHTHYMethodCode.CONFIRM_ORDER_FORM.getCode(),
                resultJsonObject.getInt("code"),
                jsonObject.toString(),
                resultJsonObject.toString(),
                stopWatch.getTime());
        return resultJsonObject;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param transactionId
     * @return
     */
    public JSONObject cancelOrder(final String orderId, final String transactionId) {
        JSONObject jsonObject = new JSONObject();
        String reqTime = this.getCurrentDateString();
        try {
            jsonObject.put("sign", this.getSign(EnumHTHYMethodCode.CANCEL_ORDER_FORM.getCode(), reqTime));
            jsonObject.put("partnerid", HySdkConstans.PARTNERID);
            jsonObject.put("method", EnumHTHYMethodCode.CANCEL_ORDER_FORM.getCode());
            jsonObject.put("orderid", orderId);
            jsonObject.put("transactionid", transactionId);
            jsonObject.put("reqtime", reqTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final StopWatch stopWatch = new StopWatch();
        final JSONObject resultJsonObject = HttpMethod.httpClient(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL);
        this.postHandle(orderId,
                EnumHTHYMethodCode.CANCEL_ORDER_FORM.getCode(),
                resultJsonObject.getInt("code"),
                jsonObject.toString(),
                resultJsonObject.toString(),
                stopWatch.getTime());
        return resultJsonObject;
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

    private String getSign(final String method, final String reqtime) {

        try {
            return MD5Util.MD5(HySdkConstans.PARTNERID + method
                    + reqtime + MD5Util.MD5(HySdkConstans.SIGN_KEY));
        } catch (final Exception e) {
            log.info(e);
            e.printStackTrace();
        }
        return "";
    }

    private String getCurrentDateString() {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        return date.format(new Date());
    }
}
