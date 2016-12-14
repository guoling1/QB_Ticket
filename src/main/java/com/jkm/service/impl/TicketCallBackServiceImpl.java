package com.jkm.service.impl;

import com.jkm.entity.*;
import com.jkm.enums.EnumGrabTicketStatus;
import com.jkm.enums.EnumOrderFormDetailStatus;
import com.jkm.enums.EnumOrderFormStatus;
import com.jkm.enums.EnumRefundTicketFlowStatus;
import com.jkm.service.*;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.service.hy.impl.HySdkServiceImpl;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.HttpMethod;
import com.jkm.util.MD5Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2016-12-12.
 *
 * 1.抢票成功 2.抢票失败 3.退票成功 4.退票失败 5.订票成功 6.订票失败
 */
@Service
public class TicketCallBackServiceImpl implements TicketCallBackService{
    private Logger log = Logger.getLogger(TicketCallBackServiceImpl.class);

    @Autowired
    private GrabTicketFormService grabTicketFormService;
    @Autowired
    private OrderFormService orderFormService;
    @Autowired
    private MerchantAppInfoService merchantAppInfoService;
    @Autowired
    private OrderFormDetailService orderFormDetailService;
    /**
     * {@inheritDoc}
     * @param orderForm
     */
    @Override
    public void orderFormCallBack(OrderForm orderForm) {
        final String[] split = orderForm.getUid().split("_");
        final long merchantId = Long.valueOf(split[0]);
        final String uid = split[1];
        final String reqTime = DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss);
        //根据appid查询该应用的相关密钥及url
        final MerchantAppInfo merchantAppInfo = this.merchantAppInfoService.selectByPrimaryKey(merchantId);
        final String appId = merchantAppInfo.getOpenId();
        final String key = merchantAppInfo.getSecretKey();
        //返回信息
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("sign", this.getSign(appId, uid, reqTime, key));
        jsonObject.put("uid", uid);
        jsonObject.put("reqTime", reqTime);
        jsonObject.put("appid", appId);
        jsonObject.put("orderId", orderForm.getOrderId());
        jsonObject.put("trainNo", orderForm.getCheci());
        jsonObject.put("seatTypeName", orderForm.getZwName());
        jsonObject.put("fromStation", orderForm.getFromStationName());
        jsonObject.put("toStation", orderForm.getToStationName());
        jsonObject.put("startDate", orderForm.getStartDate());
        jsonObject.put("endDate", orderForm.getEndDate());
        jsonObject.put("startTime", orderForm.getStartTime());
        jsonObject.put("endTime", orderForm.getEndTime());
        jsonObject.put("runTime", orderForm.getRunTime());
        final JSONArray jsonArray = new JSONArray();
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByOrderFormId(orderForm.getId());
        for (OrderFormDetail detail : orderFormDetails){
            JSONObject object = new JSONObject();
            object.put("passengerId", detail.getPassengerId());
            object.put("passengerName", detail.getPassengerName());
            object.put("ticketNo", detail.getTicketNo());
            object.put("price", detail.getPrice());
            object.put("ticketType", detail.getPiaoType());
            object.put("cxin", detail.getCxin());
            jsonArray.add(object);
        }
        jsonObject.put("passengers", jsonArray);
        if (orderForm.getStatus() == EnumOrderFormStatus.ORDER_FORM_TICKET_SUCCESS.getId()){
            //出票成功
            jsonObject.put("success", true);
            jsonObject.put("msg", "订票成功");
            jsonObject.put("type", 5);

        }else{
            //出票失败
            jsonObject.put("success", false);
            jsonObject.put("msg", "订票失败");
            jsonObject.put("type", 6);
        }
        //回调请求
        try{
            final JSONObject resultJsonObject = HttpMethod.httpClient(jsonObject, merchantAppInfo.getCallBackUrl());
            log.info("代购单[" + orderForm.getId() + "]推送给app消息:"+ jsonObject.toString() +",app返回信息:" + resultJsonObject.toString());
        }catch (final Throwable throwable){
            log.info(">>>>>>>>>>>>>>>"+ merchantAppInfo.getCallBackUrl()+">>>>>>>>..>>>>>>>.");
            log.error("代购单[" + orderForm.getId() + "]推送给app消息异常:" + throwable.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     * @param grabTicketForm
     */
    @Override
    public void grabFormCallBack(GrabTicketForm grabTicketForm) {

        final String[] split = grabTicketForm.getUid().split("_");
        final long merchantId = Long.valueOf(split[0]);
        final String uid = split[1];
        final String reqTime = DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss);
        //根据appid查询该应用的相关密钥及url
        final MerchantAppInfo merchantAppInfo = this.merchantAppInfoService.selectByPrimaryKey(merchantId);
        final String key = merchantAppInfo.getSecretKey();
        final String appId = merchantAppInfo.getOpenId();
        //返回信息
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("sign", this.getSign(appId, uid, reqTime, key));
        jsonObject.put("uid", uid);
        jsonObject.put("reqTime", reqTime);
        jsonObject.put("appid", appId);
        jsonObject.put("orderId", grabTicketForm.getOrderId());
        jsonObject.put("trainNo", grabTicketForm.getCheci());
        jsonObject.put("seatTypeName", grabTicketForm.getZwname());
        jsonObject.put("fromStation", grabTicketForm.getFromStationName());
        jsonObject.put("toStation", grabTicketForm.getToStationName());
        jsonObject.put("startDate", grabTicketForm.getStartDate());
        jsonObject.put("endDate", grabTicketForm.getEndDate());
        jsonObject.put("startTime", grabTicketForm.getStartTime());
        jsonObject.put("endTime", grabTicketForm.getEndTime());
        jsonObject.put("runTime", grabTicketForm.getRunTime());
        if (grabTicketForm.getStatus() == EnumGrabTicketStatus.GRAB_FORM_SUCCESS.getId()){
            //抢票成功
            jsonObject.put("success", true);
            jsonObject.put("msg", "抢票成功");
            jsonObject.put("type", 1);
            final JSONArray jsonArray = new JSONArray();
            final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByGrabTicketFormId(grabTicketForm.getId());
            for (OrderFormDetail detail : orderFormDetails){
                JSONObject object = new JSONObject();
                object.put("passengerId", detail.getPassengerId());
                object.put("passengerName", detail.getPassengerName());
                object.put("ticketNo", detail.getTicketNo());
                object.put("price", detail.getPrice());
                object.put("ticketType", detail.getPiaoType());
                object.put("cxin", detail.getCxin());
                jsonArray.add(object);
            }
            jsonObject.put("passengers", jsonArray);
        }else{
            //抢票失败
            jsonObject.put("passengers", grabTicketForm.getPassengerInfo());
            jsonObject.put("success", false);
            jsonObject.put("msg", "抢票失败");
            jsonObject.put("type", 2);
        }
        //回调请求
        try{
            final JSONObject resultJsonObject = HttpMethod.httpClient(jsonObject, merchantAppInfo.getCallBackUrl());
            log.info("抢票单[" + grabTicketForm.getId() + "]推送给app消息:"+ jsonObject.toString() +",app返回信息:" + resultJsonObject.toString());
        }catch (final Throwable throwable){
            log.error("抢票单[" + grabTicketForm.getId() + "]推送给app消息异常:" + throwable.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param refundTicketFlow
     */
    @Override
    public void refundTicketCallBack(RefundTicketFlow refundTicketFlow) {

        final OrderFormDetail orderFormDetail = this.orderFormDetailService.selectById(refundTicketFlow.getOrderFormDetailId()).get();
        if (refundTicketFlow.getGrabTicketFormId() != 0){
            //查询抢票单
            //查询大订单,获取appId , uid
            final GrabTicketForm grabTicketForm = this.grabTicketFormService.selectById(refundTicketFlow.getGrabTicketFormId()).get();
            final String[] split = grabTicketForm.getUid().split("_");
            final long merchantId = Long.valueOf(split[0]);
            final String uid = split[1];
            final String reqTime = DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss);
            //根据appid查询该应用的相关密钥及url
            final MerchantAppInfo merchantAppInfo = this.merchantAppInfoService.selectByPrimaryKey(merchantId);
            final String key = merchantAppInfo.getSecretKey();
            final String appId = merchantAppInfo.getOpenId();
            //返回信息
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("sign", this.getSign(appId, uid, reqTime, key));
            jsonObject.put("uid", uid);
            jsonObject.put("reqTime", reqTime);
            jsonObject.put("appid", appId);
            jsonObject.put("orderId", grabTicketForm.getOrderId());
            jsonObject.put("passengerId", orderFormDetail.getPassengerId());
            jsonObject.put("passengerName",orderFormDetail.getPassengerName());
            if (refundTicketFlow.getStatus() == EnumRefundTicketFlowStatus.REFUND_TICKET_SUCCESS.getId()){
                jsonObject.put("type", 3);
                jsonObject.put("success",true);
                jsonObject.put("msg", "退票成功");
            }else{
                jsonObject.put("type", 4);
                jsonObject.put("success", false);
                jsonObject.put("msg", "退票失败");
            }
            try{
                final JSONObject resultJsonObject = HttpMethod.httpClient(jsonObject, merchantAppInfo.getCallBackUrl());
                log.info("抢票单[" + grabTicketForm.getId() + "]推送给app消息:"+ jsonObject.toString() +",app返回信息:" + resultJsonObject.toString());
            }catch (final Throwable throwable){
                log.error("抢票单[" + grabTicketForm.getId() + "]推送给app消息异常:" + throwable.getMessage());
            }
        }

        if (refundTicketFlow.getOrderFormId() != 0){
            //查询代购单
            //查询大订单,获取appId , uid
            final OrderForm orderForm = this.orderFormService.selectById(refundTicketFlow.getOrderFormId()).get();
            final String[] split = orderForm.getUid().split("_");
            final long merchantId = Long.valueOf(split[0]);
            final String uid = split[1];
            final String reqTime = DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss);
            //根据appid查询该应用的相关密钥及url
            final MerchantAppInfo merchantAppInfo = this.merchantAppInfoService.selectByPrimaryKey(merchantId);
            final String key = merchantAppInfo.getSecretKey();
            final String appId = merchantAppInfo.getOpenId();
            //返回信息
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("sign", this.getSign(appId, uid, reqTime, key));
            jsonObject.put("uid", uid);
            jsonObject.put("reqTime", reqTime);
            jsonObject.put("appid", appId);
            jsonObject.put("orderId", orderForm.getOrderId());
            jsonObject.put("passengerId", orderFormDetail.getPassengerId());
            jsonObject.put("passengerName",orderFormDetail.getPassengerName());
            if (refundTicketFlow.getStatus() == EnumRefundTicketFlowStatus.REFUND_TICKET_SUCCESS.getId()){
                jsonObject.put("type", 3);
                jsonObject.put("success",true);
                jsonObject.put("msg", "退票成功");
            }else{
                jsonObject.put("type", 4);
                jsonObject.put("success", false);
                jsonObject.put("msg", "退票失败");
            }
            //回调请求
            try{
                final JSONObject resultJsonObject = HttpMethod.httpClient(jsonObject, merchantAppInfo.getCallBackUrl());
                log.info("代购单[" + orderForm.getId() + "]推送给app消息:"+ jsonObject.toString() +",app返回信息:" + resultJsonObject.toString());
            }catch (final Throwable throwable){
                log.error("代购单[" + orderForm.getId() + "]推送给app消息异常:" + throwable.getMessage());
            }

        }
    }

    private String getSign(final String appId, final String uid, final String reqTime, final String key) {

        try {
            return MD5Util.MD5(appId + uid + reqTime + MD5Util.MD5(key));
        } catch (final Exception e) {
            log.info(e);
            e.printStackTrace();
        }
        return "";
    }
}
