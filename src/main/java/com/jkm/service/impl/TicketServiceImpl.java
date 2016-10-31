package com.jkm.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.controller.helper.request.RequestBookTicket;
import com.jkm.entity.ContactForm;
import com.google.common.base.Preconditions;
import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.enums.EnumOrderFormDetailStatus;
import com.jkm.enums.EnumOrderFormStatus;
import com.jkm.enums.EnumPassenger;
import com.jkm.service.ContactFormService;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.*;
import org.apache.commons.collections.CollectionUtils;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.SnGenerator;
import org.apache.commons.lang3.tuple.Pair;
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
    public long bookTicket(final RequestBookTicket requestBookTicket) {

        final OrderForm orderForm = new OrderForm();
        orderForm.setUid(requestBookTicket.getUid());
        orderForm.setQueryKey(requestBookTicket.getQueryKey());
        orderForm.setTermTransId(SnGenerator.generate());
        orderForm.setTrainNo(requestBookTicket.getTrainNo());
        orderForm.setPrice(requestBookTicket.getPrice());
        orderForm.setDepartStation(requestBookTicket.getDepartStation());
        orderForm.setArriveStation(requestBookTicket.getArriveStation());
        orderForm.setDepartStationCode(requestBookTicket.getDepartStationCode());
        orderForm.setArriveStationCode(requestBookTicket.getArriveStationCode());
        orderForm.setDepartDate(DateFormatUtil.parse(requestBookTicket.getDepartDate(), DateFormatUtil.yyyy_MM_dd));
        orderForm.setDepartTime(requestBookTicket.getDepartTime());
        orderForm.setArriveDate(DateFormatUtil.parse(requestBookTicket.getArriveDate(), DateFormatUtil.yyyy_MM_dd));
        orderForm.setArriveTime(requestBookTicket.getArriveTime());
        orderForm.setContactName(requestBookTicket.getContactName());
        orderForm.setContactMobile(requestBookTicket.getContactMobile());
        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getId());
        orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getValue());
        this.orderFormService.add(orderForm);
        final BigDecimal totalPrice = new BigDecimal("0.00");
        final List<RequestBookTicket.passenger> passengers = requestBookTicket.getPassengers();
        Preconditions.checkState(!passengers.isEmpty(), "乘客不可以为空");
        Lists.transform(passengers, new Function<RequestBookTicket.passenger, OrderFormDetail>() {
            @Override
            public OrderFormDetail apply(RequestBookTicket.passenger passenger) {
                final Optional<ContactForm> contactFormOptional = contactFormService.selectById(passenger.getContractFormId());
                final ContactForm contactForm = contactFormOptional.get();
                final OrderFormDetail orderFormDetail = new OrderFormDetail();
                if (EnumPassenger.CHILDREN.getId() == contactForm.getUserType()) {
                    totalPrice.add(orderForm.getPrice().divide(new BigDecimal("2"), 2, BigDecimal.ROUND_HALF_UP));
                } else if (EnumPassenger.ADULT.getId() == contactForm.getUserType()) {
                    totalPrice.add(orderForm.getPrice());
                }
                orderFormDetail.setOrderFormId(orderForm.getId());
                orderFormDetail.setPassengerName(contactForm.getUserName());
                orderFormDetail.setPassengerType(contactForm.getUserType());
                orderFormDetail.setCardType(contactForm.getCardType());
                orderFormDetail.setCardNo(contactForm.getCardNo());
                orderFormDetail.setBirth(contactForm.getBirth());
                orderFormDetail.setSeatType(passenger.getSeatType());
                orderFormDetail.setTrainNo(orderForm.getTrainNo());
                orderFormDetail.setDepartStation(orderForm.getDepartStation());
                orderFormDetail.setArriveStation(orderForm.getArriveStation());
                orderFormDetail.setDepartStationCode(orderForm.getDepartStationCode());
                orderFormDetail.setArriveStationCode(orderForm.getArriveStationCode());
                orderFormDetail.setStatus(EnumOrderFormDetailStatus.TICKET_INITIALIZATION.getId());
                orderFormDetail.setRemark(EnumOrderFormDetailStatus.TICKET_INITIALIZATION.getValue());
                orderFormDetailService.add(orderFormDetail);
                return orderFormDetail;
            }
        });
        orderForm.setTotalPrice(totalPrice);
        this.orderFormService.update(orderForm);
        return orderForm.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     * @return
     */
    @Override
    @Transactional
    public Pair<Boolean, String> requestBookTicket(final long orderFormId) {
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByIdWithLock(orderFormId);
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderFormId + "]不存在");
        final OrderForm orderForm = orderFormOptional.get();
        Preconditions.checkState(orderForm.isCustomerPaySuccess(), "订单[" + orderFormId + "]状态 不是客户已经付款完成的状态");
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByOrderFormId(orderFormId);
        Preconditions.checkState(!CollectionUtils.isEmpty(orderFormDetails), "该订单[" + orderFormId + "]下对应的车票记录不存在");
        final YsTrainTicketsBookingRequest ysTrainTicketsBookingRequest = new YsTrainTicketsBookingRequest();
        final List<YsTrainTicketsBookingRequest.passenger> passengerList = Lists.transform(orderFormDetails, new Function<OrderFormDetail, YsTrainTicketsBookingRequest.passenger>() {
            @Override
            public YsTrainTicketsBookingRequest.passenger apply(OrderFormDetail orderFormDetail) {
                Preconditions.checkState(orderFormDetail.isTicketInit(),
                        "订单[" + orderFormId + "] 对应的单个乘客车票订单[" + orderFormDetail.getId() + "]状态不正确");
                final YsTrainTicketsBookingRequest.passenger passenger = ysTrainTicketsBookingRequest.new passenger();
                passenger.setType(orderFormDetail.getPassengerType());
                //TODO
                passenger.setName(orderFormDetail.getPassengerName());
                passenger.setCardType(orderFormDetail.getCardType());
                passenger.setCardNo(orderFormDetail.getCardNo());
                passenger.setBirth(orderFormDetail.getBirth());
                passenger.setSeatType(orderFormDetail.getSeatType());
                passenger.setTicketPrice(String.valueOf(orderForm.getPrice().doubleValue()));
                return passenger;
            }
        });
        ysTrainTicketsBookingRequest.setQueryKey(orderForm.getQueryKey());
        ysTrainTicketsBookingRequest.setTermTransID(orderForm.getTermTransId());
        ysTrainTicketsBookingRequest.setTrainNo(orderForm.getTrainNo());
        ysTrainTicketsBookingRequest.setDepartStationCode(orderForm.getDepartStationCode());
        ysTrainTicketsBookingRequest.setArriveStationCode(orderForm.getArriveStationCode());
        ysTrainTicketsBookingRequest.setDepartDate(DateFormatUtil.format(orderForm.getDepartDate(), "yyyy-MM-dd"));
        ysTrainTicketsBookingRequest.setDepartTime(orderForm.getDepartTime());
        ysTrainTicketsBookingRequest.setContactName(orderForm.getContactName());
        ysTrainTicketsBookingRequest.setContactMobile(orderForm.getContactMobile());
        ysTrainTicketsBookingRequest.setPassengers(passengerList);
        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUESTING.getId());
        orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CONFIRM_TICKET_REQUESTING.getValue());
        //订单设置为请求中
