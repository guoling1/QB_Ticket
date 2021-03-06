package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.controller.helper.request.RequestGrabTicket;
import com.jkm.controller.helper.request.RequestSubmitOrder;
import com.jkm.controller.helper.response.ResponseGrabTicket;
import com.jkm.entity.*;
import com.jkm.entity.fusion.SingleRefundData;
import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.enums.*;
import com.jkm.helper.InsurancePolicyUtil;
import com.jkm.helper.TicketMessageParams.*;
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
import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.commons.collections.CollectionUtils;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.SnGenerator;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private ConfirmOrderExceptionRecordService confirmOrderExceptionRecordService;

    @Autowired
    private TicketCallBackService ticketCallBackService;

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
        final RequestSubmitOrder.Passenger firstPassenger = passengerList.get(0);
        Preconditions.checkState(EnumTrainTicketType.ADULT.getId().equals(firstPassenger.getPiaoType()), "第一个乘客必须是成人");
        for (int i = 0; i < passengerList.size(); i++) {
            TbContactInfo contactInfo;
            RequestSubmitOrder.Passenger passenger = passengerList.get(i);
            if (EnumTrainTicketType.ADULT.getId().equals(passenger.getPiaoType())) {
                final Optional<TbContactInfo> contactInfoOptional1 = this.contactInfoService.selectById(passenger.getId());
                Preconditions.checkState(contactInfoOptional1.isPresent(), "乘客不存在");
                contactInfo = contactInfoOptional1.get();
            } else {
                contactInfo = this.contactInfoService.selectById(passengerList.get(0).getId()).get();
            }
            final OrderFormDetail orderFormDetail = new OrderFormDetail();
            final JSONObject passengerJsonObject = new JSONObject();
            orderFormDetail.setOrderFormId(orderForm.getId());
            orderFormDetail.setGrabTicketFormId(0);
            orderFormDetail.setIsGrab(0);
            orderFormDetail.setMobile(contactInfo.getTel());
            orderFormDetail.setPassengerId(passenger.getId());
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
            passengerJsonObject.put("passengerid", passenger.getId());
            passengerJsonObject.put("price", orderForm.getPrice());
            passengerJsonObject.put("zwcode", orderForm.getZwCode());
            passengerJsonObject.put("zwname", orderForm.getZwName());
            passengerJsonObject.put("piaotype", orderFormDetail.getPiaoType());
            passengerJsonObject.put("piaotypename", EnumTrainTicketType.of(orderFormDetail.getPiaoType()).getValue());
            passengerJsonObject.put("cxin", "");
            passengerJsonArray.add(passengerJsonObject);
        }
        log.info("用户[" + orderForm.getUid() + "]发送订单提交请求！！");
        final JSONObject jsonObject = this.hySdkService.submitOrderImpl(orderForm, passengerJsonArray);
