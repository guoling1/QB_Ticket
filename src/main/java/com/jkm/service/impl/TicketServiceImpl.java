package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.controller.helper.request.RequestGrabTicket;
import com.jkm.controller.helper.request.RequestSubmitOrder;
import com.jkm.entity.*;
import com.jkm.entity.fusion.SingleRefundData;
import com.jkm.enums.*;
import com.jkm.helper.InsurancePolicyUtil;
import com.jkm.helper.TicketMessageParams.SendBuyTicketFailParam;
import com.jkm.helper.TicketMessageParams.SendBuyTicketSuccessParam;
import com.jkm.helper.TicketMessageParams.SendGrabTicketFailParam;
import com.jkm.helper.TicketMessageParams.SendGrabTicketSuccessParam;
import com.jkm.service.*;
import com.jkm.service.ContactInfoService;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.service.hy.entity.HyCancelPolicyOrderRequest;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.service.hy.HySdkService;
import com.jkm.util.MD5Util;
import com.jkm.util.mq.MqConfig;
import com.jkm.util.mq.MqProducer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.SnGenerator;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Service
public class TicketServiceImpl implements TicketService {

    private static Logger log = Logger.getLogger(TicketServiceImpl.class);

    /**
     * 单位分钟
     */
    private static final int PAYMENT_MINUTE = 15;

    @Autowired
    private OrderFormDetailService orderFormDetailService;

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private ContactInfoService contactInfoService;

    @Autowired
    private HySdkService hySdkService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ChargeMoneyOrderService chargeMoneyOrderService;

    @Autowired
    private RefundTicketFlowService refundTicketFlowService;

    @Autowired
    private PolicyOrderService policyOrderService;

    @Autowired
    private ReturnMoneyOrderService returnMoneyOrderService;
    @Autowired
    private AuthenService authenService;

    @Autowired
    private RefundOrderFlowService refundOrderFlowService;

    @Autowired
    private QueryTicketPriceService queryTicketPriceService;

    @Autowired
    private GrabTicketFormService grabTicketFormService;

    @Autowired
    private TicketSendMessageService ticketSendMessageService;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public Triple<Boolean, String, Long> submitOrder(final RequestSubmitOrder requestSubmitOrder) {
        log.info("开始创建订单！！");
        final OrderForm orderForm = new OrderForm();
        this.userInfoService.insertUser(requestSubmitOrder.getUid(), requestSubmitOrder.getMobile());
//        final Optional<TbContactInfo> contactInfoOptional = this.contactInfoService.selectByUid(requestSubmitOrder.getUid());
//        Preconditions.checkState(contactInfoOptional.isPresent(), "订票人uid[" + requestSubmitOrder.getUid() + "]不存在");
        final UserInfo userInfo = this.userInfoService.selectByUid(requestSubmitOrder.getUid());
        final List<RequestSubmitOrder.Passenger> passengerList = requestSubmitOrder.getPassengers();
        Preconditions.checkState(!CollectionUtils.isEmpty(passengerList), "乘客列表为空");
        orderForm.setUid(requestSubmitOrder.getUid());
        orderForm.setMobile(requestSubmitOrder.getMobile());
        orderForm.setPrice(requestSubmitOrder.getPrice());
        orderForm.setBuyTicketPackageId(requestSubmitOrder.getBuyTicketPackageId());
        orderForm.setBuyTicketPackagePrice(new BigDecimal(EnumBuyTicketPackageType.of(requestSubmitOrder.getBuyTicketPackageId()).getPrice())
                .multiply(new BigDecimal(String.valueOf(passengerList.size()))).setScale(BigDecimal.ROUND_HALF_UP, 2));
        orderForm.setTicketTotalPrice(new BigDecimal("0.00"));
        orderForm.setGrabTicketPackageId(0);
        orderForm.setGrabTicketPackagePrice(new BigDecimal("0.00"));
        orderForm.setTotalPrice(new BigDecimal("0.00"));
        orderForm.setFromStationName(requestSubmitOrder.getFromStationName());
        orderForm.setFromStationCode(requestSubmitOrder.getFromStationCode());
        orderForm.setToStationName(requestSubmitOrder.getToStationName());
        orderForm.setToStationCode(requestSubmitOrder.getToStationCode());
        orderForm.setZwCode(requestSubmitOrder.getZwCode());
        orderForm.setZwName(EnumTrainTicketSeatType.of(requestSubmitOrder.getZwCode()).getValue());
        orderForm.setStartDate(requestSubmitOrder.getStartDate());
        orderForm.setEndDate(requestSubmitOrder.getEndDate());
        orderForm.setStartTime(requestSubmitOrder.getStartTime());
        orderForm.setEndTime(requestSubmitOrder.getEndTime());
        orderForm.setRunTime(requestSubmitOrder.getRunTime());
        orderForm.setCheci(requestSubmitOrder.getCheci());
        if (null != userInfo) {
            orderForm.setLoginUserName(userInfo.getAccount());
            orderForm.setLoginUserPassword(userInfo.getPwd());
        }
        orderForm.setOrderId(SnGenerator.generate());
        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getId());
        orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getValue());
        this.orderFormService.add(orderForm);
        final JSONArray passengerJsonArray = new JSONArray();
        for (RequestSubmitOrder.Passenger passenger : passengerList) {
            final Optional<TbContactInfo> contactInfoOptional1 = this.contactInfoService.selectById(passenger.getId());
            Preconditions.checkState(contactInfoOptional1.isPresent(), "乘客不存在");
            final TbContactInfo contactInfo = contactInfoOptional1.get();
            final OrderFormDetail orderFormDetail = new OrderFormDetail();
            final JSONObject passengerJsonObject = new JSONObject();
            orderFormDetail.setOrderFormId(orderForm.getId());
            orderFormDetail.setGrabTicketFormId(0);
            orderFormDetail.setIsGrab(0);
            orderFormDetail.setMobile(contactInfo.getTel());
            orderFormDetail.setPassengerId(contactInfo.getId());
            orderFormDetail.setPassengerName(contactInfo.getName());
            orderFormDetail.setPassportSeNo(contactInfo.getIdenty());
            orderFormDetail.setPassportTypeSeId(contactInfo.getIdentyType());
            orderFormDetail.setPassportTypeSeName(EnumCertificatesType.of(contactInfo.getIdentyType()).getValue());
            orderFormDetail.setPrice(requestSubmitOrder.getPrice());
            orderFormDetail.setCheci(orderForm.getCheci());
            orderFormDetail.setPiaoType(passenger.getPiaoType());
            orderFormDetail.setStatus(EnumOrderFormDetailStatus.TICKET_INITIALIZATION.getId());
            orderFormDetail.setRemark(EnumOrderFormDetailStatus.TICKET_INITIALIZATION.getValue());
            this.orderFormDetailService.add(orderFormDetail);
            passengerJsonObject.put("passengersename", contactInfo.getName());
            passengerJsonObject.put("passportseno", orderFormDetail.getPassportSeNoPlain());
            passengerJsonObject.put("passporttypeseid", contactInfo.getIdentyType());
            passengerJsonObject.put("passporttypeseidname", EnumCertificatesType.of(contactInfo.getIdentyType()).getValue());
            passengerJsonObject.put("passengerid", contactInfo.getId());
            passengerJsonObject.put("price", orderForm.getPrice());
            passengerJsonObject.put("zwcode", orderForm.getZwCode());
            passengerJsonObject.put("zwname", orderForm.getZwName());
            passengerJsonObject.put("piaotype", orderFormDetail.getPiaoType());
            passengerJsonObject.put("piaotypename", EnumTrainTicketType.of(orderFormDetail.getPiaoType()).getValue());
            passengerJsonObject.put("cxin", "");
            passengerJsonArray.add(passengerJsonObject);
        }
        log.info("发送订单提交请求！！");
        final JSONObject jsonObject = this.hySdkService.submitOrderImpl(orderForm, passengerJsonArray);
