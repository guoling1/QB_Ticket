package com.jkm.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.controller.helper.request.RequestSubmitOrder;
import com.jkm.entity.ContactForm;
import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.entity.RefundTicketFlow;
import com.jkm.enums.EnumHTHYMethodCode;
import com.jkm.entity.TbContactInfo;
import com.jkm.enums.EnumBuyTicketPackageType;
import com.jkm.enums.EnumCertificatesType;
import com.jkm.enums.EnumOrderFormDetailStatus;
import com.jkm.enums.EnumOrderFormStatus;
import com.jkm.enums.EnumRefundTicketFlowStatus;
import com.jkm.service.*;
import com.jkm.service.ContactInfoService;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.service.hy.entity.HyRefundCallbackResponse;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.ys.entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.SnGenerator;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private ContactInfoService contactInfoService;

    @Autowired
    private HySdkService hySdkService;

//    private UserInfoS

    @Autowired
    private RefundTicketFlowService refundTicketFlowService;

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
        orderForm.setUid(requestSubmitOrder.getUid());
        orderForm.setMobile(contactInfoOptional.get().getTel());
        orderForm.setPrice(requestSubmitOrder.getPrice());
        orderForm.setBuyTicketPackageId(requestSubmitOrder.getBuyTicketPackageId());
        orderForm.setBuyTicketPackagePrice(new BigDecimal(EnumBuyTicketPackageType.of(requestSubmitOrder.getBuyTicketPackageId()).getPrice()));
        orderForm.setFromStationName(requestSubmitOrder.getFromStationName());
        orderForm.setFromStationCode(requestSubmitOrder.getFromStationCode());
        orderForm.setToStationName(requestSubmitOrder.getToStationName());
        orderForm.setToStationCode(requestSubmitOrder.getToStationCode());
        orderForm.setZwCode(requestSubmitOrder.getZwCode());
        orderForm.setZwName(requestSubmitOrder.getZwName());
        orderForm.setStartDate(requestSubmitOrder.getStartDate());
        orderForm.setEndDate(requestSubmitOrder.getEndDate());
        orderForm.setStartTime(requestSubmitOrder.getStartTime());
        orderForm.setEndTime(requestSubmitOrder.getEndTime());
        orderForm.setRunTime(requestSubmitOrder.getRunTime());
        orderForm.setCheci(requestSubmitOrder.getCheci());
//        orderForm.setLoginUserName(contactFormOptional.get().getLoginUserName());
//        orderForm.setLoginUserPassword(contactFormOptional.get().getLoginUserPassword());
        orderForm.setOrderId(SnGenerator.generate());
        orderForm.setTrainDate(DateFormatUtil.parse(requestSubmitOrder.getTrainDate(), DateFormatUtil.yyyy_MM_dd));
        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getId());
        orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getValue());
        this.orderFormService.add(orderForm);
        final List<RequestSubmitOrder.Passenger> passengerList = requestSubmitOrder.getPassengers();
        Preconditions.checkState(CollectionUtils.isEmpty(passengerList), "乘客列表为空");
        final JSONArray passengerJsonArray = new JSONArray();
        Lists.transform(passengerList, new Function<RequestSubmitOrder.Passenger, OrderFormDetail>() {
            @Override
            public OrderFormDetail apply(RequestSubmitOrder.Passenger passenger) {
                final Optional<TbContactInfo> contactInfoOptional1 = contactInfoService.selectById(passenger.getId());
                Preconditions.checkState(contactInfoOptional1.isPresent(), "乘客不村在");
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
                passengerJsonObject.put("passporttypeseidname", EnumCertificatesType.of(contactInfo.getIdentyType()));
                passengerJsonObject.put("zwcode", orderForm.getZwCode());
                passengerJsonObject.put("zwname", orderForm.getZwName());
                passengerJsonObject.put("piaotype", orderFormDetail.getPiaoType());
                passengerJsonObject.put("cxin", "");
                passengerJsonArray.add(passengerJsonObject);
                return null;
            }
        });
        log.info("发送订单提交请求！！");
        final JSONObject jsonObject = this.hySdkService.submitOrderImpl(orderForm, passengerJsonArray);
        final String code = jsonObject.getString("code");
        final boolean success = jsonObject.getBoolean("success");
        if (success && ("802".equals(code) || "905".equals(code))) {
            log.info("订单提交受理成功(占座成功)--等待回调！！！");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_REQUESTING.getValue());
            this.orderFormService.update(orderForm);
            return Triple.of(true, jsonObject.getString("msg"), orderForm.getId());
        } else {
            log.error("占座失败");
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
        log.info("订单提交-回调处理中");
        final String orderId = jsonObject.getString("orderId");
        final boolean success = jsonObject.getBoolean("success");
        final boolean orderSuccess = jsonObject.getBoolean("ordersuccess");
        final JSONArray passengers = jsonObject.getJSONArray("passengers");
        Preconditions.checkState(!passengers.isEmpty(), "乘客列表为空了");
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByOrderId(orderId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderId + "]不存在");
        Preconditions.checkState(orderFormOptional.get().isRequestOccupySeatRequesting(), "订单[" + orderId + "]状态不正确");
        final Optional<OrderForm> orderFormOptional1 = this.orderFormService.selectByIdWithLock(orderFormOptional.get().getId());
        final OrderForm orderForm = orderFormOptional1.get();
        if (success && orderSuccess) {
            log.info("订单回调处理成功---占座成功");
            orderForm.setTicketTotalPrice(new BigDecimal(jsonObject.getString("orderamount")));
            orderForm.setTotalPrice(orderForm.getTicketTotalPrice().add(orderForm.getBuyTicketPackagePrice()).add(orderForm.getGrabTicketPackagePrice()));
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
                orderFormDetail.setCxin(passengersJo.getString("cxin"));
                this.orderFormDetailService.update(orderFormDetail);
            }
            //TODO
            //调用支付接口
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
     * @param orderFormId
     * @return
     */
    @Override
    @Transactional
    public Pair<Boolean, String> confirmOrder(final long orderFormId) {
        log.info("订单[" + orderFormId + "]--确认订单请求中");
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByIdWithLock(orderFormId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderFormId + "]不存在");
        final OrderForm orderForm = orderFormOptional.get();
        Preconditions.checkState(orderForm.isCustomerPaySuccess(), "订单[" + orderFormId + "]状态不正确");

        final JSONObject jsonObject = this.hySdkService.confirmTrainTicket(orderForm.getOrderId(), orderForm.getOutOrderId());
        final String code = jsonObject.getString("code");
        final boolean success = jsonObject.getBoolean("success");
        if (success && "100".equals(code)) {
            log.info("订单[" + orderFormId + "]--确认订单请求受理成功");
            orderForm.setOrderNumber(jsonObject.getString("ordernumber"));
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_SUCCESS.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_SUCCESS.getValue());
            this.orderFormService.update(orderForm);
            return Pair.of(true, jsonObject.getString("msg"));
        } else {
            log.info("订单[" + orderFormId + "]--确认订单受理失败");
            orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_FAIL.getId());
            orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUEST_FAIL.getValue());
            this.orderFormService.update(orderForm);
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());
            //TODO
            //退款
            return Pair.of(false, jsonObject.getString("msg"));
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
            this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_ORDER_CANCEL.getValue(),
                    EnumOrderFormDetailStatus.TICKET_ORDER_CANCEL.getId(), orderForm.getId());
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
        request.setPartnerId(HySdkConstans.PARTNERID);
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

}
