package com.jkm.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.controller.helper.request.RequestBookTicket;
import com.jkm.entity.ContactForm;
import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.enums.EnumHTHYMethodCode;
import com.jkm.enums.EnumOrderFormDetailStatus;
import com.jkm.enums.EnumOrderFormStatus;
import com.jkm.service.ContactFormService;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.*;
import com.jkm.util.HttpMethod;
import com.jkm.util.MD5Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.SnGenerator;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Service
public class TicketServiceImpl implements TicketService {

    private static Logger log = Logger.getLogger(TicketServiceImpl.class);

    @Autowired
    private OrderFormDetailService orderFormDetailService;

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private ContactFormService contactFormService;

    @Autowired
    private YsSdkService ysSdkService;



    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public long submitOrder(final RequestBookTicket requestBookTicket) {

        final OrderForm orderForm = new OrderForm();
        final Optional<ContactForm> contactFormOptional = this.contactFormService.selectByUid(requestBookTicket.getUid());
        Preconditions.checkState(contactFormOptional.isPresent(), "订票人uid[" + requestBookTicket.getUid() + "]不村在");
        orderForm.setUid(requestBookTicket.getUid());
        orderForm.setPrice(requestBookTicket.getPrice());
        orderForm.setFromStationName(requestBookTicket.getFromStationName());
        orderForm.setFromStationCode(requestBookTicket.getFromStationCode());
        orderForm.setToStationName(requestBookTicket.getToStationName());
        orderForm.setToStationCode(requestBookTicket.getToStationCode());
        orderForm.setCheci(requestBookTicket.getCheci());
        //TODO
        orderForm.setLoginUserName(contactFormOptional.get().getUserName());
        orderForm.setLoginUserPassword(contactFormOptional.get().getUserName());
        orderForm.setOrderId(SnGenerator.generate());
        orderForm.setTrainDate(DateFormatUtil.parse(requestBookTicket.getTrainDate(), DateFormatUtil.yyyy_MM_dd));
        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getId());
        orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getValue());
        this.orderFormService.add(orderForm);
        final List<RequestBookTicket.Passenger> passengerList = requestBookTicket.getPassengers();
        Preconditions.checkState(CollectionUtils.isEmpty(passengerList), "乘客列表为空");
        final JSONArray passengerJsonArray = new JSONArray();
        Lists.transform(passengerList, new Function<RequestBookTicket.Passenger, OrderFormDetail>() {
            @Override
            public OrderFormDetail apply(RequestBookTicket.Passenger passenger) {
                final Optional<ContactForm> contactFormOptional1 = contactFormService.selectById(passenger.getId());
                Preconditions.checkState(contactFormOptional1.isPresent(), "乘客不村在");
                final ContactForm contactForm = contactFormOptional1.get();
                final OrderFormDetail orderFormDetail = new OrderFormDetail();
                final JSONObject passengerJsonObject = new JSONObject();
                orderFormDetail.setOrderFormId(orderForm.getId());
                orderFormDetail.setPassengerId(contactForm.getId());
                orderFormDetail.setPrice(requestBookTicket.getPrice());
                orderFormDetail.setCheci(orderForm.getCheci());
                //TODO
//                orderFormDetail.setPiaoType(contactForm.getp);
                orderFormDetail.setStatus(EnumOrderFormDetailStatus.TICKET_INITIALIZATION.getId());
                orderFormDetail.setRemark(EnumOrderFormDetailStatus.TICKET_INITIALIZATION.getValue());
                orderFormDetailService.add(orderFormDetail);

                passengerJsonObject.put("passengersename", "");
                passengerJsonObject.put("passportseno", "");
                passengerJsonObject.put("passporttypeseid", "");
                passengerJsonObject.put("passporttypeseidname", "");
                passengerJsonObject.put("zwcode", "");
                passengerJsonObject.put("zwname", "");
                passengerJsonObject.put("piaotype", "");
                passengerJsonObject.put("cxin", "");
                passengerJsonArray.add(passengerJsonObject);
                return null;
            }
        });

        final JSONObject jsonObject = this.submitOrderImpl(orderForm, passengerJsonArray);
        final String code = jsonObject.getString("code");
        final boolean success = jsonObject.getBoolean("success");
        if (success && ("802".equals(code) || "905".equals(code))) {
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getValue());
            this.orderFormService.update(orderForm);
        } else {
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
        }
        return orderForm.getId();
    }


    /**
     * 处理订票回调
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void handleSubmitOrderCallbackResponse(final JSONObject jsonObject) {
        final String orderId = jsonObject.getString("orderId");
        final boolean success = jsonObject.getBoolean("success");
        final boolean ordersuccess = jsonObject.getBoolean("ordersuccess");
        final JSONArray passengers = jsonObject.getJSONArray("passengers");
        Preconditions.checkState(!passengers.isEmpty(), "乘客列表为空了");
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByOrderId(orderId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderId + "]不存在");
        Preconditions.checkState(orderFormOptional.get().isRequestOccupySeatRequesting(), "订单[" + orderId + "]状态不正确");
        final Optional<OrderForm> orderFormOptional1 = this.orderFormService.selectByIdWithLock(orderFormOptional.get().getId());
        final OrderForm orderForm = orderFormOptional1.get();
        if (success && ordersuccess) {
            orderForm.setTotalPrice(new BigDecimal(jsonObject.getString("orderamount")));
            orderForm.setOutOrderId(jsonObject.getString("transactionid"));
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_TRUE.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_TRUE.getValue());
            this.orderFormService.update(orderForm);
            for (int i = 0; i < passengers.size(); i++) {
                final JSONObject passengersJo = passengers.getJSONObject(i);
                final int passengerid = passengersJo.getInt("passengerid");
                final String piaoType = passengersJo.getString("piaotype");
                final Optional<OrderFormDetail> orderFormDetailOptional =
                        this.orderFormDetailService.selectByOrderFormIdAndPassengerIdAndPiaoType(orderId, passengerid, piaoType);
                Preconditions.checkState(orderFormDetailOptional.isPresent(), "乘客的票的记录不存在");
                final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
                Preconditions.checkState(orderFormDetail.isTicketInit(), "乘客的票的记录[" + orderFormDetail.getId() + "]状态不正确");
                orderFormDetail.setTicketNo(passengersJo.getString("ticket_no"));
                this.orderFormDetailService.update(orderFormDetail);
            }
            //TODO
            //调用支付接口
        } else {
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     * @return
     */
    @Override
    public Pair<Boolean, String> confirmOrder(final long orderFormId) {

        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByIdWithLock(orderFormId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderFormId + "]不存在");
        final OrderForm orderForm = orderFormOptional.get();
        Preconditions.checkState(orderForm.isCustomerPaySuccess(), "订单[" + orderFormId + "]状态不正确");


        //请求第三方
        final JSONObject jsonObject = this.confirmTrainTicket(orderForm.getOrderId(), orderForm.getOutOrderId());
        final String code = jsonObject.getString("code");
        final boolean success = jsonObject.getBoolean("success");
        if (success && "100".equals(code)) {
            orderForm.setOrderNumber(jsonObject.getString("ordernumber"));
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_SUCCESS.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_SUCCESS.getValue());
            this.orderFormService.update(orderForm);
            return Pair.of(true, "确认出票请求成功");
        } else {
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());

            //TODO
            //退款
            return Pair.of(false, "确认出票请求失败");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param jsonObject
     */
    @Override
    public void handleConfirmOrderCallbackResponse(final JSONObject jsonObject) {
        final String orderId = jsonObject.getString("orderId");
        final String isSuccess = jsonObject.getString("isSuccess");
        final String code = jsonObject.getString("code");
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByOrderId(orderId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderId + "]不存在");
        Preconditions.checkState(orderFormOptional.get().confirmTicketRequestSuccess(), "订单[" + orderId + "]状态不正确");
        final Optional<OrderForm> orderFormOptional1 = this.orderFormService.selectByIdWithLock(orderFormOptional.get().getId());
        final OrderForm orderForm = orderFormOptional1.get();
        if ("Y".equals(isSuccess) && "100".equals(code)) {
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_TICKET_SUCCESS.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_TICKET_SUCCESS.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getId(), orderForm.getId());
        } else {
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_TICKET_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_TICKET_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
            //TODO
            //退款
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     * @return
     */
    @Override
    public Pair<Boolean, String> cancelOrder(final long orderFormId) {



        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Pair<Boolean, String> refund(final long orderFormDetailId) {
        final Optional<OrderFormDetail> orderFormDetailOptional = this.orderFormDetailService.selectById(orderFormDetailId);
        Preconditions.checkArgument(orderFormDetailOptional.isPresent() , "订单不存在");
        final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectById(orderFormDetail.getOrderFormId());
        Preconditions.checkArgument(orderFormOptional.isPresent() , "请求订单不存在");
        final OrderForm orderForm = orderFormOptional.get();
        final YsRefundTicketRequest request = YsRefundTicketRequest.builder().termTransID(SnGenerator.generate()).transID("")
                .passengerID("").build();
        request.setReqDateTime(DateFormatUtil.format(new Date(),"yyyyMMddHHmmss"));

        final YsRefundTicketResponse response = this.ysSdkService.refundTicket(request);
        if(response.getStatus().equals("1004")){
            return Pair.of(true , "退票请求成功");
        }else {
            return Pair.of(false, "退票请求失败");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param response
     */
    @Override
    public void handleRefundCallbackMsg(YsRefundCallbackResponse response) {
        //退款成功, 修改小订单状态 , 退款给客户
        switch (response.getRefundType()){
            case "1":
                return;
            case "2":
                return;
            default:
                return;
        }
    }


    /**
     * 订单提交
     *
     * @param orderform
     * @param passengers
     * @return
     */
    private JSONObject submitOrderImpl(final OrderForm orderform, final JSONArray passengers) {
        JSONObject jsonObject = new JSONObject();
        String reqtime = this.getCurrentDateString();
        try {
            jsonObject.put("sign", this.getSign(EnumHTHYMethodCode.SUBMIT_ORDER_FORM.getCode(), reqtime));
            jsonObject.put("partnerid", HySdkConstans.PARTNERID);
            jsonObject.put("passengers", passengers);
            jsonObject.put("method", EnumHTHYMethodCode.SUBMIT_ORDER_FORM.getCode());
            jsonObject.put("is_accept_standing", false);
            jsonObject.put("to_station_code", orderform.getToStationCode());
            jsonObject.put("train_date", orderform.getTrainDate());
            jsonObject.put("callbackurl", HySdkConstans.SUBMIT_TICKET_NOTIFY_URL);
            jsonObject.put("reqtime", reqtime);
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
        return HttpMethod.httpClient(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL);
    }

    /**
     * 确认出票
     *
     * @param orderid
     * @param transactionid
     * @return
     */
    private JSONObject confirmTrainTicket(String orderid, String transactionid) {
        JSONObject jsonObject = new JSONObject();
        String reqtime = this.getCurrentDateString();
        try {
            jsonObject.put("sign", this.getSign(EnumHTHYMethodCode.CONFIRM_ORDER_FORM.getCode(), reqtime));
            jsonObject.put("partnerid", HySdkConstans.PARTNERID);
            jsonObject.put("method", EnumHTHYMethodCode.CONFIRM_ORDER_FORM.getCode());
            jsonObject.put("orderid", orderid);
            jsonObject.put("reqtime", reqtime);
            jsonObject.put("transactionid", transactionid);
            jsonObject.put("reqtime", reqtime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpMethod.httpClient(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL);
    }

    /**
     * 取消订单
     *
     * @param orderid
     * @param transactionid
     * @return
     */
    private JSONObject cancelOrder(String orderid, String transactionid) {
        JSONObject jsonObject = new JSONObject();
        String reqtime = this.getCurrentDateString();
        try {
            jsonObject.put("sign", this.getSign(EnumHTHYMethodCode.CANCEL_ORDER_FORM.getCode(), reqtime));
            jsonObject.put("partnerid", HySdkConstans.PARTNERID);
            jsonObject.put("method", EnumHTHYMethodCode.CANCEL_ORDER_FORM.getCode());
            jsonObject.put("orderid", orderid);
            jsonObject.put("transactionid", transactionid);
            jsonObject.put("reqtime", reqtime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpMethod.httpClient(jsonObject, HySdkConstans.SERVICE_GATEWAY_URL);
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
