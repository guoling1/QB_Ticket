package com.jkm.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.controller.helper.request.RequestSubmitOrder;
import com.jkm.entity.*;
import com.jkm.entity.fusion.SingleRefundData;
import com.jkm.enums.*;
import com.jkm.helper.InsurancePolicyUtil;
import com.jkm.service.*;
import com.jkm.service.ContactInfoService;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.service.hy.HySdkService;
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
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Service
public class TicketServiceImpl implements TicketService {

    private static Logger log = Logger.getLogger(TicketServiceImpl.class);

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
    private AuthenService authenService;

    @Autowired
    private RefundOrderFlowService refundOrderFlowService;

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
        final Optional<TbContactInfo> contactInfoOptional = this.contactInfoService.selectByUid(requestSubmitOrder.getUid());
        Preconditions.checkState(contactInfoOptional.isPresent(), "订票人uid[" + requestSubmitOrder.getUid() + "]不存在");
        final UserInfo userInfo = this.userInfoService.selectByUid(requestSubmitOrder.getUid());
        final List<RequestSubmitOrder.Passenger> passengerList = requestSubmitOrder.getPassengers();
        Preconditions.checkState(!CollectionUtils.isEmpty(passengerList), "乘客列表为空");
        orderForm.setUid(requestSubmitOrder.getUid());
        orderForm.setMobile(contactInfoOptional.get().getTel());
        orderForm.setPrice(requestSubmitOrder.getPrice());
        orderForm.setBuyTicketPackageId(requestSubmitOrder.getBuyTicketPackageId());
        System.out.println(EnumBuyTicketPackageType.of(requestSubmitOrder.getBuyTicketPackageId()).getPrice());
        orderForm.setBuyTicketPackagePrice(new BigDecimal(EnumBuyTicketPackageType.of(requestSubmitOrder.getBuyTicketPackageId()).getPrice())
                .multiply(new BigDecimal(String.valueOf(passengerList.size()))));
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
            final Optional<TbContactInfo> contactInfoOptional1 = contactInfoService.selectById(passenger.getId());
            Preconditions.checkState(contactInfoOptional1.isPresent(), "乘客不存在");
            final TbContactInfo contactInfo = contactInfoOptional1.get();
            final OrderFormDetail orderFormDetail = new OrderFormDetail();
            final JSONObject passengerJsonObject = new JSONObject();
            orderFormDetail.setOrderFormId(orderForm.getId());
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
            orderFormDetailService.add(orderFormDetail);
            passengerJsonObject.put("passengersename", contactInfo.getName());
            passengerJsonObject.put("passportseno", contactInfo.getIdenty());
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
            log.info("订单提交受理成功(占座成功)--等待回调！！！");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getValue());
            this.orderFormService.update(orderForm);
            return Triple.of(true, jsonObject.getString("msg"), orderForm.getId());
        } else {
            log.info("占座失败!request:[" + jsonObject.toString() + "]");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
            return Triple.of(false, jsonObject.getString("msg"), 0L);
        }
    }


    /**
     * 处理订票回调
     *
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional
    public void handleSubmitOrderCallbackResponse(final JSONObject jsonObject) {
        final String orderId = jsonObject.getString("orderId");
        log.info("订单[" + orderId + "]提交-回调处理中");
        final boolean success = jsonObject.getBoolean("success");
        final boolean orderSuccess = jsonObject.getBoolean("ordersuccess");
        final String orderAmount = jsonObject.getString("orderamount");
        final JSONArray passengers = jsonObject.getJSONArray("passengers");
        Preconditions.checkState(!passengers.isEmpty(), "乘客列表为空了");
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByOrderId(orderId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderId + "]不存在");
        if (orderFormOptional.get().isOccupySuccessOrFail()) {//处理可能的多次回调
            return;
        }
        Preconditions.checkState(orderFormOptional.get().isRequestOccupySeatRequesting(), "订单[" + orderId + "]状态不正确");
        final Optional<OrderForm> orderFormOptional1 = this.orderFormService.selectByIdWithLock(orderFormOptional.get().getId());
        final OrderForm orderForm = orderFormOptional1.get();
        orderForm.setTicketTotalPrice(new BigDecimal(orderAmount));
        if (success && orderSuccess) {
            log.info("订单回调处理成功---占座成功");
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
                        this.orderFormDetailService.selectByOrderFormIdAndPassengerIdAndPiaoType(orderId, passengerId, piaoType);
                Preconditions.checkState(orderFormDetailOptional.isPresent(), "乘客的票的记录不存在");
                final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
                Preconditions.checkState(orderFormDetail.isTicketInit(), "乘客的票的记录[" + orderFormDetail.getId() + "]状态不正确");
                orderFormDetail.setTicketNo(passengersJo.getString("ticket_no"));
                orderFormDetail.setCxin(passengersJo.getString("cxin"));
                this.orderFormDetailService.update(orderFormDetail);
            }

            //########################## 处理保险策略 ################################
            if (InsurancePolicyUtil.isOpenPolicy && !orderForm.isBuyInsuance()) {
                this.handleInsurancePolicy(orderForm);
                return;
            }
            //###########################################################
            //创建商户收款记录
            final ChargeMoneyOrder chargeMoneyOrder = new ChargeMoneyOrder();
            chargeMoneyOrder.setOrderFormId(orderForm.getId());
            chargeMoneyOrder.setTotalAmount(orderForm.getTotalPrice());
            chargeMoneyOrder.setBuyTicketPackage(orderForm.getBuyTicketPackagePrice());
            chargeMoneyOrder.setGrabTicketPackage(orderForm.getGrabTicketPackagePrice());
            chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.INIT.getId());
            chargeMoneyOrderService.init(chargeMoneyOrder);

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
        if (success) {//  && "100".equals(code)
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
            this.orderRefund(refundOrderFlow);
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
        final String orderId = jsonObject.getString("orderId");
        final String isSuccess = jsonObject.getString("isSuccess");
        final String code = jsonObject.getString("code");
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByOrderId(orderId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderId + "]不存在");
        if (orderFormOptional.get().isBuySuccessOrFail()) {//处理多次回调
            return;
        }
        Preconditions.checkState(orderFormOptional.get().confirmTicketRequestSuccess(), "订单[" + orderId + "]状态不正确");
        final Optional<OrderForm> orderFormOptional1 = this.orderFormService.selectByIdWithLock(orderFormOptional.get().getId());
        final OrderForm orderForm = orderFormOptional1.get();
        if ("Y".equals(isSuccess) && "100".equals(code)) {
            log.info("确认订单回调函数--出票成功");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_TICKET_SUCCESS.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_TICKET_SUCCESS.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getId(), orderForm.getId());
        } else {
            log.info("确认订单回调函数--出票失败");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_TICKET_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_TICKET_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
            //退款
            final RefundOrderFlow refundOrderFlow = this.initRefundOrderFlow(orderForm);
            this.orderRefund(refundOrderFlow);
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
        final String code = jsonObject.getString("code");
        final boolean success = jsonObject.getBoolean("success");
        if (success && "100".equals(code)) {
            log.info("订单[" + orderFormId + "]取消成功！！");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CANCEL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CANCEL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_ORDER_CANCEL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_ORDER_CANCEL.getId(), orderForm.getId());
            //如果客户支付成功，退款
            if (orderForm.isCustomerPaySuccess()) {
                final RefundOrderFlow refundOrderFlow = this.initRefundOrderFlow(orderForm);
                this.orderRefund(refundOrderFlow);
            }
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
    public Pair<Boolean, String> refund(final long orderFormDetailId) {
        final Optional<OrderFormDetail> orderFormDetailOptional = this.orderFormDetailService.selectById(orderFormDetailId);
        Preconditions.checkArgument(orderFormDetailOptional.isPresent() , "订单不存在");
        final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectById(orderFormDetail.getOrderFormId());
        Preconditions.checkArgument(orderFormOptional.isPresent() , "请求订单不存在");
        final OrderForm orderForm = orderFormOptional.get();
        // 申请时间距离发车时间不到2小时或已经超过发出时间时，不可退票
        final Date applyTime = new Date();

        //可退票, 创建退票流水订单
        this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUESTING);
        final RefundTicketFlow flow = new RefundTicketFlow();
        flow.setOrderFormId(orderForm.getId());
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
            final HyReturnTicketResponse response = this.hySdkService.returnTicket(request, jsonArray);
            if(response.getSuccess().equals("true")){
                this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_REQUEST_SUCCESS);
                return Pair.of(true , "退票请求已接收");
            }else{
                this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                return Pair.of(false, "退票请求失败");
            }
        }catch (Throwable e){
            this.orderFormDetailService.updateStatusById(orderFormDetail.getId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
                return Pair.of(false, e.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param jsonObject
     */
    @Override
    public void handleRefundCallbackMsg(final JSONObject jsonObject) {
        //退款成功, 修改小订单状态 , 退款给客户
        final JSONArray jsonArray = jsonObject.getJSONArray("returntickets");
        if (jsonObject.getInt("returntype") == 1){
            //线上退款

            if(jsonObject.getBoolean("returnstate")){
                //退款成功
               JSONObject obj  = (JSONObject)jsonArray.get(0);
                final RefundTicketFlow flow = this.refundTicketFlowService.getByTicketNo(obj.getString("ticket_no"));
                flow.setReturnmoney(obj.getString("returnmoney"));
                flow.setSuccessTime(obj.getString("returntime"));
                flow.setRemark("线上退款成功");
                this.refundTicketFlowService.update(flow);
                this.orderFormDetailService.updateStatusById(flow.getOrderFormDetailId() , EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                // 给用户退款
            }else{
                //退款失败
                JSONObject obj  = (JSONObject)jsonArray.get(0);
                final RefundTicketFlow flow = this.refundTicketFlowService.getByTicketNo(obj.getString("ticket_no"));
                flow.setRemark("线上退款" + obj.getString("returnfailmsg"));
                this.refundTicketFlowService.update(flow);
                this.orderFormDetailService.updateStatusById(flow.getOrderFormDetailId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
            }

        }else{
            //线下退款,或改签退款
            if(jsonObject.getBoolean("returnsuccess")){
                //退款成功
                //退款成功
                JSONObject obj  = (JSONObject)jsonArray.get(0);
                final RefundTicketFlow flow = this.refundTicketFlowService.getByTicketNo(obj.getString("ticket_no"));
                flow.setReturnmoney(obj.getString("returnmoney"));
                flow.setSuccessTime(obj.getString("returntime"));
                flow.setRemark("线下退款成功");
                this.refundTicketFlowService.update(flow);
                this.orderFormDetailService.updateStatusById(flow.getOrderFormDetailId() , EnumOrderFormDetailStatus.TICKET_RETURN_SUCCESS);
                //给用户退款

            }else{
                //退款失败
                //退款失败
                JSONObject obj  = (JSONObject)jsonArray.get(0);
                final RefundTicketFlow flow = this.refundTicketFlowService.getByTicketNo(obj.getString("ticket_no"));
                flow.setRemark("线下退款" + obj.getString("returnfailmsg"));
                this.refundTicketFlowService.update(flow);
                this.orderFormDetailService.updateStatusById(flow.getOrderFormDetailId() , EnumOrderFormDetailStatus.TICKET_RETURN_FAIL);
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
        Preconditions.checkState(orderForm.isOccupySuccess(), "处理客户付款，订单[%s]的状态不是占座成功状态！！！");
        final Optional<ChargeMoneyOrder> chargeMoneyOrderOptional = this.chargeMoneyOrderService.selectByOrderFormId(orderFormId);
        Preconditions.checkState(chargeMoneyOrderOptional.isPresent(), "订单[%s]对应的收款记录不存在", orderFormId);
        final ChargeMoneyOrder chargeMoneyOrder = this.chargeMoneyOrderService.selectByIdWithLock(chargeMoneyOrderOptional.get().getId()).get();
        Preconditions.checkState(chargeMoneyOrder.isPaySuccess(), "订单[%s]对应的收款记录已经付款成功！！！！！！！");
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
            chargeMoneyOrder.setStatus(EnumChargeMoneyOrderStatus.PAYMENT_TICKET_FAIL.getId());
        }
        this.chargeMoneyOrderService.update(chargeMoneyOrder);
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
    private void orderRefund(final RefundOrderFlow refundOrderFlow) {
        Preconditions.checkState(refundOrderFlow.isRefundSuccess(), "订单[" + refundOrderFlow.getOrderFormId()  +
                "]对应的退款单[" + refundOrderFlow.getId()+ "]已经退款");
        final SingleRefundData singleRefundData = new SingleRefundData();
        singleRefundData.setOrgSn(refundOrderFlow.getPaymentSn());
        singleRefundData.setOrdDate(refundOrderFlow.getOrderDate());
        singleRefundData.setRefundAmount(refundOrderFlow.getRefundAmount().toString());
        singleRefundData.setOrgAmount(refundOrderFlow.getOriginalAmount().toString());
        singleRefundData.setRefundReason(refundOrderFlow.getRefundReason());
        final Map<String, Object> resultMap = this.authenService.singlRefund(singleRefundData);
        if ((boolean) resultMap.get("retCode")) {
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId());
        } else {
            refundOrderFlow.setStatus(EnumRefundOrderFlowStatus.REFUND_FAIL.getId());
        }
        this.refundOrderFlowService.update(refundOrderFlow);
    }
}