//        final String code = jsonObject.getString("code");
        final boolean success = jsonObject.getBoolean("success");
        if (success) {// && ("802".equals(code) || "905".equals(code))
            log.info("用户[" + orderForm.getUid() + "]的订单[" + orderForm.getId() + "]提交受理成功(占座请求成功)--等待回调！！！");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getValue());
            this.orderFormService.update(orderForm);
            return Triple.of(true, jsonObject.getString("msg"), orderForm.getId());
        } else {
            log.info("用户[" + orderForm.getUid() + "]的订单[" + orderForm.getId() + "]占座请求失败!request:[" + jsonObject.toString() + "]");
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
            log.info("订单[" + orderForm.getId() + "]回调处理成功---占座成功");
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
                final String price = passengersJo.getString("price");
                if (!StringUtils.isBlank(price)) {
                    orderFormDetail.setPrice(new BigDecimal(price));
                }
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
            log.info("订单[" + orderForm.getId() + "]回调处理成功---占座失败");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getId());
            orderForm.setRemark(jsonObject.getString("msg"));
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = this.hySdkService.confirmTrainTicket(orderForm.getOrderId(), orderForm.getOutOrderId());
        } catch (Throwable e) {
            log.error("确认订单[" + orderForm.getId() + "]发生异常,记录数据，等待人工介入！！");
            final ConfirmOrderExceptionRecord confirmOrderExceptionRecord = new ConfirmOrderExceptionRecord();
            confirmOrderExceptionRecord.setStatus(0);
            confirmOrderExceptionRecord.setPaymentSn(orderForm.getPaymentSn());
            confirmOrderExceptionRecord.setOrderFormId(orderForm.getId());
            confirmOrderExceptionRecord.setAmount(orderForm.getTotalPrice());
            confirmOrderExceptionRecord.setRemark("确认出票出现异常");
            this.confirmOrderExceptionRecordService.add(confirmOrderExceptionRecord);
            return;
        }
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
            orderForm.setRemark(jsonObject.getString("msg"));
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
                log.error("订单[" + orderForm.getId() + "] 买保险异常,请手动购买..........", throwable);
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
        //给app推送消息
        try{
            log.info("给app推送代购结果信息, 订单id:" + orderForm.getId());
            this.ticketCallBackService.orderFormCallBack(orderForm);
        }catch (final Throwable throwable){
            log.info("给app推送代购结果信息, 异常信息:" + throwable.getMessage());
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
        final String reqToken = SnGenerator.generate();
        final Optional<OrderFormDetail> orderFormDetailOptional = this.orderFormDetailService.selectById(orderFormDetailId);
        Preconditions.checkArgument(orderFormDetailOptional.isPresent() , "订单不存在");
        final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
        //判断订单的状态是否可退, 若该订单下有儿童票不能退,先退儿童票
        if(orderFormDetail.getStatus() != EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getId()){
            return Pair.of(false, "此订单不能退票");
        }
        final boolean flag = this.isCanReturnTicket(orderFormDetailId);
        if (!flag){
            return Pair.of(false, "请先退儿童票");
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
                this.orderFormDetailService.updateStatusById(orderFormDetailId,EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                return Pair.of(false , "距离发车时间不到2小时,不能退票");
            }
            //可退票, 创建退票流水订单
            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUESTING);
            final RefundTicketFlow flow = new RefundTicketFlow();
            flow.setReqToken(reqToken);
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
            request.setReqToken(reqToken);
            request.setTransactionId(orderForm.getOutOrderId());
            JSONObject obj = new JSONObject();
            //查询乘客信息
            final Optional<TbContactInfo> optional = this.contactInfoService.selectById(orderFormDetail.getPassengerId());
            Preconditions.checkArgument(optional.isPresent(), "乘客信息不存在");
            final TbContactInfo tbContactInfo = optional.get();
            obj.put("passengername", tbContactInfo.getName());
            obj.put("passportseno",tbContactInfo.identyOrg(tbContactInfo.getIdenty()));
            obj.put("passporttypeseid",tbContactInfo.getIdentyType());
            obj.put("ticket_no",orderFormDetail.getTicketNo());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(obj);
                //发请求
                log.info("订单号:"+ orderFormDetailId + "请求退票, 退票请求中......");
                final HyReturnTicketResponse response = this.hySdkService.returnTicket(request, jsonArray);
                if(response.getSuccess().equals("true")){
                    log.info("订单号:"+ orderFormDetailId + "请求退票, 退票请求已接收......");
                    this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUESTING);
                    return Pair.of(true , "退票中,请等待");
                }else{
                    switch (response.getCode()){
                        case 116:
                            flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_FAIL.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                            return Pair.of(false, "退票失败");
                        case 117:
                            flow.setStatus(EnumRefundTicketFlowStatus.INIT.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUESTING);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求成功......");
                            return Pair.of(true, "退票中,请等待");
                        case 118:
                            flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_FAIL.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                            return Pair.of(false, "退票失败");
                        case 701:
                            flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_FAIL.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                            return Pair.of(false, "退票失败");
                        default:
                            flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_FAIL.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                            return Pair.of(false, "退票失败");
                    }

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
                this.orderFormDetailService.updateStatusById(orderFormDetailId,EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                return Pair.of(false , "距离发车时间不到2小时,不能退票");
            }
            //可退票, 创建退票流水订单
            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUESTING);
            final RefundTicketFlow flow = new RefundTicketFlow();
            flow.setReqToken(reqToken);
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
            request.setReqToken(reqToken);
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
                final HyReturnTicketResponse response = this.hySdkService.returnTicket(request, jsonArray);
                if(response.getSuccess().equals("true")){
                    log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求成功......");
                    this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUESTING);
                    return Pair.of(true , "退票中,请等待");
                }else{
                    switch (response.getCode()){
                        case 116:
                            flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_FAIL.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                            return Pair.of(false, "退票失败");
                        case 117:
                            flow.setStatus(EnumRefundTicketFlowStatus.INIT.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUESTING);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求成功......");
                            return Pair.of(true, "退票中,请等待");
                        case 118:
                            flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_FAIL.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                            return Pair.of(false, "退票失败");
                        case 701:
                            flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_FAIL.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                            return Pair.of(false, "退票失败");
                        default:
                            flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_FAIL.getId());
                            this.refundTicketFlowService.update(flow);
                            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                            log.error("订单号:"+ orderFormDetailId + "请求退票, 退票请求失败......");
                            return Pair.of(false, "退票失败");
                    }
                }
        }

    }

    private boolean isCanReturnTicket(long orderFormDetailId) {

        final Optional<OrderFormDetail> orderFormDetailOptional = this.orderFormDetailService.selectByIdWithLock(orderFormDetailId);
        Preconditions.checkNotNull(orderFormDetailOptional.isPresent(), "出票订单不存在");
        final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
        if (orderFormDetail.getPiaoType().equals(EnumTrainTicketType.CHILDREN.getId())){
            return true;
        }
        if (orderFormDetail.getIsGrab() == 0){
            //代购单
            final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByOrderFormId(orderFormDetail.getOrderFormId());
            for (OrderFormDetail formDetail : orderFormDetails){
                if (formDetail.getPassportSeNo().equals(orderFormDetail.getPassportSeNo())
                        && formDetail.getPiaoType().equals(EnumTrainTicketType.CHILDREN.getId())
                        && (formDetail.getStatus() == EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getId())
                        || formDetail.getStatus() == EnumOrderFormDetailStatus.TICKET_RETURN_FAIL.getId()){
                    return false;
                }
            }
        }else {
            //抢票单
            final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByGrabTicketFormId(orderFormDetail.getGrabTicketFormId());
            for (OrderFormDetail formDetail : orderFormDetails){
                if (formDetail.getPassportSeNo().equals(orderFormDetail.getPassportSeNo())
                        && formDetail.getPiaoType().equals(EnumTrainTicketType.CHILDREN.getId())
                        && (formDetail.getStatus() == EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getId())
                        || formDetail.getStatus() == EnumOrderFormDetailStatus.TICKET_RETURN_FAIL.getId()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param jsonObject
     */
    @Override
    @Transactional
    public void handleRefundCallbackMsg(final JSONObject jsonObject) {
        //退票成功, 修改小订单状态 , 退款给客户
        final JSONArray jsonArray = jsonObject.getJSONArray("returntickets");
        if (jsonObject.getInt("returntype") == 1) {
            //线上退票
            if (jsonObject.getBoolean("returnstate")) {
                //退票成功
                JSONObject obj = (JSONObject) jsonArray.get(0);
                final RefundTicketFlow flow = this.refundTicketFlowService.getByReqToken(jsonObject.getString("reqtoken"));
                final OrderFormDetail orderFormDetail = this.orderFormDetailService.selectById(flow.getOrderFormDetailId()).get();
                if(orderFormDetail.getStatus() == EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS.getId()){
                    return;
                }
                flow.setReturnType(1);
                flow.setReturnmoney(obj.getString("returnmoney"));
                flow.setSuccessTime(obj.getString("returntime"));
                flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_SUCCESS.getId());
                flow.setRemark("线上退票成功");
                log.info("线上退票结果推送" + flow.getOrderFormDetailId() + "订单退票成功,退款中");
                this.refundTicketFlowService.update(flow);
                //给app推送消息
                try{
                    log.info("给app推送退票结果信息, 退票单id:" + flow.getId());
                    this.ticketCallBackService.refundTicketCallBack(flow);
                }catch (final Throwable throwable){
                    log.info("给app推送退票结果信息, 异常信息:" + throwable.getMessage());
                }
                this.orderFormDetailService.updateStatusById(flow.getOrderFormDetailId(), EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                // 给用户退款, 是否有出票套餐, 创建退款单 , 先退保险 , 再退票款 , 抢票套餐不退
                final String uid;
                final String mobile;
                final String startStation;
                final String endStation;
                final String trainNo;
                final String startDate;
                final String paymentSn ;
                final int buyTicketPackageId ;
                final BigDecimal totalPrice;
                final Date createTime;
                if(orderFormDetail.getIsGrab() == 0){
                    final OrderForm orderForm = this.orderFormService.selectById(flow.getOrderFormId()).get();
                    uid = orderForm.getUid();
                    mobile = orderForm.getMobile();
                    startStation = orderForm.getFromStationName();
                    endStation = orderForm.getToStationName();
                    trainNo = orderForm.getCheci();
                    startDate = orderForm.getStartDate();
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
                    uid = orderForm.getUid();
                    mobile = orderForm.getPhone();
                    startStation = orderForm.getFromStationName();
                    endStation = orderForm.getToStationName();
                    trainNo = orderForm.getCheci();
                    startDate = orderForm.getStartDate();
                    paymentSn = orderForm.getPaymentSn();
                    buyTicketPackageId = orderForm.getBuyTicketPackage();
                    totalPrice = orderForm.getGrabTotalPrice();
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
                //发送短信
                final SendReturnTicketOnlineParam param = new SendReturnTicketOnlineParam();
                param.setUid(uid);
                param.setMobile(mobile);
                param.setUserName(obj.getString("passengername"));
                param.setStartStation(startStation);
                param.setEndStation(endStation);
                param.setTrainNo(trainNo);
                param.setStartDate(startDate);
                this.ticketSendMessageService.sendReturnTicketOnlineMessage(param);
                //退票款单
                final ReturnMoneyOrder returnMoneyOrder = new ReturnMoneyOrder();
                returnMoneyOrder.setOrderFormDetailId(orderFormDetail.getId());
                returnMoneyOrder.setOrderFormSn(paymentSn);
                returnMoneyOrder.setRemark("线上退票退款");
                //保险
                final PolicyOrder policyOrder = this.policyOrderService.getByOrderFormDetailId(orderFormDetail.getId());
                //先判断是否有出票套餐,有则退保险 , 无则不退
                if ((buyTicketPackageId != EnumBuyTicketPackageType.TICKET_PACKAGE_FIRST.getId())){
                    //先退保险
                    final HyCancelPolicyOrderRequest request = new HyCancelPolicyOrderRequest();
                    request.setUsername(HySdkConstans.USERNAME);
                    request.setMethod(EnumHTHYMethodCode.CANCEL_POLICY_ORDER.getCode());
                    request.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
                    request.setPolicyNo(policyOrder.getPolicyNo());
                    JSONObject object = null;
                    try{

                        if (policyOrder.getStatus() == EnumPolicyOrderStatus.POLICY_BUY_FAIL.getId()){
                            //如果用户保险购买失败,则不请求,
                            returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()).
                                    add(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice())));
                            returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice()));
                        }else{
                            //够买成功,退保
                            log.info("小订单[" + orderFormDetail.getId() +"]请求hy进行退保,请求中......");
                            object = this.hySdkService.cancelPolicyOrder(request);
                            if (object.getInt("resultId") == 0) {
                                //退保成功 , 计算退款金额, 退款金额 = 该乘客票款实退金额 + 出票套餐
                                //更新保险单状态
                                log.info("小订单[" + orderFormDetail.getId() +"]请求hy进行退保,退保成功......");
                                this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_SUCCESS);
                                returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()).
                                        add(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice())));
                                returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice()));
                            } else {
                                //退保失败
                                //更新保险单状态
                                log.info("小订单[" + orderFormDetail.getId() +"]请求hy进行退保,退保失败......");
                                this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_FAIL);
                                returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                                returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice()));
                            }
                        }

                    }catch (final Throwable throwable){
                        log.error("小订单[" + orderFormDetail.getId() +"]保险退保异常,异常信息:" + throwable.getMessage());
                        this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_FAIL);
                        returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                        returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice()));
                    }

                }else{
                    returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                    returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(0));
                }

                    returnMoneyOrder.setOrgMoney(totalPrice);
                    returnMoneyOrder.setOrgDate(createTime);
                    returnMoneyOrder.setReturnTicketMoney(new BigDecimal(flow.getReturnmoney()));
                    returnMoneyOrder.setReturnGrabTicketPackage(new BigDecimal(0));
                    returnMoneyOrder.setStatus(EnumReturnMoneyOrderStatus.INIT.getId());
                    this.returnMoneyOrderService.init(returnMoneyOrder);
                    //请求退款接口 , 退款
                    final SingleRefundData data = new SingleRefundData();
                    data.setReqSn(SnGenerator.generate());
                    data.setOrgSn(returnMoneyOrder.getOrderFormSn());
                    data.setOrgDate(DateFormatUtil.format(returnMoneyOrder.getOrgDate(), DateFormatUtil.yyyyMMdd));
                    data.setRefundAmount(returnMoneyOrder.getReturnTotalMoney().toString());
                    data.setOrgAmount(returnMoneyOrder.getOrgMoney().toString());
                    data.setRefundReason(returnMoneyOrder.getRemark());
                    log.info("小订单[" + orderFormDetail.getId() + "请求hz进行退款....请求中");
                    final Map<String, Object> map = this.authenService.singlRefund(data);
                //判断退款是否受理成功 ,
                    if (map.get("retCode").equals("0000") || map.get("retCode").equals("5000")){
                        //受理成功 , 修改退票流水单状态, 退款中
                        //TODO 退票退款
                        JSONObject mqJo = new JSONObject();
                        mqJo.put("orderFormDetailId", orderFormDetail.getId());
                        mqJo.put("reqToken", flow.getReqToken());
                        mqJo.put("reqSn", data.getReqSn());
                        mqJo.put("sendCount", 0);
                        MqProducer.sendMessage(mqJo, MqConfig.RETURN_TICKET_REFUND_ING, 2000);
                        flow.setStatus(EnumRefundTicketFlowStatus.TICKET_REFUND_ING.getId());
                        this.refundTicketFlowService.update(flow);
                        log.info("小订单[" + orderFormDetail.getId() + "请求hz进行退款....受理成功");
                        //退款成功, 修改退票流水 , 退款单状态
                        //this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_SUCCESS);
                    }else{
                        log.info("小订单[" + orderFormDetail.getId() + "请求hz进行退款....失败");
                        //受理失败 , 修改退票流水单状态, 退款失败
                        flow.setStatus(EnumRefundTicketFlowStatus.TICKET_REFUND_FAIL.getId());
                        this.refundTicketFlowService.update(flow);
                       this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_FAIL);
                    }
            } else {
                        //退票失败
                        JSONObject obj = (JSONObject) jsonArray.get(0);
                        final RefundTicketFlow flow = this.refundTicketFlowService.getByTicketNo(obj.getString("ticket_no"));
                         log.info("线上退票结果推送" + flow.getOrderFormDetailId() + "订单退票失败");
                        flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_FAIL.getId());
                        flow.setRemark("线上退票失败" + obj.getString("returnfailmsg"));
                        this.refundTicketFlowService.update(flow);
                        //给app推送消息
                        try{
                            log.info("给app推送退票结果信息, 退票单id:" + flow.getId());
                            this.ticketCallBackService.refundTicketCallBack(flow);
                        }catch (final Throwable throwable){
                            log.info("给app推送退票结果信息, 异常信息:" + throwable.getMessage());
                        }
                        this.orderFormDetailService.updateStatusById(flow.getOrderFormDetailId(), EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                    }
        }
            else{
   /////////////////////////////*****线下退款回调(改签,退票)*****/////////////////////////
                //线下退款,或改签退款
                if (jsonObject.getBoolean("returnstate")) {
                    //退票成功
                    JSONObject obj = (JSONObject) jsonArray.get(0);
                    final Optional<OrderFormDetail> orderFormDetailOptional = this.orderFormDetailService.selectByTicketNo(obj.getString("ticket_no"));
                    Preconditions.checkState(orderFormDetailOptional.isPresent(), "出票订单不存在");
                    final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
                    if(orderFormDetail.getStatus() == EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS.getId()){
                        return;
                    }
                    //更新出票订单状态
                    log.info("线下退票结果推送" + orderFormDetail.getId() + "订单退票成功, 退款中......");
                    this.orderFormDetailService.updateStatusById(orderFormDetail.getId(), EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                    //给用户退款, 是否有出票套餐, 创建退款单 , 先退保险 , 再退票款 , 抢票套餐不退
                    final String uid;
                    final String mobile;
                    final String startStation;
                    final String endStation;
                    final String trainNo;
                    final String amount;
                    final String startDate;
                    final String paymentSn ;
                    final int buyTicketPackageId ;
                    final BigDecimal totalPrice;
                    final Date createTime;
                    final RefundTicketFlow flow = new RefundTicketFlow();
                    if(orderFormDetail.getIsGrab() == 0){
                        //订购退票
                        final OrderForm orderForm = this.orderFormService.selectByOrderId(jsonObject.getString("apiorderid")).get();
                        flow.setReqToken(jsonObject.getString("reqtoken"));
                        flow.setReturnType(jsonObject.getInt("returntype"));
                        flow.setOrderFormId(orderForm.getId());
                        flow.setGrabTicketFormId(0);
                        flow.setOrderFormDetailId(orderFormDetail.getId());
                        flow.setTicketNo(orderFormDetail.getTicketNo());
                        flow.setApplyTime(new Date());
                        flow.setReturnmoney(obj.getString("returnmoney"));
                        flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_SUCCESS.getId());
                        this.refundTicketFlowService.init(flow);
                        uid = orderForm.getUid();
                        mobile = orderForm.getMobile();
                        startStation = orderForm.getFromStationName();
                        endStation = orderForm.getToStationName();
                        trainNo = orderForm.getCheci();
                        startDate = orderForm.getStartDate();
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
                        //抢票单退票
                        final GrabTicketForm grabTicketForm = this.grabTicketFormService.selectByOrderIdWithLock(jsonObject.getString("apiorderid")).get();
                        flow.setReqToken(jsonObject.getString("reqtoken"));
                        flow.setReturnType(jsonObject.getInt("returntype"));
                        flow.setOrderFormId(0);
                        flow.setGrabTicketFormId(grabTicketForm.getId());
                        flow.setOrderFormDetailId(orderFormDetail.getId());
                        flow.setTicketNo(orderFormDetail.getTicketNo());
                        flow.setApplyTime(new Date());
                        flow.setReturnmoney(obj.getString("returnmoney"));
                        flow.setStatus(EnumRefundTicketFlowStatus.REFUND_TICKET_SUCCESS.getId());
                        this.refundTicketFlowService.init(flow);
                        uid = grabTicketForm.getUid();
                        mobile = grabTicketForm.getPhone();
                        startStation = grabTicketForm.getFromStationName();
                        endStation = grabTicketForm.getToStationName();
                        trainNo = grabTicketForm.getCheci();
                        startDate = grabTicketForm.getStartDate();
                        paymentSn = grabTicketForm.getPaymentSn();
                        buyTicketPackageId = grabTicketForm.getBuyTicketPackage();
                        totalPrice = grabTicketForm.getTotalPrice();
                        createTime = grabTicketForm.getCreateTime();
                        //如果退票成功, 判断大订单是否全部退票
                        final List<OrderFormDetail> list = this.orderFormDetailService.selectAllTicketsNoReFundGrab(grabTicketForm.getId(), EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                        final long count = this.orderFormDetailService.selectGrabFormNumGrab(grabTicketForm.getId());
                        if (list.size() == (int)count){
                            //全部退票
                            grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_HAVE_BEEN_RETURN_TICKET.getId());
                            this.grabTicketFormService.update(grabTicketForm);
                        }
                    }

                    ReturnMoneyOrder returnMoneyOrder = new ReturnMoneyOrder();
                    returnMoneyOrder.setOrderFormDetailId(orderFormDetail.getId());
                    returnMoneyOrder.setOrderFormSn(paymentSn);
                    returnMoneyOrder.setRemark("线下退票退款");
                    final PolicyOrder policyOrder = this.policyOrderService.getByOrderFormDetailId(orderFormDetail.getId());
                    //先判断是否有出票套餐,有则退保险 , 无则不退
                    if ((buyTicketPackageId != EnumBuyTicketPackageType.TICKET_PACKAGE_FIRST.getId())){
                        //先退保险
                        final HyCancelPolicyOrderRequest request = new HyCancelPolicyOrderRequest();
                        request.setUsername(HySdkConstans.USERNAME);
                        request.setMethod(EnumHTHYMethodCode.CANCEL_POLICY_ORDER.getCode());
                        request.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
                        request.setPolicyNo(policyOrder.getPolicyNo());
                        final JSONObject object;
                        try{

                            if (policyOrder.getStatus() == EnumPolicyOrderStatus.POLICY_BUY_FAIL.getId()){
                                //如果用户保险购买失败,则不请求,
                                returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()).
                                        add(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice())));
                                returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice()));
                            }else{
                                log.info("小订单[" + orderFormDetail.getId() +"]请求hy进行退保,请求中......");
                                object = this.hySdkService.cancelPolicyOrder(request);
                                if (object.getInt("resultId") == 0) {
                                    //退保成功 , 计算退款金额, 退款金额 = 该乘客票款实退金额 + 出票套餐
                                    //更新保险单状态
                                    this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_SUCCESS);
                                    //有出票套餐
                                    returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()).
                                            add(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice())));
                                    returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(EnumBuyTicketPackageType.of(buyTicketPackageId).getPrice()));
                                    log.info("小订单[" + orderFormDetail.getId() +"]请求hy进行退保,退保成功......");
                                } else {
                                    //退保失败
                                    //更新保险单状态
                                    this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_FAIL);
                                    //有出票套餐
                                    returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                                    returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(0));
                                    log.error("小订单[" + orderFormDetail.getId() +"]请求hy进行退保,退保失败......");
                                }
                            }

                        }catch (final Throwable throwable){
                            this.policyOrderService.updateStatusById(policyOrder.getId(), EnumPolicyOrderStatus.POLICY_RETURN_FAIL);
                            returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                            returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(0));
                            log.error("小订单[" + orderFormDetail.getId() +"]保险退保异常,异常信息:" + throwable.getMessage());
                        }

                    }else{
                        //无出票套餐
                        returnMoneyOrder.setReturnTotalMoney(new BigDecimal(flow.getReturnmoney()));
                        returnMoneyOrder.setReturnBuyTicketPackage(new BigDecimal(0));
                    }

                    returnMoneyOrder.setOrgMoney(totalPrice);
                    returnMoneyOrder.setOrgDate(createTime);
                    returnMoneyOrder.setReturnTicketMoney(new BigDecimal(flow.getReturnmoney()));
                    returnMoneyOrder.setReturnGrabTicketPackage(new BigDecimal(0));
                    returnMoneyOrder.setStatus(EnumReturnMoneyOrderStatus.INIT.getId());
                    this.returnMoneyOrderService.init(returnMoneyOrder);
                    //发送退票短信
                    final SendReturnTicketDownParam param = new SendReturnTicketDownParam();
                    param.setUid(uid);
                    param.setMobile(mobile);
                    param.setUserName(obj.getString("passengername"));
                    param.setStartStation(startStation);
                    param.setEndStation(endStation);
                    param.setTrainNo(trainNo);
                    param.setAmount(returnMoneyOrder.getReturnTotalMoney().toString());
                    param.setStartDate(startDate);
                    this.ticketSendMessageService.sendReturnTicketDownMessage(param);
                    //请求退款接口 , 退款
                    final SingleRefundData data = new SingleRefundData();
                    data.setReqSn(SnGenerator.generate());
                    data.setOrgSn(returnMoneyOrder.getOrderFormSn());
                    data.setOrgDate(DateFormatUtil.format(returnMoneyOrder.getOrgDate(), DateFormatUtil.yyyyMMdd));
                    data.setRefundAmount(returnMoneyOrder.getReturnTotalMoney().toString());
                    data.setOrgAmount(returnMoneyOrder.getOrgMoney().toString());
                    data.setRefundReason(returnMoneyOrder.getRemark());
                    log.info("小订单[" + orderFormDetail.getId() + "请求hz进行退款....请求中");
                    final Map<String, Object> map = this.authenService.singlRefund(data);
                    //判断退款受理是否成功 ,
                    if (map.get("retCode").equals("0000") || map.get("retCode").equals("5000")){
                        //TODO 退票退款
                        JSONObject mqJo = new JSONObject();
                        mqJo.put("orderFormDetailId", orderFormDetail.getId());
                        mqJo.put("reqSn", data.getReqSn());
                        mqJo.put("sendCount", 0);
                        mqJo.put("reqToken", flow.getReqToken());
                        MqProducer.sendMessage(mqJo, MqConfig.RETURN_TICKET_REFUND_ING, 2000);
                        flow.setStatus(EnumRefundTicketFlowStatus.TICKET_REFUND_ING.getId());
                        this.refundTicketFlowService.update(flow);
                        log.info("小订单[" + orderFormDetail.getId() + "请求hz进行退款....受理成功");
                        //退款成功, 修改退票流水 , 退款单状态
                        //this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_SUCCESS);
                    }else{
                        //失败.
                        flow.setStatus(EnumRefundTicketFlowStatus.TICKET_REFUND_FAIL.getId());
                        this.refundTicketFlowService.update(flow);
                        this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_FAIL);
                        log.info("小订单[" + orderFormDetail.getId() + "请求hz进行退款....失败");

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
        Preconditions.checkState(orderForm.isCustomerPaying(), "处理客户付款，订单[%s]的状态不是在支付中！！！", orderFormId);
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
    public ResponseGrabTicket grabTicket(final RequestGrabTicket req) {

        final JSONObject jsonObject = this.queryTicketPriceService.queryTicket(req.getUid(),HySdkConstans.QUERY_PARTNER_ID, "train_query", req.getFromStationCode(), req.getToStationCode(),req.getFromStationName(), req.getToStationName(), req.getGrabStartTime(), "ADULT");
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
        grabTicketForm.setExpireTime(new Date(new Date().getTime() + 15*60*1000));
        grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_PAY_WAIT.getId());
        //初始化抢票单, 创建收款单收款
        this.grabTicketFormService.init(grabTicketForm);
        log.info("抢票单id:" + grabTicketForm.getId() + ">>>>>>>>>>最高价格为:"+ grabPrice +"<<<<<<<<<<<<<<<<<<<");
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
        log.info("抢票单ID:"+ grabTicketForm.getId() + "抢票单待支付15分钟,放入消息队列");
        JSONObject mqJo = new JSONObject();
        mqJo.put("grabTicketFormId",grabTicketForm.getId());
        MqProducer.sendMessage(mqJo, MqConfig.TICKET_CANCEL_EXPIRED_GRAB_ORDER, 1000*15*60);

        final ResponseGrabTicket responseGrabTicket = new ResponseGrabTicket();
        responseGrabTicket.setGrabTicketFormId(grabTicketForm.getId());
        if (new BigDecimal("0").compareTo(new BigDecimal(grabPrice)) == 0){
            responseGrabTicket.setPrice(new BigDecimal("0"));
        }else{
            responseGrabTicket.setPrice(grabTicketForm.getGrabTotalPrice());
        }
        return responseGrabTicket;
    }

    /**
     * {@inheritDoc}
     *
     * @param grabTicketFormId
     * @param paymentSn
     * @param isPaySuccess
     */
    @Override
    @Transactional
    public void handleGrabCustomerPayMsg(long grabTicketFormId, String paymentSn, boolean isPaySuccess) throws Exception {
        log.info("抢票单:" + grabTicketFormId + "支付结果回调, 支付结果:" + isPaySuccess);
        final Optional<GrabTicketForm> orderFormOptional = this.grabTicketFormService.selectByIdWithLock(grabTicketFormId);
        final GrabTicketForm orderForm = orderFormOptional.get();
        //Preconditions.checkState(orderForm.isPaySuccess(), "抢票单[%s]的状态不是支付成功状态！！！" , grabTicketFormId);
        final Optional<ChargeMoneyOrder> chargeMoneyOrderOptional = this.chargeMoneyOrderService.selectByGrabTicketFormId(grabTicketFormId);
        Preconditions.checkState(chargeMoneyOrderOptional.isPresent(), "订单[%s]对应的收款记录不存在", grabTicketFormId);
        final ChargeMoneyOrder chargeMoneyOrder = this.chargeMoneyOrderService.selectByIdWithLock(chargeMoneyOrderOptional.get().getId()).get();
        Preconditions.checkState(!chargeMoneyOrder.isPaySuccess(), "订单[%s]对应的收款记录已经付款成功！！！！！！！", orderForm.getId());
        if (isPaySuccess) {
            log.info("抢票单[" + grabTicketFormId + "]支付成功");
            orderForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_PAY_SUCCESS.getId());
            orderForm.setRemark(EnumGrabTicketStatus.GRAB_FORM_PAY_SUCCESS.getValue());
            orderForm.setPaymentSn(paymentSn);
            this.grabTicketFormService.update(orderForm);
            chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.PAYMENT_TICKET_SUCCESS.getId());
            log.info("抢票单[" + grabTicketFormId + "]支付成功--调用抢票下单接口！！");
            //抢票支付成功, 请求抢票
                this.requestGrabImpl(grabTicketFormId);
        } else {
            // 抢票单支付失败, 记录流水号
            orderForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_PAY_FAIL.getId());
            orderForm.setRemark(EnumGrabTicketStatus.GRAB_FORM_PAY_FAIL.getValue());
            orderForm.setPaymentSn(paymentSn);
            this.grabTicketFormService.update(orderForm);
            chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.PAYMENT_TICKET_FAIL.getId());
            log.info("抢票单[" + grabTicketFormId + "]支付失败--............！");
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
        if (grabTicketForm.getStatus() != EnumGrabTicketStatus.GRAB_FORM_REQUEST_SUCCESS.getId()){
            return;//防止重复请求
        }
        if(jsonParams.getBoolean("success") == true && jsonParams.getInt("code") == 100){
            log.info("hy抢票回调通知" + grabTicketForm.getId() + "抢票单抢票成功");
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
                orderFormDetail.setPassportSeNo(UserBankCardSupporter.encryptCardId(obj.getString("passportseno")));
                orderFormDetail.setPassportTypeSeId(obj.getString("passporttypeseid"));
                orderFormDetail.setPassportTypeSeName(obj.getString("passporttypeseidname"));
                orderFormDetail.setTicketNo(obj.getString("ticket_no"));
                orderFormDetail.setPrice(new BigDecimal(obj.getString("price")));
                orderFormDetail.setCheci(grabTicketForm.getCheci());
                orderFormDetail.setPiaoType(obj.getString("piaotype"));
                orderFormDetail.setCxin(obj.getString("cxin"));
                orderFormDetail.setStatus(EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getId());
                if(obj.getString("piaotype").equals(EnumTrainTicketType.ADULT.getId())){
                    grabTicketForm.setPrice(new BigDecimal(obj.getString("price")));
                    grabTicketForm.setZwname(obj.getString("zwname"));
                }
                this.orderFormDetailService.add(orderFormDetail);
            }
            this.grabTicketFormService.update(grabTicketForm);
            //给app推送消息
            try{
                log.info("给app推送抢票结果信息, 抢票单id:" + grabTicketForm.getId());
                this.ticketCallBackService.grabFormCallBack(grabTicketForm);
            }catch (final Throwable throwable){
                log.info("给app推送抢票结果信息发生异常, 异常信息:" + throwable.getMessage());
            }
            //退差价
            final BigDecimal subtract = grabTicketForm.getGrabTicketTotalPrice().subtract(grabTicketForm.getTicketTotalPrice());
            if (subtract.compareTo(new BigDecimal(0)) != 0){
                final SendGrabTicketSuccessParam param = new SendGrabTicketSuccessParam();
                param.setUid(grabTicketForm.getUid());
                param.setMobile(grabTicketForm.getPhone());
                param.setStartStation(grabTicketForm.getFromStationName());
                param.setEndStation(grabTicketForm.getToStationName());
                param.setTrainNo(grabTicketForm.getCheci());
                param.setStartDate(grabTicketForm.getStartDate());
                param.setStartTime(grabTicketForm.getStartTime());
                param.setTicketNo(grabTicketForm.getOrderNumber());
                param.setResidueAmount(subtract.toString());
                this.ticketSendMessageService.sendGrabTicketSuccessHaveResidueMessage(param);
                //有差价,才退款
                final Optional<RefundOrderFlow> optional = this.refundOrderFlowService.selectByGrabTicketFormId(grabTicketForm.getId());
                final RefundOrderFlow refundOrderFlow;
                if (optional.isPresent()){
                    refundOrderFlow = optional.get();
                    refundOrderFlow.setOrderFormId(0);
                    refundOrderFlow.setGrabTicketFormId(grabTicketForm.getId());
                    refundOrderFlow.setPaymentSn(grabTicketForm.getPaymentSn());
                    refundOrderFlow.setOrderDate(DateFormatUtil.format(grabTicketForm.getCreateTime(), DateFormatUtil.yyyyMMdd));
                    refundOrderFlow.setRefundAmount(subtract);
                    refundOrderFlow.setOriginalAmount(grabTicketForm.getGrabTotalPrice());
                    refundOrderFlow.setRefundReason("抢票成功差价退款");
                    this.refundOrderFlowService.update(refundOrderFlow);
                }else{
                    refundOrderFlow = new RefundOrderFlow();
                    refundOrderFlow.setOrderFormId(0);
                    refundOrderFlow.setGrabTicketFormId(grabTicketForm.getId());
                    refundOrderFlow.setPaymentSn(grabTicketForm.getPaymentSn());
                    refundOrderFlow.setOrderDate(DateFormatUtil.format(grabTicketForm.getCreateTime(), DateFormatUtil.yyyyMMdd));
                    refundOrderFlow.setRefundAmount(subtract);
                    refundOrderFlow.setOriginalAmount(grabTicketForm.getGrabTotalPrice());
                    refundOrderFlow.setRefundReason("抢票成功差价退款");
                    this.refundOrderFlowService.insert(refundOrderFlow);
                }

                //请求退款接口 , 退款
                final SingleRefundData data = new SingleRefundData();
                data.setReqSn(SnGenerator.generate());
                data.setOrgSn(refundOrderFlow.getPaymentSn());
                data.setOrgDate(refundOrderFlow.getOrderDate());
                data.setRefundAmount(refundOrderFlow.getRefundAmount().toString());
                data.setOrgAmount(refundOrderFlow.getOriginalAmount().toString());
                data.setRefundReason(refundOrderFlow.getRefundReason());
                log.info("抢票单:"+ grabTicketForm.getId()+ "请求hz退款接口退款");
                final Map<String, Object> map = this.authenService.singlRefund(data);
                //判断退款受理是否成功 ,
                if (map.get("retCode").equals("0000") || map.get("retCode").equals("5000")){
                    log.info("抢票单:"+ grabTicketForm.getId()+ "请求hz退还差价,请求受理成功");
                    //TODO 抢票差价退款
                    //受理成功 ,
                    //消息
                    JSONObject mqJo = new JSONObject();
                    mqJo.put("grabTicketFormId", grabTicketForm.getId());
                    mqJo.put("reqSn", data.getReqSn());
                    mqJo.put("refundAmount", data.getRefundAmount());
                    mqJo.put("sendCount", 0);
                    MqProducer.sendMessage(mqJo, MqConfig.GRAB_TICKET_REFUND_PART, 2000);
                    //this.grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_REFUND_ING,grabTicketForm.getId());
                }else{
                    //受理失败.修改流水单状态
                    log.info("抢票单:"+ grabTicketForm.getId()+ "请求hz退还差价,受理失败, 退款失败"+ map.get("signMsg"));
                    refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
                    this.refundOrderFlowService.update(refundOrderFlow);
                    //grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REFUND_FAIL.getId());
                    //grabTicketForm.setRemark(map.get("signMsg").toString());
                    //this.grabTicketFormService.update(grabTicketForm);
                }
            }else{
                //发送短信
                final SendGrabTicketSuccessParam param = new SendGrabTicketSuccessParam();
                param.setUid(grabTicketForm.getUid());
                param.setMobile(grabTicketForm.getPhone());
                param.setStartStation(grabTicketForm.getFromStationName());
                param.setEndStation(grabTicketForm.getToStationName());
                param.setTrainNo(grabTicketForm.getCheci());
                param.setStartDate(grabTicketForm.getStartDate());
                param.setStartTime(grabTicketForm.getStartTime());
                param.setTicketNo(grabTicketForm.getOrderNumber());
                this.ticketSendMessageService.sendGrabTicketSuccessHaveNotResidueMessage(param);
            }
            //购买保险
            try{
                this.policyOrderService.batchBuyGrabPolicy(grabTicketForm.getId());
            }catch (final Throwable throwable){
                log.error("抢票单[" + grabTicketForm.getId() + "] 买保险异常,请手动购买..........", throwable);
            }

        }else{
            log.info("hy抢票回调通知" + grabTicketForm.getId() + "抢票单抢票失败,全额退款");
            //抢票时间到期,抢票失败 , 全额退款
            //TODO 更新抢票单状态
            this.grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_FAIL, grabTicketForm.getId());
            //给app推送消息
            try{
                log.info("给app推送抢票结果信息, 抢票单id:" + grabTicketForm.getId());
                this.ticketCallBackService.grabFormCallBack(grabTicketForm);
            }catch (final Throwable throwable){
                log.info("给app推送抢票结果信息发生异常, 异常信息:" + throwable.getMessage());
            }
            //全额退款
            this.returnToGrabFail(grabTicketForm.getId());
        }

    }

    /**
     * 抢票失败给用户全额退票
     */
    public void returnToGrabFail(final long grabTicketFormId){
        final Optional<GrabTicketForm> grabTicketFormOptional = this.grabTicketFormService.selectByIdWithLock(grabTicketFormId);
        Preconditions.checkArgument(grabTicketFormOptional.isPresent(), "订单不存在");
        final GrabTicketForm grabTicketForm = grabTicketFormOptional.get();

        final Optional<RefundOrderFlow> optional = this.refundOrderFlowService.selectByGrabTicketFormId(grabTicketFormId);
        final RefundOrderFlow refundOrderFlow;
        if (optional.isPresent()){
            refundOrderFlow = optional.get();
            refundOrderFlow.setOrderFormId(0);
            refundOrderFlow.setGrabTicketFormId(grabTicketForm.getId());
            refundOrderFlow.setPaymentSn(grabTicketForm.getPaymentSn());
            refundOrderFlow.setOrderDate(DateFormatUtil.format(grabTicketForm.getCreateTime(), DateFormatUtil.yyyyMMdd));
            refundOrderFlow.setRefundAmount(grabTicketForm.getGrabTotalPrice());
            refundOrderFlow.setOriginalAmount(grabTicketForm.getGrabTotalPrice());
            refundOrderFlow.setRefundReason("抢票失败,全额退款");
            this.refundOrderFlowService.update(refundOrderFlow);
        }else{
            refundOrderFlow  = new RefundOrderFlow();
            refundOrderFlow.setOrderFormId(0);
            refundOrderFlow.setGrabTicketFormId(grabTicketForm.getId());
            refundOrderFlow.setPaymentSn(grabTicketForm.getPaymentSn());
            refundOrderFlow.setOrderDate(DateFormatUtil.format(grabTicketForm.getCreateTime(), DateFormatUtil.yyyyMMdd));
            refundOrderFlow.setRefundAmount(grabTicketForm.getGrabTotalPrice());
            refundOrderFlow.setOriginalAmount(grabTicketForm.getGrabTotalPrice());
            refundOrderFlow.setRefundReason("抢票失败,全额退款");
            this.refundOrderFlowService.insert(refundOrderFlow);
        }
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
        data.setReqSn(SnGenerator.generate());
        data.setOrgSn(refundOrderFlow.getPaymentSn());
        data.setOrgDate(refundOrderFlow.getOrderDate());
        data.setRefundAmount(refundOrderFlow.getRefundAmount().toString());
        data.setOrgAmount(refundOrderFlow.getOriginalAmount().toString());
        data.setRefundReason(refundOrderFlow.getRefundReason());
        log.info("抢票订单["+ grabTicketFormId +"]抢票失败到期请求自动退款......请求hz退款,请求中....");
        final Map<String, Object> map = this.authenService.singlRefund(data);
        //判断退款是否受理成功 ,
        //TODO 抢票全额退款
        if (map.get("retCode").equals("0000") || map.get("retCode").equals("5000")){
            this.grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_REFUND_ING,grabTicketFormId);
            log.info("抢票订单["+ grabTicketFormId +"]抢票失败到期请求自动退款......请求hz退款,受理成功....");
            //成功 ,
            //消息
            JSONObject mqJo = new JSONObject();
            mqJo.put("grabTicketFormId", grabTicketForm.getId());
            mqJo.put("reqSn", data.getReqSn());
            mqJo.put("refundAmount", data.getRefundAmount());
            mqJo.put("sendCount", 0);
            MqProducer.sendMessage(mqJo, MqConfig.GRAB_TICKET_REFUND_ALL, 2000);
            // refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId());
            //this.refundOrderFlowService.update(refundOrderFlow);
        }else{
            //失败.
            this.grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_REFUND_FAIL,grabTicketFormId);
            log.error("抢票订单["+ grabTicketFormId +"]抢票失败到期请求自动退款......请求hz退款,受理失败,退款失败,人工退款....");
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
        if (EnumGrabTicketStatus.GRAB_FORM_REQUEST_SUCCESS.getId() != grabTicketForm.getStatus() &&
                EnumGrabTicketStatus.WAIT_FOR_REFUND.getId() != grabTicketForm.getStatus()){
            return Pair.of(false, "此抢票单不能取消");
        }

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("qorderid", grabTicketForm.getOrderId());
        jsonObject.put("partnerid", HySdkConstans.GRAB_PARTNER_ID);
        jsonObject.put("reqtime", DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
        jsonObject.put("sign", MD5Util.MD5(HySdkConstans.GRAB_PARTNER_ID + DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss) + MD5Util.MD5(HySdkConstans.GRAB_SIGN_KEY)));
        log.info(grabTicketFormId + "抢票单请求hy取消订单,请求中........");
        final JSONObject jsonResponse = this.hySdkService.cancelGrabTickets(jsonObject);
        if("success".equals(jsonResponse.getString("isSuccess")) || EnumGrabTicketStatus.WAIT_FOR_REFUND.getId() == grabTicketForm.getStatus()){
            //发送短信
            final SendCancelGrabTicketParam param = new SendCancelGrabTicketParam();
            param.setUid(grabTicketForm.getUid());
            param.setMobile(grabTicketForm.getPhone());
            param.setStartStation(grabTicketForm.getFromStationName());
            param.setEndStation(grabTicketForm.getToStationName());
            param.setStartDate(grabTicketForm.getGrabStartTime());
            this.ticketSendMessageService.sendCancelGrabTicketMessage(param);
            log.info(grabTicketFormId + "抢票单请求hy取消订单,请求成功,取消成功........");
            //抢票单取消成功, 退款
            final Optional<RefundOrderFlow> optional = this.refundOrderFlowService.selectByGrabTicketFormId(grabTicketFormId);
            final RefundOrderFlow refundOrderFlow;
            if (optional.isPresent()){
                refundOrderFlow = optional.get();
                refundOrderFlow.setOrderFormId(0);
               refundOrderFlow.setGrabTicketFormId(grabTicketForm.getId());
               refundOrderFlow.setPaymentSn(grabTicketForm.getPaymentSn());
               refundOrderFlow.setOrderDate(DateFormatUtil.format(grabTicketForm.getCreateTime(),DateFormatUtil.yyyyMMdd));
               refundOrderFlow.setRefundAmount(grabTicketForm.getGrabTotalPrice());
               refundOrderFlow.setOriginalAmount(grabTicketForm.getGrabTotalPrice());
               refundOrderFlow.setRefundReason("取消抢票退款");
               refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.INIT.getId());
               this.refundOrderFlowService.update(refundOrderFlow);
            }else{
                refundOrderFlow  = new RefundOrderFlow();
               refundOrderFlow.setOrderFormId(0);
               refundOrderFlow.setGrabTicketFormId(grabTicketForm.getId());
               refundOrderFlow.setPaymentSn(grabTicketForm.getPaymentSn());
               refundOrderFlow.setOrderDate(DateFormatUtil.format(grabTicketForm.getCreateTime(),DateFormatUtil.yyyyMMdd));
               refundOrderFlow.setRefundAmount(grabTicketForm.getGrabTotalPrice());
               refundOrderFlow.setOriginalAmount(grabTicketForm.getGrabTotalPrice());
               refundOrderFlow.setRefundReason("取消抢票退款");
               refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.INIT.getId());
               this.refundOrderFlowService.insert(refundOrderFlow);
           }
            //请求退款接口 , 退款
            final SingleRefundData data = new SingleRefundData();
            data.setReqSn(SnGenerator.generate());
            data.setOrgSn(refundOrderFlow.getPaymentSn());
            data.setOrgDate(refundOrderFlow.getOrderDate());
            data.setRefundAmount(refundOrderFlow.getRefundAmount().toString());
            data.setOrgAmount(refundOrderFlow.getOriginalAmount().toString());
            data.setRefundReason(refundOrderFlow.getRefundReason());
            log.info("抢票单["+grabTicketFormId + "]取消抢票单成功,请求hz全额退款,请求中...........");
            final Map<String, Object> map = this.authenService.singlRefund(data);
            //判断退款受理是否成功 ,
            if (map.get("retCode").equals("0000") || map.get("retCode").equals("5000")){
                //成功 ,
                //TODO 取消抢票单退款
                //消息
                log.info("抢票单["+grabTicketFormId + "]取消抢票单成功,请求hz全额退款,受理成功...........");
                JSONObject mqJo = new JSONObject();
                mqJo.put("grabTicketFormId", grabTicketForm.getId());
                mqJo.put("reqSn", data.getReqSn());
                mqJo.put("refundAmount", data.getRefundAmount());
                mqJo.put("sendCount", 0);
                MqProducer.sendMessage(mqJo, MqConfig.CANCEL_GRAB_TICKET_REFUND_ALL, 2000);
                grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REFUND_ING.getId());
                this.grabTicketFormService.update(grabTicketForm);
                //this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_SUCCESS);
            }else{
                //失败.
                log.info("抢票单["+grabTicketFormId + "]取消抢票单成功,请求hz全额退款,受理失败,退款失败,人工处理...........");
                grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REFUND_FAIL.getId());
                this.grabTicketFormService.update(grabTicketForm);
                refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
                this.refundOrderFlowService.update(refundOrderFlow);
            }
            return Pair.of(true, "取消抢票单成功");
        }else{
            log.info(grabTicketFormId + "抢票单请求hy取消订单,请求成功,取消失败........");
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
    @Transactional
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
            log.info("抢票单["+grabTicketForm.getId()+"]未购买套餐,放入消息队列,到期退款.........");
            grabTicketForm.setStatus(EnumGrabTicketStatus.WAIT_FOR_REFUND.getId());
            this.grabTicketFormService.update(grabTicketForm);
            //放入消息队列 ,
            //TODO 抢票
            JSONObject mqJo = new JSONObject();
            mqJo.put("grabTicketFormId",grabTicketForm.getId());
            MqProducer.sendMessage(mqJo, MqConfig.NO_PACKAGE_WAIT_REFUND, endTime.getTime() - (new Date()).getTime());
            return;
        }
        //请求抢票
        final JSONObject jsonObject = new JSONObject();
        final JSONArray passengerJsonArray = new JSONArray();
        final JSONArray jsonArray = JSONArray.fromObject(grabTicketForm.getPassengerInfo());
        //判断第一个是不是成人,若不是则直接下单失败,等待到期退款
        final JSONObject first = JSONObject.fromObject(jsonArray.get(0));
        final Optional<TbContactInfo> firstOptional = contactInfoService.selectById(first.getLong("id"));
        Preconditions.checkState(firstOptional.isPresent(), "乘客不存在");
        final TbContactInfo firstContactInfo = firstOptional.get();
        if (first.getString("piaoType").equals(EnumTrainTicketType.ADULT.getId())){
            for (Object obj : jsonArray){
                final JSONObject object = JSONObject.fromObject(obj);
                final JSONObject passengerJsonObject = new JSONObject();
                final Optional<TbContactInfo> contactInfoOptional1 = contactInfoService.selectById(object.getLong("id"));
                Preconditions.checkState(contactInfoOptional1.isPresent(), "乘客不存在");
                final TbContactInfo contactInfo = contactInfoOptional1.get();
                if (object.getString("piaoType").equals(EnumTrainTicketType.ADULT.getId())){
                    passengerJsonObject.put("passengersename", firstContactInfo.getName());
                    passengerJsonObject.put("passportseno", firstContactInfo.identyOrg(contactInfo.getIdenty()));
                }else{
                    passengerJsonObject.put("passengersename", firstContactInfo.getName());
                    passengerJsonObject.put("passportseno", contactInfo.identyOrg(contactInfo.getIdenty()));
                }
                passengerJsonObject.put("passengerid", object.getString("id"));
                passengerJsonObject.put("passporttypeseid", contactInfo.getIdentyType());
                passengerJsonObject.put("passporttypeidname", EnumCertificatesType.of(contactInfo.getIdentyType()).getValue());
                passengerJsonObject.put("piaotype", String.valueOf(contactInfo.getPersonType()));
                passengerJsonObject.put("piaotypename", EnumTrainTicketType.of(String.valueOf(contactInfo.getPersonType())).getValue());
                passengerJsonArray.add(passengerJsonObject);
            }
        }else{
            //乘客信息不对,第一个不是成人
            log.info("抢票单["+grabTicketForm.getId()+"]未购买套餐,放入消息队列,到期退款.........");
            grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REQUEST_FAIL.getId());
            this.grabTicketFormService.update(grabTicketForm);
            //放入消息队列 ,
            //TODO 抢票
            JSONObject mqJo = new JSONObject();
            mqJo.put("grabTicketFormId",grabTicketForm.getId());
            MqProducer.sendMessage(mqJo, MqConfig.GRAB_FORM_FAIL_WAIT_REFUND, endTime.getTime() - (new Date()).getTime());
            return;
        }

        jsonObject.put("partnerid",HySdkConstans.GRAB_PARTNER_ID);
        jsonObject.put("method", EnumHTHYMethodCode.QIANG_PIAO_ORDER.getCode());
        jsonObject.put("reqtime", DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
        jsonObject.put("sign", MD5Util.MD5(HySdkConstans.GRAB_PARTNER_ID + EnumHTHYMethodCode.QIANG_PIAO_ORDER.getCode() +
                DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss) + MD5Util.MD5(HySdkConstans.GRAB_SIGN_KEY)));
        jsonObject.put("qorderid", grabTicketForm.getOrderId());
        jsonObject.put("callback_url", "");
        final String trainType = this.getGrabTrainType(grabTicketForm.getTrainCodes());
        jsonObject.put("train_type", trainType);
        jsonObject.put("from_station_code", grabTicketForm.getFromStationCode());
        jsonObject.put("from_station_name",grabTicketForm.getFromStationName());
        jsonObject.put("to_station_code", grabTicketForm.getToStationCode());
        jsonObject.put("to_station_name", grabTicketForm.getToStationName());
        jsonObject.put("start_date", DateFormatUtil.format(parse,DateFormatUtil.yyyyMMdd));
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
        log.info("抢票单[" + grabTicketForm.getId() + "]请求hy抢票下单,请求中.........");
        try{
            final JSONObject jsonResponse = this.hySdkService.grabTickets(jsonObject);
            if(jsonResponse.getBoolean("success") == true && jsonResponse.getInt("code") == 100){
                //抢票下单成功
                grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REQUEST_SUCCESS.getId());
                grabTicketForm.setRemark("抢票下单成功");
                this.grabTicketFormService.update(grabTicketForm);
                log.info("抢票单[" + grabTicketForm.getId() + "]请求hy抢票下单,下单成功.........");
            }else{
                //抢票下单失败,更改抢票单状态, 记录失败原因 , 到期退款
                //放入消息队列 , 1到期退款
                //TODO 抢票GRAB_FORM_PAY_WAITGRAB_FORM_PAY_WAIT
                JSONObject mqJo = new JSONObject();
                mqJo.put("grabTicketFormId",grabTicketForm.getId());
                MqProducer.sendMessage(mqJo, MqConfig.GRAB_FORM_FAIL_WAIT_REFUND, endTime.getTime() - (new Date()).getTime());
                grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REQUEST_FAIL.getId());
                grabTicketForm.setRemark(jsonResponse.getString("msg"));
                this.grabTicketFormService.update(grabTicketForm);
                log.info("抢票单[" + grabTicketForm.getId() + "]请求hy抢票下单,下单失败,放入消息队列,到期全额退款.........");
            }
        }catch (final Throwable throwable){
            JSONObject mqJo = new JSONObject();
            mqJo.put("grabTicketFormId",grabTicketForm.getId());
            MqProducer.sendMessage(mqJo, MqConfig.GRAB_FORM_FAIL_WAIT_REFUND, endTime.getTime() - (new Date()).getTime());
            grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REQUEST_FAIL.getId());
            grabTicketForm.setRemark("请求异常,抢票下单失败");
            this.grabTicketFormService.update(grabTicketForm);
            log.error("抢票单号:"+grabTicketForm.getId()+"请求异常,抢票下单失败,放入消息队列,到期退款.....");
        }

    }

    private String getGrabTrainType(String trainCodes) {

        final String[] split = trainCodes.split(",");
        final StringBuilder builder = new StringBuilder();
        for (String str : split) {
            if (str.startsWith("G")) {
                builder.append("G,");
            } else if (str.startsWith("D")) {
                builder.append("D,");
            } else if (str.startsWith("Z")) {
                builder.append("Z,");
            } else if (str.startsWith("T")) {
                builder.append("T,");
            } else if (str.startsWith("K")) {
                builder.append("K,");
            } else if (str.startsWith("C")) {
                builder.append("C,");
            } else{
                builder.append("Q,");
            }
        }
      return  builder.substring(0, builder.lastIndexOf(","));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormDetailId
     * @param reqToken
     * @param flag
     * @param string
     */
    @Override
    public void handleOrderFormDeatailRefundResult(long orderFormDetailId, String reqToken, boolean flag, String string) {
        final Optional<OrderFormDetail> orderFormDetailOptional = this.orderFormDetailService.selectById(orderFormDetailId);
        Preconditions.checkState(orderFormDetailOptional.isPresent(), "车票订单不存在");
        final RefundTicketFlow refundTicketFlow = this.refundTicketFlowService.getByReqToken(reqToken);
        final ReturnMoneyOrder returnMoneyOrder = this.returnMoneyOrderService.selectByOrderFormDetailId(orderFormDetailId);
        //退款成功, 修改退票流水 , 退款单状态
        if (flag){
            //成功
            log.info("小订单号:[" + orderFormDetailId + "退票款退款成功, 退款流水号:[" +refundTicketFlow.getId()+ "]");
            refundTicketFlow.setStatus(EnumRefundTicketFlowStatus.TICKET_REFUND_SUCCESS.getId());
            this.refundTicketFlowService.update(refundTicketFlow);
            this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_FAIL);
        }else{
            //失败
            log.error("小订单号:[" + orderFormDetailId + "退票款退款失败,人工处理 ,退款流水号:[" +refundTicketFlow.getId()+ "]");
            refundTicketFlow.setStatus(EnumRefundTicketFlowStatus.TICKET_REFUND_FAIL.getId());
            this.refundTicketFlowService.update(refundTicketFlow);
            this.returnMoneyOrderService.updateStatusById(returnMoneyOrder.getId(), EnumReturnMoneyOrderStatus.RETURN_MONEY_FAIL);
        }
    }

    /**
     * {@inheritDoc}
     * @param grabTicketFormId
     * @param flag
     * @param string
     */
    @Override
    public void handleGrabPartRefundResult(long grabTicketFormId, boolean flag, String string) {
        final Optional<RefundOrderFlow> optional = this.refundOrderFlowService.selectByGrabTicketFormId(grabTicketFormId);
        Preconditions.checkState(optional.isPresent(), "订单退款流水不存在");
        final RefundOrderFlow refundOrderFlow = optional.get();
        if (flag){
            //成功
            log.info("抢票单[" + grabTicketFormId +"]差价退款成功,退款流水:["+refundOrderFlow.getId()+"]");
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId());
            this.refundOrderFlowService.update(refundOrderFlow);
        }else{
            //失败
            log.error("抢票单[" + grabTicketFormId +"]差价退款失败,转人工处理,退款流水:["+refundOrderFlow.getId()+"]");
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
            this.refundOrderFlowService.update(refundOrderFlow);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param grabTicketFormId
     * @param flag
     * @param string
     */
    @Override
    public void handleGrabAllRefundResult(long grabTicketFormId, boolean flag, String string) {
        final Optional<RefundOrderFlow> optional = this.refundOrderFlowService.selectByGrabTicketFormId(grabTicketFormId);
        Preconditions.checkState(optional.isPresent(), "订单退款流水不存在");
        final RefundOrderFlow refundOrderFlow = optional.get();
        if (flag){
            //成功
            this.grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_REFUND_SUCCESS,grabTicketFormId);
            log.info("抢票单[" + grabTicketFormId + "]全额退款,退款成功,退款流水:[" +refundOrderFlow.getId() +"]");
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId());
            this.refundOrderFlowService.update(refundOrderFlow);
        }else{
            //失败
            this.grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_REFUND_FAIL,grabTicketFormId);
            log.error("抢票单[" + grabTicketFormId + "]全额退款,退款失败,人工处理,退款流水:[" +refundOrderFlow.getId() +"]");
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
            this.refundOrderFlowService.update(refundOrderFlow);
        }
    }


    /**
     *
     * @param grabTicketFormId
     * @param
     * @param
     */
    @Override
    public void handleCancelGrabRefundResult(long grabTicketFormId, boolean flag, String string) {
        final GrabTicketForm grabTicketForm = this.grabTicketFormService.selectByIdWithLock(grabTicketFormId).get();
        final Optional<RefundOrderFlow> optional = this.refundOrderFlowService.selectByGrabTicketFormId(grabTicketFormId);
        Preconditions.checkState(optional.isPresent(), "订单退款流水不存在");
        final RefundOrderFlow refundOrderFlow = optional.get();
        if (flag){
            //成功
            grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REFUND_SUCCESS.getId());
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId());
            this.refundOrderFlowService.update(refundOrderFlow);
            this.grabTicketFormService.update(grabTicketForm);
        }else{
            //失败
            grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_REFUND_FAIL.getId());
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
            this.refundOrderFlowService.update(refundOrderFlow);
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
        Preconditions.checkState(!refundOrderFlowOptional.isPresent(), "订单[" + orderForm.getId() + "]已经生成退款单");
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
        singleRefundData.setReqSn(SnGenerator.generate());
        singleRefundData.setOrgSn(refundOrderFlow.getPaymentSn());
        singleRefundData.setOrgDate(refundOrderFlow.getOrderDate());
        singleRefundData.setRefundAmount(refundOrderFlow.getRefundAmount().toString());
        singleRefundData.setOrgAmount(refundOrderFlow.getOriginalAmount().toString());
        singleRefundData.setRefundReason(refundOrderFlow.getRefundReason());
        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_REFUND_ING.getId());
        orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_REFUND_ING.getValue());
        this.orderFormService.update(orderForm);
        final Map<String, Object> resultMap = this.authenService.singlRefund(singleRefundData);
        final String retCode = (String) resultMap.get("retCode");
        if ("0000".equals(retCode) || "5000".equals(retCode)) {
            log.info("订单[" + orderForm.getId() + "]--退款单[" + refundOrderFlow.getId() + "]" + "退款申请受理成功！！添加消息回调");
            //消息
            JSONObject mqJo = new JSONObject();
            mqJo.put("orderFormId", orderForm.getId());
            //退款流水号
            mqJo.put("reqSn", singleRefundData.getReqSn());
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