//        final String code = jsonObject.getString("code");
        final boolean success = jsonObject.getBoolean("success");
        if (success) {// && ("802".equals(code) || "905".equals(code))
            log.info("订单提交受理成功(占座请求成功)--等待回调！！！");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getValue());
            this.orderFormService.update(orderForm);
            return Triple.of(true, jsonObject.getString("msg"), orderForm.getId());
        } else {
            log.info("占座请求失败!request:[" + jsonObject.toString() + "]");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
            return Triple.of(false, jsonObject.getString("msg"), 0L);
        }
    }


    /**
     * {@inheritDoc}
     *
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional
    public void handleSubmitOrderCallbackResponse(final JSONObject jsonObject) {
        final String orderId = jsonObject.getString("orderid");
        log.info("订单[" + orderId + "]提交-回调处理中");
        final boolean success = jsonObject.getBoolean("success");
        final boolean orderSuccess = jsonObject.getBoolean("ordersuccess");
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByOrderId(orderId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderId + "]不存在");
        if (orderFormOptional.get().isOccupySuccessOrFail()) {//处理可能的多次回调
            return;
        }
        Preconditions.checkState(orderFormOptional.get().isRequestOccupySeatRequesting(), "订单[" + orderId + "]状态不正确");
        final Optional<OrderForm> orderFormOptional1 = this.orderFormService.selectByIdWithLock(orderFormOptional.get().getId());
        final OrderForm orderForm = orderFormOptional1.get();
        Preconditions.checkState(orderForm.isRequestOccupySeatRequesting(), "订单[" + orderId + "]状态不正确");
        if (success && orderSuccess) {
            final JSONArray passengers = jsonObject.getJSONArray("passengers");
            Preconditions.checkState(!passengers.isEmpty(), "乘客列表为空了");
            log.info("订单回调处理成功---占座成功");
            orderForm.setTicketTotalPrice(new BigDecimal(jsonObject.getString("orderamount")));
            orderForm.setTicketTotalPrice(new BigDecimal(jsonObject.getString("orderamount")));
            orderForm.setTotalPrice(orderForm.getTicketTotalPrice().add(orderForm.getBuyTicketPackagePrice()).add(orderForm.getGrabTicketPackagePrice()));
            orderForm.setOutOrderId(jsonObject.getString("transactionid"));
            orderForm.setExpireTime(new DateTime(new Date()).plusMinutes(PAYMENT_MINUTE).toDate());
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_TRUE.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_TRUE.getValue());
            this.orderFormService.update(orderForm);
            for (int i = 0; i < passengers.size(); i++) {
                final JSONObject passengersJo = passengers.getJSONObject(i);
                final int passengerId = passengersJo.getInt("passengerid");
                final String piaoType = passengersJo.getString("piaotype");
                final Optional<OrderFormDetail> orderFormDetailOptional =
                        this.orderFormDetailService.selectByOrderFormIdAndPassengerIdAndPiaoType(orderForm.getId(), passengerId, piaoType);
                Preconditions.checkState(orderFormDetailOptional.isPresent(), "乘客的票的记录不存在");
                final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
                Preconditions.checkState(orderFormDetail.isTicketInit(), "乘客的票的记录[" + orderFormDetail.getId() + "]状态不正确");
                orderFormDetail.setTicketNo(passengersJo.getString("ticket_no"));
                orderFormDetail.setCxin(passengersJo.getString("cxin"));
                this.orderFormDetailService.update(orderFormDetail);
            }

            //########################## 处理保险策略 ################################
            if (InsurancePolicyUtil.isOpenPolicy && !orderForm.isBuyInsurance()) {
                this.handleInsurancePolicy(orderForm);
                return;
            }
            //###########################################################
            //创建商户收款记录
            final ChargeMoneyOrder chargeMoneyOrder = new ChargeMoneyOrder();
            chargeMoneyOrder.setOrderFormId(orderForm.getId());
            chargeMoneyOrder.setGrabTicketFormId(0);
            chargeMoneyOrder.setTotalAmount(orderForm.getTotalPrice());
            chargeMoneyOrder.setBuyTicketPackage(orderForm.getBuyTicketPackagePrice());
            chargeMoneyOrder.setGrabTicketPackage(orderForm.getGrabTicketPackagePrice());
            chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.INIT.getId());
            chargeMoneyOrderService.init(chargeMoneyOrder);
            //放入消息队列
            JSONObject mqJo = new JSONObject();
            mqJo.put("orderFormId",orderForm.getId());
            MqProducer.sendMessage(mqJo, MqConfig.TICKET_CANCEL_EXPIRED_ORDER, PAYMENT_MINUTE * 60 * 1000);
        } else {
            log.info("订单回调处理成功---占座失败");
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
     * @param orderForm
     * @return
     */
    @Override
    @Transactional
    public void confirmOrder(final OrderForm orderForm) {
        log.info("订单[" + orderForm.getId() + "]--确认订单请求中");
        Preconditions.checkState(orderForm.isCustomerPaySuccess(), "订单[" + orderForm.getId() + "]状态不正确");

        final JSONObject jsonObject = this.hySdkService.confirmTrainTicket(orderForm.getOrderId(), orderForm.getOutOrderId());
//        final String code = jsonObject.getString("code");
        final boolean success = jsonObject.getBoolean("success");
        if (success) {
            log.info("订单[" + orderForm.getId() + "]--确认订单请求受理成功");
            orderForm.setOrderNumber(jsonObject.getString("ordernumber"));
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_SUCCESS.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_SUCCESS.getValue());
            this.orderFormService.update(orderForm);
        } else {
            log.info("订单[" + orderForm.getId() + "]--确认订单受理失败");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
            //退款
            final RefundOrderFlow refundOrderFlow = this.initRefundOrderFlow(orderForm);
            this.orderRefund(refundOrderFlow, orderForm);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param jsonObject
     */
    @Override
    @Transactional
    public void handleConfirmOrderCallbackResponse(final JSONObject jsonObject) {
        log.info("确认订单--回调函数处理中");
        final String orderId = jsonObject.getString("orderid");
        final String isSuccess = jsonObject.getString("isSuccess");
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByOrderId(orderId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderId + "]不存在");
        if (orderFormOptional.get().isBuySuccessOrFail()) {//处理多次回调
            return;
        }
        Preconditions.checkState(orderFormOptional.get().confirmTicketRequestSuccess(), "订单[" + orderId + "]状态不正确");
        final Optional<OrderForm> orderFormOptional1 = this.orderFormService.selectByIdWithLock(orderFormOptional.get().getId());
        final OrderForm orderForm = orderFormOptional1.get();
        Preconditions.checkState(orderForm.confirmTicketRequestSuccess(), "订单[" + orderId + "]状态不正确");
        if ("Y".equals(isSuccess)) {
            log.info("确认订单回调函数--出票成功");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_TICKET_SUCCESS.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_TICKET_SUCCESS.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getId(), orderForm.getId());
            try {
                this.policyOrderService.batchBuyPolicy(orderForm.getId());
            } catch (final Throwable throwable) {
                log.error("订单[" + orderForm.getId() + "] 买保险异常", throwable);
            }
            //短信通知
            final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByOrderFormId(orderForm.getId());
            for (OrderFormDetail orderFormDetail : orderFormDetails) {
                final SendBuyTicketSuccessParam sendBuyTicketSuccessParam = new SendBuyTicketSuccessParam();
                sendBuyTicketSuccessParam.setUid(orderForm.getUid());
                sendBuyTicketSuccessParam.setMobile(orderForm.getMobile());
                sendBuyTicketSuccessParam.setStartStation(orderForm.getFromStationName());
                sendBuyTicketSuccessParam.setEndStation(orderForm.getToStationName());
                sendBuyTicketSuccessParam.setTrainNo(orderForm.getCheci());
                sendBuyTicketSuccessParam.setStartDate(orderForm.getStartDate());
                sendBuyTicketSuccessParam.setStartTime(orderForm.getStartTime());
                sendBuyTicketSuccessParam.setTicketNo(orderFormDetail.getTicketNo());
                this.ticketSendMessageService.sendBuyTicketSuccessMessage(sendBuyTicketSuccessParam);
            }
        } else {
            log.info("确认订单回调函数--出票失败");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_TICKET_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_TICKET_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
            //退款
            final RefundOrderFlow refundOrderFlow = this.initRefundOrderFlow(orderForm);
            this.orderRefund(refundOrderFlow, orderForm);
            //短信通知
            final SendBuyTicketFailParam sendBuyTicketFailParam = new SendBuyTicketFailParam();
            sendBuyTicketFailParam.setUid(orderForm.getUid());
            sendBuyTicketFailParam.setMobile(orderForm.getMobile());
            sendBuyTicketFailParam.setStartStation(orderForm.getFromStationName());
            sendBuyTicketFailParam.setEndStation(orderForm.getToStationName());
            sendBuyTicketFailParam.setTrainNo(orderForm.getCheci());
            this.ticketSendMessageService.sendBuyTicketFailMessage(sendBuyTicketFailParam);

        }

    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     * @return
     */
    @Override
    @Transactional
    public Pair<Boolean, String> cancelOrder(final long orderFormId) {
        log.info("订单[" + orderFormId + "]取消中！！");
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByIdWithLock(orderFormId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderFormId + "]不存在");
        final OrderForm orderForm = orderFormOptional.get();
        Preconditions.checkState(orderForm.isCanCancelOrder(), "订单[" + orderFormId + "]状态不正确");

        final JSONObject jsonObject = this.hySdkService.cancelOrder(orderForm.getOrderId(), orderForm.getOutOrderId());
        final boolean success = jsonObject.getBoolean("success");
        if (success) {
            log.info("订单[" + orderFormId + "]取消请求成功！！");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CANCEL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CANCEL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_ORDER_CANCEL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_ORDER_CANCEL.getId(), orderForm.getId());
            //如果客户支付成功，退款
//            if (orderForm.isCustomerPaySuccess()) {
//                final RefundOrderFlow refundOrderFlow = this.initRefundOrderFlow(orderForm);
//                this.orderRefund(refundOrderFlow, orderForm);
//            }
            return Pair.of(true, jsonObject.getString("msg"));
        }
        log.error("订单[" + orderFormId + "]取消失败，原因：[" + jsonObject.getString("msg") + "]");
        return Pair.of(false, jsonObject.getString("msg"));
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public Pair<Boolean, String> refund(final long orderFormDetailId) {
        final Optional<OrderFormDetail> orderFormDetailOptional = this.orderFormDetailService.selectById(orderFormDetailId);
        Preconditions.checkArgument(orderFormDetailOptional.isPresent() , "订单不存在");
        final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
        //判断订单的状态是否可退
        if(orderFormDetail.getStatus() != EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getId()){
            return Pair.of(false, "此订单不能退票");
        }
        //判断是否是 代购票还是抢购票
        if (orderFormDetail.getIsGrab() == 0){
            //是代购票
            final Optional<OrderForm> orderFormOptional = this.orderFormService.selectById(orderFormDetail.getOrderFormId());
            Preconditions.checkArgument(orderFormOptional.isPresent() , "请求订单不存在");
            final OrderForm orderForm = orderFormOptional.get();
            // 申请时间距离发车时间不到2小时或已经超过发出时间时，不可退票
            final Date applyTime = new Date();
            //发车时间
            final Date startTime = DateFormatUtil.parse(orderForm.getStartDate()+" "+orderForm.getStartTime()+ ":00",
                    DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            if((applyTime.getTime() + 2*60*60*1000 + 1*60*1000) >= startTime.getTime()){
                return Pair.of(false , "距离发车时间不到2小时,不能退票");
            }
            //可退票, 创建退票流水订单
            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUESTING);
            final RefundTicketFlow flow = new RefundTicketFlow();
            flow.setOrderFormId(orderForm.getId());
            flow.setGrabTicketFormId(0);
            flow.setOrderFormDetailId(orderFormDetailId);
            flow.setApplyTime(applyTime);
            flow.setTicketNo(orderFormDetail.getTicketNo());
            flow.setStatus(EnumRefundTicketFlowStatus.INIT.getId());
            this.refundTicketFlowService.init(flow);
            //向航天华有发退票请求
            final HyReturnTicketRequest request = new HyReturnTicketRequest();
            request.setPartnerId(HySdkConstans.ORDER_PARTNER_ID);
            request.setMethod(EnumHTHYMethodCode.RETURN_TICKET.getCode());
            request.setReqTime(DateFormatUtil.format(new Date(),DateFormatUtil.yyyyMMddHHmmss));
            request.setCallBackUrl(HySdkConstans.REFUND_TICKET_NOTIFY_URL);
            request.setOrderId(orderForm.getOrderId());
            request.setOrderNumber(orderForm.getOrderNumber());
            request.setReqToken(SnGenerator.generate());
            request.setTransactionId(orderForm.getOutOrderId());
            JSONObject obj = new JSONObject();
            //查询乘客信息
            final Optional<TbContactInfo> optional = this.contactInfoService.selectById(orderFormDetail.getPassengerId());
            Preconditions.checkArgument(optional.isPresent(), "乘客信息不存在");
            final TbContactInfo tbContactInfo = optional.get();
            obj.put("passengername", tbContactInfo.getName());
            obj.put("passportseno",tbContactInfo.getIdenty());
            obj.put("passporttypeseid",tbContactInfo.getIdentyType());
            obj.put("ticket_no",orderFormDetail.getTicketNo());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(obj);
            try{
                //发请求
                log.info("订单号:"+ orderFormDetailId + "请求退票, 退票请求中......");
                final HyReturnTicketResponse response = this.hySdkService.returnTicket(request, jsonArray);
                if(response.getSuccess().equals("true")){
                    log.info("订单号:"+ orderFormDetailId + "请求退票, 退票请求已接收......");
                    this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUEST_SUCCESS);
                    return Pair.of(true , "退票请求已接收");
                }else{
                    log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                    //this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                    return Pair.of(false, "退票请求失败");
                }
            }catch (Throwable e){
                log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求异常......");
                return Pair.of(false, e.getMessage());
            }
        }else{
            //是抢购票
            final Optional<GrabTicketForm> orderFormOptional = this.grabTicketFormService.selectById(orderFormDetail.getGrabTicketFormId());
            Preconditions.checkArgument(orderFormOptional.isPresent() , "请求抢票订单不存在");
            final GrabTicketForm orderForm = orderFormOptional.get();
            // 申请时间距离发车时间不到2小时或已经超过发出时间时，不可退票
            final Date applyTime = new Date();
            //发车时间
            final Date startTime = DateFormatUtil.parse(orderForm.getStartDate()+" "+orderForm.getStartTime()+ ":00",
                    DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            if((applyTime.getTime() + 2*60*60*1000 + 1*60*1000) >= startTime.getTime()){
                return Pair.of(false , "距离发车时间不到2小时,不能退票");
            }
            //可退票, 创建退票流水订单
            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUESTING);
            final RefundTicketFlow flow = new RefundTicketFlow();
            flow.setOrderFormId(0);
            flow.setGrabTicketFormId(orderForm.getId());
            flow.setOrderFormDetailId(orderFormDetailId);
            flow.setApplyTime(applyTime);
            flow.setTicketNo(orderFormDetail.getTicketNo());
            flow.setStatus(EnumRefundTicketFlowStatus.INIT.getId());
            this.refundTicketFlowService.init(flow);
            //向航天华有发退票请求
            final HyReturnTicketRequest request = new HyReturnTicketRequest();
            request.setPartnerId(HySdkConstans.ORDER_PARTNER_ID);
            request.setMethod(EnumHTHYMethodCode.RETURN_TICKET.getCode());
            request.setReqTime(DateFormatUtil.format(new Date(),DateFormatUtil.yyyyMMddHHmmss));
            request.setCallBackUrl(HySdkConstans.REFUND_TICKET_NOTIFY_URL);
            request.setOrderId(orderForm.getOrderId());
            request.setOrderNumber(orderForm.getOrderNumber());
            request.setReqToken(SnGenerator.generate());
            request.setTransactionId(orderForm.getTransactionId());
            JSONObject obj = new JSONObject();
            //查询乘客信息
            final Optional<TbContactInfo> optional = this.contactInfoService.selectById(orderFormDetail.getPassengerId());
            Preconditions.checkArgument(optional.isPresent(), "乘客信息不存在");
            final TbContactInfo tbContactInfo = optional.get();
            obj.put("passengername", tbContactInfo.getName());
            obj.put("passportseno",tbContactInfo.getIdenty());
            obj.put("passporttypeseid",tbContactInfo.getIdentyType());
            obj.put("ticket_no",orderFormDetail.getTicketNo());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(obj);
            try{
                final HyReturnTicketResponse response = this.hySdkService.returnTicket(request, jsonArray);
                if(response.getSuccess().equals("true")){
                    log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求成功......");
                    this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUEST_SUCCESS);
                    return Pair.of(true , "退票请求已接收");
                }else{
                    log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                    //this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                    return Pair.of(false, "退票请求失败");
                }
            }catch (Throwable e){
                log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求异常......");
                //this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                return Pair.of(false, e.getMessage());
            }
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param jsonObject
     */
    @Override
    @Transactional
    public void handleRefundCallbackMsg(final JSONObject jsonObject) {
        //退款成功, 修改小订单状态 , 退款给客户
        final JSONArray jsonArray = jsonObject.getJSONArray("returntickets");
        if (jsonObject.getInt("returntype") == 1) {
            //线上退款
            if (jsonObject.getBoolean("returnstate")) {
    //////////////////////////
                //退票成功
                JSONObject obj = (JSONObject) jsonArray.get(0);
                final RefundTicketFlow flow = this.refundTicketFlowService.getByTicketNo(obj.getString("ticket_no"));
                flow.setReturnmoney(obj.getString("returnmoney"));
                flow.setSuccessTime(obj.getString("returntime"));
                flow.setRemark("线上退票成功");
                this.refundTicketFlowService.update(flow);
                this.orderFormDetailService.updateStatusById(flow.getOrderFormDetailId(), EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                // 给用户退款, 是否有出票套餐, 创建退款单 , 先退保险 , 再退票款 , 抢票套餐不退
                final OrderFormDetail orderFormDetail = this.orderFormDetailService.selectById(flow.getOrderFormDetailId()).get();
                final String paymentSn ;
                final int buyTicketPackageId ;
                final BigDecimal totalPrice;
                final Date createTime;
                if(orderFormDetail.getIsGrab() == 0){
                    final OrderForm orderForm = this.orderFormService.selectById(flow.getOrderFormId()).get();
                    paymentSn = orderForm.getPaymentSn();
                    buyTicketPackageId = orderForm.getBuyTicketPackageId();
                    totalPrice = orderForm.getTotalPrice();
                    createTime = orderForm.getCreateTime();
                    //如果退票成功, 判断大订单是否全部退票
                    final List<OrderFormDetail> list = this.orderFormDetailService.selectAllTicketsNoReFund(orderForm.getId(),EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                    final long count = this.orderFormDetailService.selectOrderFormNum(orderForm.getId());
                    if(list.size() == (int)count){
                        //全部退票
                        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_HAVE_BEEN_RETURN_TICKET.getId());
                        this.orderFormService.update(orderForm);
                    }
                }else{
                    final GrabTicketForm orderForm = this.grabTicketFormService.selectById(flow.getGrabTicketFormId()).get();
                    paymentSn = orderForm.getPaymentSn();
                    buyTicketPackageId = orderForm.getBuyTicketPackage();
                    totalPrice = orderForm.getTotalPrice();
                    createTime = orderForm.getCreateTime();
                    //如果退票成功, 判断大订单是否全部退票
                    final List<OrderFormDetail> list = this.orderFormDetailService.selectAllTicketsNoReFundGrab(orderForm.getId(), EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                    final long count = this.orderFormDetailService.selectGrabFormNumGrab(orderForm.getId());
                    if (list.size() == (int)count){
                        //全部退票
                        orderForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_HAVE_BEEN_RETURN_TICKET.getId());
                        this.grabTicketFormService.update(orderForm);
                    }
                }

                final PolicyOrder policyOrder = this.policyOrderService.getByOrderFormDetailId(orderFormDetail.getId());
                //先退保险
                final HyCancelPolicyOrderRequest request = new HyCancelPolicyOrderRequest();
                request.setUsername(HySdkConstans.USERNAME);
                request.setMethod(EnumHTHYMethodCode.CANCEL_POLICY_ORDER.getCode());
                request.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
                request.setPolicyNo(policyOrder.getPolicyNo());
                final JSONObject object = this.hySdkService.cancelPolicyOrder(request);

                ReturnMoneyOrder returnMoneyOrder = new ReturnMoneyOrder();
                returnMoneyOrder.setOrderFormDetailId(orderFormDetail.getId());
                returnMoneyOrder.setOrderFormSn(paymentSn);
                returnMoneyOrder.setRemark("线上退票退款");
                if (object.getInt("resultId") == 0) {
                    //退保成功 , 计算退款金额, 退款金额 = 该乘客票款实退金额 + 出票套餐
                    //更新保险单状态
                    this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_SUCCESS);
                    //有出票套餐
                    if (buyTicketPackageId != EnumBuyTicketPackageType.TICKET_PACKAGE_FIRST.getId()) {
                        returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()).
                                add(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice())));
                        returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice()));
                    } else {
                        //无出票套餐
                        returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                        returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(0));
                    }
                } else {
                    //退保失败
                    //更新保险单状态
                    this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_FAIL);
                    //有出票套餐
                    if (buyTicketPackageId != EnumBuyTicketPackageType.TICKET_PACKAGE_FIRST.getId()) {
                        returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                        returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice()));
                    } else {
                        //无出票套餐
                        returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                        returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(0));
                    }
                }
                    returnMoneyOrder.setOrgMoney(totalPrice);
                    returnMoneyOrder.setOrgDate(createTime);
                    returnMoneyOrder.setReturnTicketMoney(new BigDecimal(flow.getReturnmoney()));
                    returnMoneyOrder.setReturnGrabTicketPackage(new BigDecimal(0));
                    returnMoneyOrder.setStatus(EnumReturnMoneyOrderStatus.INIT.getId());
                    this.returnMoneyOrderService.init(returnMoneyOrder);
                    //请求退款接口 , 退款
                    final SingleRefundData data = new SingleRefundData();
                    data.setOrgSn(returnMoneyOrder.getOrderFormSn());
                    data.setOrdDate(DateFormatUtil.format(returnMoneyOrder.getOrgDate(), DateFormatUtil.yyyyMMdd));
                    data.setRefundAmount(returnMoneyOrder.getReturnTotalMoney().toString());
                    data.setOrgAmount(returnMoneyOrder.getOrgMoney().toString());
                    data.setRefundReason(returnMoneyOrder.getRemark());
                    final Map<String, Object> map = this.authenService.singlRefund(data);
                //判断退款是否成功 ,
                    if ((boolean) map.get("retCode") == true){
                        //成功 , 修改退款单状态,
                        this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_SUCCESS);
                    }else{
                        //失败 ,
                        this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_FAIL);
                    }