//        this.orderFormService.update(orderForm);
//        final YsTrainTicketsBookingResponse response = this.ysSdkService.bookingTicket(ysTrainTicketsBookingRequest);
//        final String status = response.getStatus();
//        switch (status) {
//            case "1004":
//                orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_REQUEST_SUCCESS.getId());
//                orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_REQUEST_SUCCESS.getValue());
//                this.orderFormService.update(orderForm);
//                return Pair.of(true, "订单请求成功");
//            default:
//                orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_FAIL.getId());
//                orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_FAIL.getValue());
//                this.orderFormService.update(orderForm);
//                this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
//                        EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderFormId);
//                //TODO
//
//                //退款
//                return Pair.of(false, "订单请求失败, 购票失败");
//        }
        return null;
    }

    /**
     * 处理订票回调
     *
     * @param response
     * @return
     */
    @Override
    public Pair<Boolean, String> handleBookTicketCallbackResponse(final YsTrainTicketBookingCallbackResponse response) {

        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByTermTransId(response.getTermTransID());
        Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + response.getTermTransID() + "]不存在");
        final OrderForm orderForm = orderFormOptional.get();
        Preconditions.checkState(orderForm.isRequestSuccess(), "订单[" + response.getTermTransID() + "]状态不正确");
        final Optional<OrderForm> orderFormOptional1 = this.orderFormService.selectByIdWithLock(orderForm.getId());
        final OrderForm orderForm1 = orderFormOptional1.get();
        //TODO
        //根据什么来判断，订票成功

        //成功
        orderForm1.setTransId(response.getTransID());
        orderForm1.setPickNo(response.getPickNo());
        orderForm1.setInsurePrice(response.getInsuranceFee());
        orderForm1.setTechnicalFee(response.getTechnicalFee());
        orderForm1.setStatus(EnumOrderFormStatus.ORDER_FORM_TICKET_SUCCESS.getId());
        orderForm1.setRemark(EnumOrderFormStatus.ORDER_FORM_TICKET_SUCCESS.getValue());
        this.orderFormService.update(orderForm1);
        final List<YsTrainTicketBookingCallbackResponse.passenger> passengers = response.getPassengers();
        Preconditions.checkState(CollectionUtils.isEmpty(passengers), "乘客列表为空");
        Lists.transform(passengers, new Function<YsTrainTicketBookingCallbackResponse.passenger, Object>() {
            @Override
            public Object apply(YsTrainTicketBookingCallbackResponse.passenger passenger) {
                final Optional<OrderFormDetail> orderFormDetailOptional =
                        orderFormDetailService.selectByCardNoAndPassengerType(passenger.getCardNo(), passenger.getType());

                Preconditions.checkState(orderFormDetailOptional.isPresent(), "乘客[" + passenger.getCardNo() + "]不存在");
                final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
                Preconditions.checkState(orderFormDetail.isTicketInit(),
                        "订单[" + orderForm.getId() + "] 对应的单个乘客车票订单[" + orderFormDetail.getId() + "]状态不正确");
                orderFormDetail.setPassengerId(passenger.getId());
                orderFormDetail.setSeatName(passenger.getSeatName());
                orderFormDetail.setPrice(passenger.getPrice());
                orderFormDetail.setPickNo(response.getPickNo());
                orderFormDetail.setDepartDateTime(response.getDepartDateTime());
                orderFormDetail.setArriveDateTime(response.getArriveDateTime());
                orderFormDetail.setStatus(EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getId());
                orderFormDetail.setRemark(EnumOrderFormDetailStatus.TICKET_BUY_SUCCESS.getValue());
                orderFormDetailService.update(orderFormDetail);
                return null;
            }
        });

        //失败 -- 退款
        orderForm1.setStatus(EnumOrderFormStatus.ORDER_FORM_TICKET_FAIL.getId());
        orderForm1.setRemark(EnumOrderFormStatus.ORDER_FORM_TICKET_FAIL.getValue());
        this.orderFormService.update(orderForm1);

        this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getValue(),
                EnumOrderFormDetailStatus.TICKET_BUY_FAIL.getId(), orderForm.getId());

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
        final YsRefundTicketRequest request = YsRefundTicketRequest.builder().termTransID(SnGenerator.generate()).transID(orderForm.getTransId())
                .passengerID(orderFormDetail.getPassengerId()).build();
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
}
