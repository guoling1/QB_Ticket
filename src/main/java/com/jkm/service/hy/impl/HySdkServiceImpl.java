package com.jkm.service.hy.impl;


import com.jkm.entity.HyChannelRequestRecord;
import com.jkm.entity.OrderForm;
import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.enums.EnumHTHYMethodCode;
import com.jkm.service.hy.HySdkRequestRecordService;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.*;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.HttpMethod;
import com.jkm.util.MD5Util;
import com.jkm.util.http.client.HttpClientFacade;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


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
            jsonObject.put("partnerid", HySdkConstans.ORDER_PARTNER_ID);
            jsonObject.put("passengers", passengers);
            jsonObject.put("method", EnumHTHYMethodCode.SUBMIT_ORDER_FORM.getCode());
            jsonObject.put("is_accept_standing", false);
            jsonObject.put("to_station_code", orderform.getToStationCode());
            jsonObject.put("train_date", orderform.getStartDate());
            jsonObject.put("callbackurl", HySdkConstans.SUBMIT_TICKET_NOTIFY_URL);
            jsonObject.put("reqtime", reqTime);
            jsonObject.put("from_station_name", orderform.getFromStationName());
            jsonObject.put("checi", orderform.getCheci());
            jsonObject.put("orderid", orderform.getOrderId());
            jsonObject.put("from_station_code", orderform.getFromStationCode());
            jsonObject.put("to_station_name", orderform.getToStationName());
            jsonObject.put("LoginUserName", orderform.getLoginUserName());
            jsonObject.put("LoginUserPassword", UserBankCardSupporter.decryptPwd(orderform.getLoginUserPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final StopWatch stopWatch = new StopWatch();
        final JSONObject resultJsonObject = HttpMethod.httpClient(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL);
        System.out.println("请求参数 : " + jsonObject);
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
            jsonObject.put("partnerid", HySdkConstans.ORDER_PARTNER_ID);
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
            jsonObject.put("partnerid", HySdkConstans.ORDER_PARTNER_ID);
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
     * @param orderId
     * @param transactionId
     * @return
     */
    @Override
    public JSONObject QueryOrder(final String orderId, final String transactionId) {
        JSONObject jsonObject = new JSONObject();
        String reqTime = this.getCurrentDateString();
        try {
            jsonObject.put("sign", this.getSign(EnumHTHYMethodCode.QUERY_ORDER_FORM.getCode(), reqTime));
            jsonObject.put("partnerid", HySdkConstans.ORDER_PARTNER_ID);
            jsonObject.put("method", EnumHTHYMethodCode.QUERY_ORDER_FORM.getCode());
            jsonObject.put("orderid", orderId);
            jsonObject.put("transactionid", transactionId);
            jsonObject.put("reqtime", reqTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final StopWatch stopWatch = new StopWatch();
        final JSONObject resultJsonObject = HttpMethod.httpClient(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL);
        this.postHandle(orderId,
                EnumHTHYMethodCode.QUERY_ORDER_FORM.getCode(),
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
    public HyReturnTicketResponse returnTicket(final HyReturnTicketRequest request, final JSONArray tickets) {
        JSONObject jsonObject = null;
        try {
            String sign = MD5Util.MD5(request.getPartnerId() + request.getMethod() + request.getReqTime() + MD5Util.MD5(HySdkConstans.ORDER_SIGN_KEY));
            jsonObject = new JSONObject();
            jsonObject.put("sign", sign);
            jsonObject.put("partnerid", request.getPartnerId());
            jsonObject.put("method", request.getMethod());
            jsonObject.put("callbackurl", HySdkConstans.REFUND_TICKET_NOTIFY_URL);
            jsonObject.put("reqtime", request.getReqTime());
            jsonObject.put("orderid", request.getOrderId());
            jsonObject.put("LoginUserName", request.getLoginUserName());
            jsonObject.put("LoginUserPassword", request.getLoginUserPassword());
            jsonObject.put("tickets", tickets);
            jsonObject.put("ordernumber", request.getOrderNumber());
            jsonObject.put("transactionid", request.getTransactionId());
            jsonObject.put("reqtoken", request.getReqToken());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final StopWatch stopWatch = new StopWatch();
        final JSONObject json = requestImpl(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL, request.getOrderId(), request.getMethod(), stopWatch);
        final Object obj = JSONObject.toBean(json, HyReturnTicketResponse.class);
        final HyReturnTicketResponse response = (HyReturnTicketResponse)obj;
        this.postHandle(request.getOrderId(),
                        request.getMethod(),
                        response.getCode(),
                        jsonObject.toString(),
                        json.toString(),
                        stopWatch.getTime());
        return response;
    }

/////////////////////******************火车票保险业务******************////////////////////////////////

    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    public JSONArray postPolicyOrder(HyPostPolicyOrderRequest request) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String sign = null;
        try {
            sign = MD5Util.MD5(request.getUsername() + request.getMethod() + request.getReqtime() + MD5Util.MD5(HySdkConstans.POLICY_SIGN_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("sign", sign);
        jsonObject.put("username", request.getUsername());
        jsonObject.put("reqtime", request.getReqtime());
        jsonObject.put("method", request.getMethod());
            JSONObject jo = new JSONObject();
            //=====================================================
            jo.put("InsProductNo", request.getInsProductNo());// 保险产品代码
            jo.put("FlightDate", request.getFlightDate());// 航班日期 String(yyyy-MM-dd)
            jo.put("FlightNumber", request.getFlightNumber());// 航班号/车次号
            jo.put("SerialNo", request.getSerialNo());// 交易流水号 需要 生成
            jo.put("ContractName", request.getContractName());// 保险人姓名
            jo.put("ContractType", request.getContractType());// 用户类型
            jo.put("Gender", request.getGender());// [M: 男性 F: 女性]
            jo.put("CardType", request.getCardType());// 证件类型
            jo.put("CardNo", request.getCardNo());// 证件号码
            jo.put("Birthday", request.getBirthday());// 出生日期 String (yyyy-MM-dd)
            jo.put("Phone", request.getPhone());// 联系电话
            jsonArray.add(jo);
        jsonObject.put("data", jsonArray);
        final StopWatch stopWatch = new StopWatch();
        final JSONArray json = requestImplToArray(jsonObject, HySdkConstans.POLICY_GATEWAY_URL, "", EnumHTHYMethodCode.POST_POLICY_ORDER.getCode(), stopWatch);
        this.postHandle("",
                EnumHTHYMethodCode.POST_POLICY_ORDER.getCode(),
                0,
                jsonObject.toString(),
                json.toString(),
                stopWatch.getTime());
        return json;
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject cancelPolicyOrder(HyCancelPolicyOrderRequest request) {
        JSONObject jsonObject = null;
        try {
            String sign = MD5Util.MD5(request.getUsername() + request.getMethod() + request.getReqtime() + MD5Util.MD5(HySdkConstans.POLICY_SIGN_KEY));
            jsonObject = new JSONObject();
            jsonObject.put("sign", sign);
            jsonObject.put("username", request.getUsername());
            jsonObject.put("reqtime", request.getReqtime());
            jsonObject.put("method", request.getMethod());
            jsonObject.put("policyNo", request.getPolicyNo());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final StopWatch stopWatch = new StopWatch();
        final JSONObject json = requestImpl(jsonObject,  HySdkConstans.POLICY_GATEWAY_URL, request.getPolicyNo(), request.getMethod(), stopWatch);
        this.postHandle(request.getPolicyNo(),
                request.getMethod(),
                json.getInt("code"),
                jsonObject.toString(),
                json.toString(),
                stopWatch.getTime());
        return json;
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject queryPolicyOrder(HyQueryPolicyOrderRequest request) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject grabTickets(JSONObject jsonObject) {
        final StopWatch stopWatch = new StopWatch();
        final JSONObject json = requestImpl(jsonObject,  HySdkConstans.GRAB_TICKET_URL, "", EnumHTHYMethodCode.QIANG_PIAO_ORDER.getCode(), stopWatch);
        this.postHandle("",
                EnumHTHYMethodCode.QIANG_PIAO_ORDER.getCode(),
                json.getInt("code"),
                jsonObject.toString(),
                json.toString(),
                stopWatch.getTime());
        return json;
    }

    /**
     * {@inheritDoc}
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject cancelGrabTickets(JSONObject jsonObject) {
        final StopWatch stopWatch = new StopWatch();
        final JSONObject json = requestImpl(jsonObject,  HySdkConstans.GRAB_TICKET_CANCEL_URL, "", "取消抢票", stopWatch);
        this.postHandle("",
                "取消抢票",
                json.getInt("code"),
                jsonObject.toString(),
                json.toString(),
                stopWatch.getTime());
        return json;
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

    //////////////////*************抢票业务***********////////////////////



    private JSONObject requestImpl(final JSONObject jsonObject,
                               final String requestUrl,
                               final String orderId,
                               final String method,
                               final StopWatch stopWatch) {
        stopWatch.start();
        final JSONObject json = HttpMethod.httpClient(jsonObject, requestUrl);
        stopWatch.stop();
        return json;
        /*try {
            final JSONObject json = HttpMethod.httpClient(jsonObject, requestUrl);
            stopWatch.stop();
            return json;
        } catch (final Throwable e) {
            this.postHandle(orderId, method ,0,jsonObject.toString(),"error",stopWatch.getTime());
            stopWatch.stop();
            throw e;
        }*/
    }

    private JSONArray requestImplToArray(final JSONObject jsonObject,
                                   final String requestUrl,
                                   final String orderId,
                                   final String method,
                                   final StopWatch stopWatch) {
        stopWatch.start();
        final JSONArray json = HttpMethod.httpClientToArray(jsonObject, requestUrl);
        stopWatch.stop();
        return json;
        /*try {
            final JSONArray json = HttpMethod.httpClientToArray(jsonObject, requestUrl);
            stopWatch.stop();
            return json;
        } catch (final Throwable e) {
            this.postHandle(orderId, method ,0,jsonObject.toString(),"error",stopWatch.getTime());
            stopWatch.stop();
            throw e;
        }*/
    }

    private String getSign(final String method, final String reqtime) {

        try {
            return MD5Util.MD5(HySdkConstans.ORDER_PARTNER_ID + method
                    + reqtime + MD5Util.MD5(HySdkConstans.ORDER_SIGN_KEY));
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