////////////////////////////////////////////////////
            } else {
                        //退票失败
                        JSONObject obj = (JSONObject) jsonArray.get(0);
                        final RefundTicketFlow flow = this.refundTicketFlowService.getByTicketNo(obj.getString("ticket_no"));
                        flow.setRemark("线上退票失败" + obj.getString("returnfailmsg"));
                        this.refundTicketFlowService.update(flow);
                        this.orderFormDetailService.updateStatusById(flow.getOrderFormDetailId(), EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                    }

        }
            else{
                //线下退款,或改签退款
                if (jsonObject.getBoolean("returnstate")) {
                    //退票成功
                    JSONObject obj = (JSONObject) jsonArray.get(0);
                    final RefundTicketFlow flow = this.refundTicketFlowService.getByTicketNo(obj.getString("ticket_no"));
                    flow.setReturnmoney(obj.getString("returnmoney"));
                    flow.setSuccessTime(obj.getString("returntime"));
                    flow.setRemark("线下退票成功");
                    this.refundTicketFlowService.update(flow);
                    this.orderFormDetailService.updateStatusById(flow.getOrderFormDetailId(), EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                    //给用户退款, 是否有出票套餐, 创建退款单 , 先退保险 , 再退票款 , 抢票套餐不退
                    final OrderFormDetail orderFormDetail = this.orderFormDetailService.selectById(flow.getOrderFormDetailId()).get();
                    final String paymentSn ;
                    final int buyTicketPackageId ;
                    final BigDecimal totalPrice;
                    final Date createTime;
                    if(orderFormDetail.getIsGrab() == 0){
                        final OrderForm orderForm = this.orderFormService.selectById(flow.getOrderFormId()).get();
                        paymentSn = orderForm.getPaymentSn();
                        buyTicketPackageId = orderForm.getBuyTicketPackageId();
                        totalPrice = orderForm.getTotalPrice();
                        createTime = orderForm.getCreateTime();
                        //如果退票成功, 判断大订单是否全部退票
                        final List<OrderFormDetail> list = this.orderFormDetailService.selectAllTicketsNoReFund(orderForm.getId(),EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                        final long count = this.orderFormDetailService.selectOrderFormNum(orderForm.getId());
                        if(list.size() == (int)count){
                            //全部退票
                            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_HAVE_BEEN_RETURN_TICKET.getId());
                            this.orderFormService.update(orderForm);
                        }
                    }else{
                        final GrabTicketForm orderForm = this.grabTicketFormService.selectById(flow.getGrabTicketFormId()).get();
                        paymentSn = orderForm.getPaymentSn();
                        buyTicketPackageId = orderForm.getBuyTicketPackage();
                        totalPrice = orderForm.getTotalPrice();
                        createTime = orderForm.getCreateTime();
                        //如果退票成功, 判断大订单是否全部退票
                        final List<OrderFormDetail> list = this.orderFormDetailService.selectAllTicketsNoReFundGrab(orderForm.getId(), EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                        final long count = this.orderFormDetailService.selectGrabFormNumGrab(orderForm.getId());
                        if (list.size() == (int)count){
                            //全部退票
                            orderForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_HAVE_BEEN_RETURN_TICKET.getId());
                            this.grabTicketFormService.update(orderForm);
                        }
                    }

                    final PolicyOrder policyOrder = this.policyOrderService.getByOrderFormDetailId(orderFormDetail.getId());
                    //先退保险
                    final HyCancelPolicyOrderRequest request = new HyCancelPolicyOrderRequest();
                    request.setUsername(HySdkConstans.USERNAME);
                    request.setMethod(EnumHTHYMethodCode.CANCEL_POLICY_ORDER.getCode());
                    request.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
                    request.setPolicyNo(policyOrder.getPolicyNo());
                    final JSONObject object = this.hySdkService.cancelPolicyOrder(request);

                    ReturnMoneyOrder returnMoneyOrder = new ReturnMoneyOrder();
                    returnMoneyOrder.setOrderFormDetailId(orderFormDetail.getId());
                    returnMoneyOrder.setOrderFormSn(paymentSn);
                    returnMoneyOrder.setRemark("线上退票退款");
                    if (object.getInt("resultId") == 0) {
                        //退保成功 , 计算退款金额, 退款金额 = 该乘客票款实退金额 + 出票套餐
                        //更新保险单状态
                        this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_SUCCESS);
                        //有出票套餐
                        if (buyTicketPackageId != EnumBuyTicketPackageType.TICKET_PACKAGE_FIRST.getId()) {
                            returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()).
                                    add(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice())));
                            returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice()));
                        } else {
                            //无出票套餐
                            returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                            returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(0));
                        }
                    } else {
                        //退保失败
                        //更新保险单状态
                        this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_FAIL);
                        //有出票套餐
                        if (buyTicketPackageId != EnumBuyTicketPackageType.TICKET_PACKAGE_FIRST.getId()) {
                            returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                            returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(0));
                        } else {
                            //无出票套餐
                            returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                            returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(0));
                        }
                    }
                    returnMoneyOrder.setOrgMoney(totalPrice);
                    returnMoneyOrder.setOrgDate(createTime);
                    returnMoneyOrder.setReturnTicketMoney(new BigDecimal(flow.getReturnmoney()));
                    returnMoneyOrder.setReturnGrabTicketPackage(new BigDecimal(0));
                    returnMoneyOrder.setStatus(EnumReturnMoneyOrderStatus.INIT.getId());
                    this.returnMoneyOrderService.init(returnMoneyOrder);
                    //请求退款接口 , 退款
                    final SingleRefundData data = new SingleRefundData();
                    data.setOrgSn(returnMoneyOrder.getOrderFormSn());
                    data.setOrdDate(DateFormatUtil.format(returnMoneyOrder.getOrgDate(), DateFormatUtil.yyyyMMdd));
                    data.setRefundAmount(returnMoneyOrder.getReturnTotalMoney().toString());
                    data.setOrgAmount(returnMoneyOrder.getOrgMoney().toString());
                    data.setRefundReason(returnMoneyOrder.getRemark());
                    final Map<String, Object> map = this.authenService.singlRefund(data);
                    //判断退款是否成功 ,
                    if ((boolean) map.get("retCode") == true){
                        //成功 , 修改退款单状态,
                        this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_SUCCESS);
                    }else{
                        //失败.
                        this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_FAIL);
                    }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     * @param paymentSn
     * @param isPaySuccess
     */
    @Override
    @Transactional
    public void handleCustomerPayMsg(final long orderFormId, final String paymentSn, final boolean isPaySuccess) {
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByIdWithLock(orderFormId);
        final OrderForm orderForm = orderFormOptional.get();
        Preconditions.checkState(orderForm.isOccupySuccess() || orderForm.isCustomerPayFail(), "处理客户付款，订单[%s]的状态不是占座成功或者付款失败状态！！！", orderFormId);
        final Optional<ChargeMoneyOrder> chargeMoneyOrderOptional = this.chargeMoneyOrderService.selectByOrderFormId(orderFormId);
        Preconditions.checkState(chargeMoneyOrderOptional.isPresent(), "订单[%s]对应的收款记录不存在", orderFormId);
        final ChargeMoneyOrder chargeMoneyOrder = this.chargeMoneyOrderService.selectByIdWithLock(chargeMoneyOrderOptional.get().getId()).get();
        Preconditions.checkState(!chargeMoneyOrder.isPaySuccess(), "订单[%s]对应的收款记录已经付款成功！！！！！！！", orderFormId);
        if (isPaySuccess) {
            log.info("订单[" + orderFormId + "]支付成功");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_SUCCESS.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_SUCCESS.getValue());
            orderForm.setPaymentSn(paymentSn);
            this.orderFormService.update(orderForm);
            chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.PAYMENT_TICKET_SUCCESS.getId());
            log.info("订单[" + orderFormId + "]支付成功--调用确认订单接口！！");
            this.confirmOrder(orderForm);
        } else {
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_FAIL.getValue());
            orderForm.setPaymentSn(paymentSn);
            this.orderFormService.update(orderForm);
            chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.PAYMENT_TICKET_FAIL.getId());
        }
        this.chargeMoneyOrderService.update(chargeMoneyOrder);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public Pair<Boolean, String> grabTicket(final RequestGrabTicket req) {

        final JSONObject jsonObject = this.queryTicketPriceService.queryTicket(HySdkConstans.QUERY_PARTNER_ID, "train_query", req.getFromStationCode(), req.getToStationCode(), req.getGrabStartTime(), "ADULT");
        //获取列车信息
        final JSONArray jsonArray = jsonObject.getJSONArray("data");
        //获取车次信息
        Map<String, JSONObject> trainCodes = this.getMaps(req.getTrainCodes(), jsonArray);
        //获取座位类型
        final String[] seatType = req.getSeatTypes().split(",");
        //获取最高价格
        final double grabPrice = this.getPriceMap(trainCodes, seatType);
        //创建抢票单

        final GrabTicketForm grabTicketForm = new GrabTicketForm();
        grabTicketForm.setUid(req.getUid());
        grabTicketForm.setPhone(req.getPhone());
        grabTicketForm.setFirstStartTime(req.getFirstStartTime());
        grabTicketForm.setTicketNum(req.getGrabPassengers().size());
        final BigDecimal multiply = new BigDecimal(grabPrice).multiply(new BigDecimal(req.getGrabPassengers().size()));
        grabTicketForm.setGrabTicketTotalPrice(multiply);
        //购买套餐的价格
        final BigDecimal buyPackage = new BigDecimal(EnumBuyTicketPackageType.of(req.getBuyTicketPackageId()).getPrice()).multiply(new BigDecimal(grabTicketForm.getTicketNum()));
        final BigDecimal grabPackage = new BigDecimal(EnumGrabTicketPackageType.of(req.getGrabTicketPackageId()).getPrice()).multiply(new BigDecimal(grabTicketForm.getTicketNum()));
        final BigDecimal grabTotalPackagePrice = buyPackage.add(grabPackage);

        grabTicketForm.setOrderId(SnGenerator.generate());
        grabTicketForm.setGrabTotalPrice(grabTotalPackagePrice.add(multiply));
        grabTicketForm.setGrabStartTime(req.getGrabStartTime());
        grabTicketForm.setGrabTimeType(req.getGrabTimeType());
        grabTicketForm.setFromStationName(req.getFromStationName());
        grabTicketForm.setFromStationCode(req.getFromStationCode());
        grabTicketForm.setToStationName(req.getToStationName());
        grabTicketForm.setToStationCode(req.getToStationCode());
        grabTicketForm.setTrainCodes(req.getTrainCodes());
        grabTicketForm.setSeatTypes(req.getSeatTypes());
        grabTicketForm.setGrabTicketPackage(req.getGrabTicketPackageId());
        grabTicketForm.setBuyTicketPackage(req.getBuyTicketPackageId());
        grabTicketForm.setPassengerInfo(JSONArray.fromObject(req.getGrabPassengers()).toString());
        grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_PAY_WAIT.getId());
        //初始化抢票单, 创建收款单收款
        this.grabTicketFormService.init(grabTicketForm);

        //创建商户收款记录
        final ChargeMoneyOrder chargeMoneyOrder = new ChargeMoneyOrder();
        chargeMoneyOrder.setOrderFormId(0);
        chargeMoneyOrder.setGrabTicketFormId(grabTicketForm.getId());
        chargeMoneyOrder.setTotalAmount(grabTicketForm.getGrabTotalPrice());
        chargeMoneyOrder.setBuyTicketPackage(buyPackage);
        chargeMoneyOrder.setGrabTicketPackage(grabPackage);
        chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.INIT.getId());
        chargeMoneyOrderService.init(chargeMoneyOrder);
        //放入消息队列 , 15分钟支付时间
        JSONObject mqJo = new JSONObject();
        mqJo.put("grabTicketFormId",grabTicketForm.getId());
        MqProducer.sendMessage(mqJo, MqConfig.TICKET_CANCEL_EXPIRED_GRAB_ORDER, 1000*15*60);

        return Pair.of(true,String.valueOf(grabTicketForm.getId()));
    }

    /**
     * {@inheritDoc}
     *
     * @param grabTicketFormId
     * @param paymentSn
     * @param isPaySuccess
     */
    @Override
    public void handleGrabCustomerPayMsg(long grabTicketFormId, String paymentSn, boolean isPaySuccess) throws Exception {
        final Optional<GrabTicketForm> orderFormOptional = this.grabTicketFormService.selectByIdWithLock(grabTicketFormId);
        final GrabTicketForm orderForm = orderFormOptional.get();
        Preconditions.checkState(orderForm.isPaySuccess(), "抢票单[%s]的状态不是支付成功状态！！！" , grabTicketFormId);
        final Optional<ChargeMoneyOrder> chargeMoneyOrderOptional = this.chargeMoneyOrderService.selectByGrabTicketFormId(grabTicketFormId);
        Preconditions.checkState(chargeMoneyOrderOptional.isPresent(), "订单[%s]对应的收款记录不存在", grabTicketFormId);
        final ChargeMoneyOrder chargeMoneyOrder = this.chargeMoneyOrderService.selectByIdWithLock(chargeMoneyOrderOptional.get().getId()).get();
        Preconditions.checkState(chargeMoneyOrder.isPaySuccess(), "订单[%s]对应的收款记录已经付款成功！！！！！！！");
        if (isPaySuccess) {
            log.info("订单[" + grabTicketFormId + "]支付成功");
            orderForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_PAY_SUCCESS.getId());
            orderForm.setRemark(EnumGrabTicketStatus.GRAB_FORM_PAY_SUCCESS.getValue());
            orderForm.setPaymentSn(paymentSn);
            this.grabTicketFormService.update(orderForm);
            chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.PAYMENT_TICKET_SUCCESS.getId());
            log.info("订单[" + grabTicketFormId + "]支付成功--调用确认订单接口！！");
            //抢票支付成功, 请求抢票
            this.requestGrabImpl(grabTicketFormId);
        } else {
            //记录流水号
            orderForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_PAY_FAIL.getId());
            orderForm.setRemark(EnumGrabTicketStatus.GRAB_FORM_PAY_FAIL.getValue());
            orderForm.setPaymentSn(paymentSn);
            this.grabTicketFormService.update(orderForm);
            chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.PAYMENT_TICKET_FAIL.getId());
        }
        this.chargeMoneyOrderService.update(chargeMoneyOrder);
    }

    /**
     * {@inheritDoc}
     *
     * @param jsonParams
     */
    @Transactional
    @Override
    public void handleGrabCallBackMsg(JSONObject jsonParams) {
        final Optional<GrabTicketForm> grabTicketFormOptional = this.grabTicketFormService.selectByOrderIdWithLock(jsonParams.getString("orderid"));
        Preconditions.checkArgument(grabTicketFormOptional.isPresent(), "抢票单还未创建");
        final GrabTicketForm grabTicketForm = grabTicketFormOptional.get();
        if(jsonParams.getBoolean("success") == true && jsonParams.getInt("code") == 100){
            //抢票成功,创建小订单, 退差价, 买保险
            grabTicketForm.setRunTime(jsonParams.getString("runtime"));
            grabTicketForm.setOrderNumber(jsonParams.getString("ordernumber"));
            grabTicketForm.setCheci(jsonParams.getString("checi"));
            final BigDecimal totalPackage = grabTicketForm.getGrabTotalPrice().subtract(grabTicketForm.getGrabTicketTotalPrice());
            grabTicketForm.setTicketTotalPrice(new BigDecimal(jsonParams.getString("orderamount")));
            grabTicketForm.setTotalPrice(totalPackage.add(grabTicketForm.getTicketTotalPrice()));
            grabTicketForm.setStartDate(jsonParams.getString("train_date"));
            grabTicketForm.setEndDate(getDateStr(jsonParams.getString("arrive_time")));
            grabTicketForm.setStartTime(getTimeStr(jsonParams.getString("start_time")));
            grabTicketForm.setEndTime(getTimeStr(jsonParams.getString("arrive_time")));
            grabTicketForm.setTransactionId(jsonParams.getString("transactionid"));
            grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_SUCCESS.getId());
            this.grabTicketFormService.update(grabTicketForm);

            final JSONArray passengers = jsonParams.getJSONArray("passengers");
            for (Object object : passengers){
                final JSONObject obj = JSONObject.fromObject(object);
                final OrderFormDetail orderFormDetail = new OrderFormDetail();
                orderFormDetail.setOrderFormId(0);
                orderFormDetail.setGrabTicketFormId(grabTicketForm.getId());
                orderFormDetail.setIsGrab(1);
                orderFormDetail.setMobile(grabTicketForm.getPhone());
                orderFormDetail.setPassengerId(obj.getLong("passengerid"));
                orderFormDetail.setPassengerName(obj.getString("passengersename"));
                orderFormDetail.setPassportSeNo(obj.getString("passportseno"));
                orderFormDetail.setPassportTypeSeId(obj.getString("passporttypeseid"));
                orderFormDetail.setPassportTypeSeName(obj.getString("passporttypeseidname"));
                orderFormDetail.setTicketNo(obj.getString("ticket_no"));
                orderFormDetail.setPrice(new BigDecimal(obj.getString("price")));
                orderFormDetail.setCheci(grabTicketForm.getCheci());
                orderFormDetail.setPiaoType(obj.getString("piaotype"));
                orderFormDetail.setCxin(obj.getString("cxin"));
                this.orderFormDetailService.add(orderFormDetail);
            }
            //退差价
            final BigDecimal subtract = grabTicketForm.getTicketTotalPrice().subtract(grabTicketForm.getGrabTicketTotalPrice());
            if (subtract.compareTo(new BigDecimal(0)) != 0){
                final SendGrabTicketSuccessParam param = new SendGrabTicketSuccessParam();
                param.setUid(grabTicketForm.getUid());
                param.setMobile(grabTicketForm.getPhone());
                param.setStartStation(grabTicketForm.getFromStationName());
                param.setEndStation(grabTicketForm.getToStationName());
                param.setTicketNo(grabTicketForm.getCheci());
                param.setStartDate(grabTicketForm.getStartDate());
                param.setStartTime(grabTicketForm.getStartTime());
                param.setTicketNo(grabTicketForm.getOrderNumber());
                param.setResidueAmount(subtract.toString());
                this.ticketSendMessageService.sendGrabTicketSuccessHaveResidueMessage(param);
                //有差价,才退款
                final RefundOrderFlow refundOrderFlow = new RefundOrderFlow();
                refundOrderFlow.setOrderFormId(0);
                refundOrderFlow.setGrabTicketFormId(grabTicketForm.getId());
                refundOrderFlow.setPaymentSn(grabTicketForm.getPaymentSn());
                refundOrderFlow.setOrderDate(DateFormatUtil.format(grabTicketForm.getCreateTime(), DateFormatUtil.yyyyMMdd));
                refundOrderFlow.setRefundAmount(subtract);
                refundOrderFlow.setOriginalAmount(grabTicketForm.getGrabTotalPrice());
                refundOrderFlow.setRefundReason("抢票成功差价退款");
                this.refundOrderFlowService.insert(refundOrderFlow);

                //请求退款接口 , 退款
                final SingleRefundData data = new SingleRefundData();
                data.setOrgSn(refundOrderFlow.getPaymentSn());
                data.setOrdDate(refundOrderFlow.getOrderDate());
                data.setRefundAmount(refundOrderFlow.getRefundAmount().toString());
                data.setOrgAmount(refundOrderFlow.getOriginalAmount().toString());
                data.setRefundReason(refundOrderFlow.getRefundReason());
                final Map<String, Object> map = this.authenService.singlRefund(data);
                //判断退款是否成功 ,
                if ((boolean) map.get("retCode") == true){
                    //成功 , 修改退款单状态,
                    refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId());
                    this.refundOrderFlowService.update(refundOrderFlow);
                }else{
                    //失败.
                    refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
                    this.refundOrderFlowService.update(refundOrderFlow);
                }
            }else{
                //发送短信
                final SendGrabTicketSuccessParam param = new SendGrabTicketSuccessParam();
                param.setUid(grabTicketForm.getUid());
                param.setMobile(grabTicketForm.getPhone());
                param.setStartStation(grabTicketForm.getFromStationName());
                param.setEndStation(grabTicketForm.getToStationName());
                param.setTicketNo(grabTicketForm.getCheci());
                param.setStartDate(grabTicketForm.getStartDate());
                param.setStartTime(grabTicketForm.getStartTime());
                param.setTicketNo(grabTicketForm.getOrderNumber());
                this.ticketSendMessageService.sendGrabTicketSuccessHaveNotResidueMessage(param);
            }
            //购买保险
            this.policyOrderService.batchBuyGrabPolicy(grabTicketForm.getId());

        }else{
            //抢票时间到期,抢票失败 , 全额退款
            //更新抢票单状态
            this.grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_FAIL, grabTicketForm.getId());
            //全额退款
            this.returnToGrabFail(grabTicketForm.getId());
           /* final ReturnMoneyOrder returnMoneyOrder = new ReturnMoneyOrder();
            returnMoneyOrder.setOrderFormDetailId(0);
            returnMoneyOrder.setOrderFormSn(grabTicketForm.getPaymentSn());
            returnMoneyOrder.setRemark("抢票失败退款");
            returnMoneyOrder.setOrgDate(grabTicketForm.getCreateTime());
            returnMoneyOrder.setReturnTotalMoney(grabTicketForm.getTotalPrice());
            returnMoneyOrder.setOrgMoney(grabTicketForm.getTotalPrice());
            returnMoneyOrder.setReturnTicketMoney(grabTicketForm.getGrabTicketTotalPrice());
            final BigDecimal buyPackage = new BigDecimal(EnumBuyTicketPackageType.of(grabTicketForm.getBuyTicketPackage()).getPrice()).multiply(new BigDecimal(grabTicketForm.getTicketNum()));
            final BigDecimal grabPackage = new BigDecimal(EnumGrabTicketPackageType.of(grabTicketForm.getGrabTicketPackage()).getPrice()).multiply(new BigDecimal(grabTicketForm.getTicketNum()));
            returnMoneyOrder.setReturnBuyTicketPackage(buyPackage);
            returnMoneyOrder.setReturnGrabTicketPackage(grabPackage);
            returnMoneyOrder.setStatus(EnumReturnMoneyOrderStatus.INIT.getId());
            this.returnMoneyOrderService.init(returnMoneyOrder);
            //抢票失败, 发送退款短信
            final SendGrabTicketFailParam param = new SendGrabTicketFailParam();
            param.setUid(grabTicketForm.getUid());
            param.setMobile(grabTicketForm.getPhone());
            param.setStartStation(grabTicketForm.getFromStationName());
            param.setEndStation(grabTicketForm.getToStationName());
            param.setStartDate(grabTicketForm.getGrabStartTime());
            this.ticketSendMessageService.sendGrabTicketFailMessage(param);
            //请求退款接口 , 退款
            final SingleRefundData data = new SingleRefundData();
            data.setOrgSn(returnMoneyOrder.getOrderFormSn());
            data.setOrdDate(DateFormatUtil.format(returnMoneyOrder.getOrgDate(), DateFormatUtil.yyyyMMdd));
            data.setRefundAmount(returnMoneyOrder.getReturnTotalMoney().toString());
            data.setOrgAmount(returnMoneyOrder.getOrgMoney().toString());
            data.setRefundReason(returnMoneyOrder.getRemark());
            final Map<String, Object> map = this.authenService.singlRefund(data);
            //判断退款是否成功 ,
            if ((boolean) map.get("retCode") == true){
                //成功 , 修改退款单状态,
                this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_SUCCESS);
            }else{
                //失败.
                this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_FAIL);
            }*/
        }
    }

    /**
     * 抢票失败给用户全额退票
     */
    public void returnToGrabFail(final long grabTicketFormId){

        final Optional<GrabTicketForm> grabTicketFormOptional = this.grabTicketFormService.selectByIdWithLock(grabTicketFormId);
        Preconditions.checkArgument(grabTicketFormOptional.isPresent(), "订单不存在");
        final GrabTicketForm grabTicketForm = grabTicketFormOptional.get();

        final RefundOrderFlow refundOrderFlow = new RefundOrderFlow();
        refundOrderFlow.setOrderFormId(0);
        refundOrderFlow.setGrabTicketFormId(grabTicketForm.getId());
        refundOrderFlow.setPaymentSn(grabTicketForm.getPaymentSn());
        refundOrderFlow.setOrderDate(DateFormatUtil.format(grabTicketForm.getCreateTime(), DateFormatUtil.yyyyMMdd));
        refundOrderFlow.setRefundAmount(grabTicketForm.getGrabTotalPrice());
        refundOrderFlow.setOriginalAmount(grabTicketForm.getGrabTotalPrice());
        refundOrderFlow.setRefundReason("抢票失败,全额退款");
        this.refundOrderFlowService.insert(refundOrderFlow);
        //抢票失败, 发送退款短信
        final SendGrabTicketFailParam param = new SendGrabTicketFailParam();
        param.setUid(grabTicketForm.getUid());
        param.setMobile(grabTicketForm.getPhone());
        param.setStartStation(grabTicketForm.getFromStationName());
        param.setEndStation(grabTicketForm.getToStationName());
        param.setStartDate(grabTicketForm.getGrabStartTime());
        this.ticketSendMessageService.sendGrabTicketFailMessage(param);
        //请求退款接口 , 退款
        final SingleRefundData data = new SingleRefundData();
        data.setOrgSn(refundOrderFlow.getPaymentSn());
        data.setOrdDate(refundOrderFlow.getOrderDate());
        data.setRefundAmount(refundOrderFlow.getRefundAmount().toString());
        data.setOrgAmount(refundOrderFlow.getOriginalAmount().toString());
        data.setRefundReason(refundOrderFlow.getRefundReason());
        final Map<String, Object> map = this.authenService.singlRefund(data);
        //判断退款是否请求成功 ,
        if ((boolean) map.get("retCode") == true){
            //成功 , 修改退款单状态,
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId());
            this.refundOrderFlowService.update(refundOrderFlow);
        }else{
            //失败.
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
            this.refundOrderFlowService.update(refundOrderFlow);
        }
    }
    /**
     * {@inheritDoc}
     *
     * @param grabTicketFormId
     * @return
     */
    @Override
    @Transactional
    public Pair<Boolean, String> cancelGrabTicket(long grabTicketFormId) throws Exception {
        //先判断该订单处在抢票进行中的状态
        final Optional<GrabTicketForm> grabTicketFormOptional = this.grabTicketFormService.selectByIdWithLock(grabTicketFormId);
        Preconditions.checkArgument(grabTicketFormOptional.isPresent(), "抢票单不存在,无法取消");
        final GrabTicketForm grabTicketForm = grabTicketFormOptional.get();
        if (EnumGrabTicketStatus.GRAB_FORM_REQUEST_SUCCESS.getId() != grabTicketForm.getStatus()){
            return Pair.of(false, "此抢票单不能取消");
        }

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("qorderid", grabTicketForm.getOrderId());
        jsonObject.put("partnerid", HySdkConstans.GRAB_PARTNER_ID);
        jsonObject.put("reqtime", DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
        jsonObject.put("sign", MD5Util.MD5(HySdkConstans.GRAB_PARTNER_ID +
                DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss) + MD5Util.MD5(HySdkConstans.GRAB_SIGN_KEY)));
        final JSONObject jsonResponse = this.hySdkService.cancelGrabTickets(jsonObject);

        if(jsonResponse.getBoolean("isSuccess")){
            //抢票单取消成功, 退款
            final ReturnMoneyOrder returnMoneyOrder = new ReturnMoneyOrder();
            returnMoneyOrder.setOrderFormDetailId(0);
            returnMoneyOrder.setOrderFormSn(grabTicketForm.getPaymentSn());
            returnMoneyOrder.setRemark("取消抢票退款");
            returnMoneyOrder.setOrgDate(grabTicketForm.getCreateTime());
            returnMoneyOrder.setReturnTotalMoney(grabTicketForm.getTotalPrice());
            returnMoneyOrder.setOrgMoney(grabTicketForm.getTotalPrice());
            returnMoneyOrder.setReturnTicketMoney(grabTicketForm.getGrabTicketTotalPrice());
            final BigDecimal buyPackage = new BigDecimal(EnumBuyTicketPackageType.of(grabTicketForm.getBuyTicketPackage()).getPrice()).multiply(new BigDecimal(grabTicketForm.getTicketNum()));
            final BigDecimal grabPackage = new BigDecimal(EnumGrabTicketPackageType.of(grabTicketForm.getGrabTicketPackage()).getPrice()).multiply(new BigDecimal(grabTicketForm.getTicketNum()));
            returnMoneyOrder.setReturnBuyTicketPackage(buyPackage);
            returnMoneyOrder.setReturnGrabTicketPackage(grabPackage);
            returnMoneyOrder.setStatus(EnumReturnMoneyOrderStatus.INIT.getId());
            this.returnMoneyOrderService.init(returnMoneyOrder);

            //请求退款接口 , 退款
            final SingleRefundData data = new SingleRefundData();
            data.setOrgSn(returnMoneyOrder.getOrderFormSn());
            data.setOrdDate(DateFormatUtil.format(returnMoneyOrder.getOrgDate(), DateFormatUtil.yyyyMMdd));
            data.setRefundAmount(returnMoneyOrder.getReturnTotalMoney().toString());
            data.setOrgAmount(returnMoneyOrder.getOrgMoney().toString());
            data.setRefundReason(returnMoneyOrder.getRemark());
            final Map<String, Object> map = this.authenService.singlRefund(data);
            //判断退款是否成功 ,
            if ((boolean) map.get("retCode") == true){
                //成功 , 修改退款单状态,
                this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_SUCCESS);
            }else{
                //失败.
                this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_FAIL);
            }
            return Pair.of(true, "取消抢票单成功");
        }else{
            //失败 , 记录
            grabTicketForm.setRemark(jsonResponse.getString("msg"));
            this.grabTicketFormService.update(grabTicketForm);
            return Pair.of(false, jsonResponse.getString("msg"));
        }
    }

    private String getDateStr(String timeString){

        //传入的时间数据格式为 2016-10-09 10:04:00
        final String substring = timeString.substring(0, 10);
        return substring;
    }

    private String getTimeStr(String timeString){

        //传入的时间数据格式为 2016-10-09 10:04:00
        final String substring = timeString.substring(11, 16);
        return substring;
    }

    /**
     * 支付成功后请求抢票
     * @param grabTicketFormId
     */
    public void requestGrabImpl(long grabTicketFormId) throws Exception {
        final Optional<GrabTicketForm> grabTicketFormOptional = this.grabTicketFormService.selectByIdWithLock(grabTicketFormId);
        final GrabTicketForm grabTicketForm = grabTicketFormOptional.get();
        //判断是否购买套餐
        final boolean flag = (EnumBuyTicketPackageType.of(grabTicketForm.getBuyTicketPackage()).getId() == EnumBuyTicketPackageType.TICKET_PACKAGE_FIRST.getId())
           &&(EnumGrabTicketPackageType.of(grabTicketForm.getGrabTicketPackage()).getId() == EnumGrabTicketPackageType.TICKET_PACKAGE_FIRST.getId());
        final Date parse = DateFormatUtil.parse(grabTicketForm.getFirstStartTime(), DateFormatUtil.yyyy_MM_dd_HH_mm);
        final Date endTime = new Date(parse.getTime() - EnumGrabTimeType.of(grabTicketForm.getGrabTimeType()).getHour()*60*60*1000);
        if (flag){
            //未购买套餐
            grabTicketForm.setStatus(EnumGrabTicketStatus.WAIT_FOR_REFUND.getId());
            this.grabTicketFormService.update(grabTicketForm);
            //放入消息队列 , 15分钟支付时间
            JSONObject mqJo = new JSONObject();
            mqJo.put("grabTicketFormId",grabTicketForm.getId());
            MqProducer.sendMessage(mqJo, MqConfig.NO_PACKAGE_WAIT_REFUND, endTime.getTime() - (new Date()).getTime());
            return;
        }
        //请求抢票
        final JSONObject jsonObject = new JSONObject();
        final JSONArray passengerJsonArray = new JSONArray();
        final JSONArray jsonArray = JSONArray.fromObject(grabTicketForm.getPassengerInfo());
        for (Object obj : jsonArray){
            final JSONObject object = JSONObject.fromObject(obj);
            final JSONObject passengerJsonObject = new JSONObject();
            final Optional<TbContactInfo> contactInfoOptional1 = contactInfoService.selectById(object.getLong("id"));
            Preconditions.checkState(contactInfoOptional1.isPresent(), "乘客不存在");
            final TbContactInfo contactInfo = contactInfoOptional1.get();
            passengerJsonObject.put("passengerid", object.getString("id"));
            passengerJsonObject.put("passengersename", contactInfo.getName());
            passengerJsonObject.put("passportseno", contactInfo.getIdenty());
            passengerJsonObject.put("passporttypeseid", contactInfo.getIdentyType());
            passengerJsonObject.put("passporttypeidname", EnumCertificatesType.of(contactInfo.getIdentyType()).getValue());
            passengerJsonObject.put("piaotype", String.valueOf(contactInfo.getPersonType()));
            passengerJsonObject.put("piaotypename", EnumTrainTicketType.of(String.valueOf(contactInfo.getPersonType())).getValue());
            passengerJsonArray.add(passengerJsonObject);
        }
        jsonObject.put("partnerid",HySdkConstans.ORDER_PARTNER_ID);
        jsonObject.put("method", EnumHTHYMethodCode.QIANG_PIAO_ORDER);
        jsonObject.put("reqtime", DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
        jsonObject.put("sign", MD5Util.MD5(HySdkConstans.ORDER_PARTNER_ID + EnumHTHYMethodCode.QIANG_PIAO_ORDER +
                DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss) + MD5Util.MD5(HySdkConstans.ORDER_SIGN_KEY)));
        jsonObject.put("qorderid", grabTicketForm.getOrderId());
        jsonObject.put("callback_url", "");
        jsonObject.put("train_type", "“G,D,Z,T,K,C,Q”");
        jsonObject.put("from_station_code", grabTicketForm.getFromStationCode());
        jsonObject.put("from_station_name",grabTicketForm.getFromStationName());
        jsonObject.put("to_station_code", grabTicketForm.getToStationCode());
        jsonObject.put("to_station_name", grabTicketForm.getToStationName());
        jsonObject.put("start_date", grabTicketForm.getGrabStartTime());
        // 判断出发日期是不是今天
        if(grabTicketForm.getGrabStartTime().equals(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd))){
            //是今天
            final String format = DateFormatUtil.format(new Date(new Date().getTime() + 3 * 60 * 60 * 1000), DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            jsonObject.put("start_begin_time", getTimeStr(format));
        }else{
            jsonObject.put("start_begin_time", "00:00");
        }
        jsonObject.put("start_end_time", "24:00");
        jsonObject.put("train_codes", grabTicketForm.getTrainCodes());
        jsonObject.put("seat_type", this.getSeatTypeStr(grabTicketForm.getSeatTypes()));
        jsonObject.put("qorder_type",100);
        jsonObject.put("qorder_start_time",DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd_HH_mm));
        jsonObject.put("qorder_end_time",DateFormatUtil.format(endTime , DateFormatUtil.yyyy_MM_dd_HH_mm));
        jsonObject.put("max_price",0);
        jsonObject.put("passengers",passengerJsonArray);
        jsonObject.put("agree_offline", false);

        final JSONObject jsonResponse = this.hySdkService.grabTickets(jsonObject);
        if(jsonResponse.getBoolean("success") == true && jsonResponse.getInt("code") == 100){
            //抢票下单成功
            grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REQUEST_SUCCESS.getId());
            grabTicketForm.setRemark(jsonResponse.getString("msg"));
            this.grabTicketFormService.update(grabTicketForm);
        }else{
            //抢票下单失败,更改抢票单状态, 记录失败原因
            grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REQUEST_FAIL.getId());
            grabTicketForm.setRemark(jsonResponse.getString("msg"));
            this.grabTicketFormService.update(grabTicketForm);
        }
    }

    private String getSeatTypeStr(String seatTypes) {

        final String[] split = seatTypes.split(",");
        final StringBuilder builder = new StringBuilder();
        for (String string : split){
            builder.append(EnumTrainTicketSeatType.of(string).getValue());
            builder.append(",");
        }
        return builder.substring(0, builder.lastIndexOf(",")).toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     * @param isRefundSuccess
     */
    @Override
    @Transactional
    public void handleOrderFormRefundResult(final long orderFormId, final boolean isRefundSuccess, final String msg) {
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByIdWithLock(orderFormId);
        final OrderForm orderForm = orderFormOptional.get();
        Preconditions.checkState(orderForm.isRefundIngOrRefundFail(), "订单[" + orderFormId + "]不是付款失败或者付款中状态");
        final Optional<RefundOrderFlow> refundOrderFlowOptional = this.refundOrderFlowService.selectByOrderFormId(orderFormId);
        final RefundOrderFlow refundOrderFlow = refundOrderFlowOptional.get();
        if (isRefundSuccess) {
            refundOrderFlow.setMsg("退款成功");
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId());
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_REFUND_SUCCESS.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_REFUND_SUCCESS.getValue());
        } else {
            refundOrderFlow.setMsg(msg);
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_REFUND_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_REFUND_FAIL.getValue());
        }
        this.orderFormService.update(orderForm);
        this.refundOrderFlowService.update(refundOrderFlow);
    }


    private double getPriceMap(Map<String, JSONObject> trainCodes, String[] seatType) {

        Map<String, Double> map = new HashMap<>();
        for (String string : seatType){
            map.put(EnumTrainTicketSeatType.getName(string), 0.00);
        }
        for (Map.Entry<String, JSONObject> entry: trainCodes.entrySet()){
            for (Map.Entry<String, Double> mapEntry : map.entrySet()){
                final Double seatPrice = entry.getValue().getDouble(mapEntry.getKey());
                if(mapEntry.getValue().compareTo(seatPrice) == -1){
                    map.put(mapEntry.getKey(), seatPrice);
                }
            }
        }
        final List<Double> list = new ArrayList<>();
       for (Map.Entry<String , Double> entry : map.entrySet()){
           list.add(entry.getValue());
       }
       Collections.sort(list);
        return list.get(list.size() - 1);
    }

    private Map<String, JSONObject> getMaps(String str, JSONArray jsonArray) {
        final String[] strings = str.split(",");
        //获取车次信息
        Map<String, JSONObject>  map = new HashMap<>();
        for (Object obj : jsonArray){
            JSONObject jsonObject = JSONObject.fromObject(obj);
            for (String string : strings){
                if(string.equals(jsonObject.getString("train_code"))){
                    map.put(string, jsonObject);
                }
            }
        }
        return map;
    }


    /**
     * 处理保险策略
     *
     * 占座成功后执行，处理结果是占座失败
     *
     * @param orderForm
     */
    private void handleInsurancePolicy(final OrderForm orderForm) {
        this.cancelOrder(orderForm.getId());
        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getId());
        orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getValue());
        this.orderFormService.update(orderForm);
        this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
    }


    /**
     * 初始化 退款单
     *
     * @return
     */
    private RefundOrderFlow initRefundOrderFlow(final OrderForm orderForm) {
        log.info("订单[" + orderForm.getId() + "]初始化退款单");
        final Optional<RefundOrderFlow> refundOrderFlowOptional = this.refundOrderFlowService.selectByOrderFormId(orderForm.getId());
        Preconditions.checkState(!refundOrderFlowOptional.isPresent(), "订单[" + orderForm.getId() + "]已经生成退款单[" + refundOrderFlowOptional.get().getId() + "]");
        final RefundOrderFlow refundOrderFlow = new RefundOrderFlow();
        refundOrderFlow.setOrderFormId(orderForm.getId());
        refundOrderFlow.setPaymentSn(orderForm.getPaymentSn());
        refundOrderFlow.setOrderDate(DateFormatUtil.format(orderForm.getCreateTime(), DateFormatUtil.yyyyMMdd));
        refundOrderFlow.setRefundAmount(orderForm.getTotalPrice());
        refundOrderFlow.setOriginalAmount(orderForm.getTotalPrice());
        refundOrderFlow.setRefundReason("正常退款");
        refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.INIT.getId());
        this.refundOrderFlowService.insert(refundOrderFlow);
        return refundOrderFlow;
    }


    /**
     * 客户订单退款
     */
    private void orderRefund(final RefundOrderFlow refundOrderFlow, final OrderForm orderForm) {
        log.info("订单[" + orderForm.getId() + "]请求退款");
        Preconditions.checkState(!refundOrderFlow.isRefundSuccess(), "订单[" + refundOrderFlow.getOrderFormId()  +
                "]对应的退款单[" + refundOrderFlow.getId()+ "]已经退款");
        final SingleRefundData singleRefundData = new SingleRefundData();
        singleRefundData.setOrgSn(refundOrderFlow.getPaymentSn());
        singleRefundData.setOrdDate(refundOrderFlow.getOrderDate());
        singleRefundData.setRefundAmount(refundOrderFlow.getRefundAmount().toString());
        singleRefundData.setOrgAmount(refundOrderFlow.getOriginalAmount().toString());
        singleRefundData.setRefundReason(refundOrderFlow.getRefundReason());
        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_REFUND_ING.getId());
        orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_REFUND_ING.getValue());
        this.orderFormService.update(orderForm);
        final Map<String, Object> resultMap = this.authenService.singlRefund(singleRefundData);
        if ((boolean) resultMap.get("retCode")) {
            log.info("订单[" + orderForm.getId() + "]--退款单[" + refundOrderFlow.getId() + "]" + "退款申请成功！！添加消息回调");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_REFUND_ING.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_REFUND_ING.getValue());
            this.orderFormService.update(orderForm);
            //消息
            JSONObject mqJo = new JSONObject();
            mqJo.put("orderFormId", orderForm.getId());
            mqJo.put("reqSn", orderForm.getPaymentSn());
            mqJo.put("refundAmount", singleRefundData.getRefundAmount());
            mqJo.put("sendCount", 0);
            MqProducer.sendMessage(mqJo, MqConfig.TICKET_HANDLE_REFUND_ORDER_RESULT, 2000);
        } else {
            log.info("订单[" + orderForm.getId() + "]--退款单[" + refundOrderFlow.getId() + "]" + "退款失败");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_REFUND_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_REFUND_FAIL.getValue());
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
            this.refundOrderFlowService.update(refundOrderFlow);
            this.orderFormService.update(orderForm);
        }
    }
}