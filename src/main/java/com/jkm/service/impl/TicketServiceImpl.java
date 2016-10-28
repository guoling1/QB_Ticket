package com.jkm.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.controller.helper.request.RequestBookTicket;
import com.jkm.entity.ContactForm;
import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.enums.EnumOrderFormDetailStatus;
import com.jkm.enums.EnumOrderFormStatus;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import com.jkm.service.ys.entity.YsTrainTicketsBookingRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private OrderFormDetailService orderFormDetailService;

    @Autowired
    private OrderFormService orderFormService;

//    private Contrac

    @Autowired
    private YsSdkService ysSdkService;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public void bookTicket(final RequestBookTicket requestBookTicket) {

        final OrderForm orderForm = new OrderForm();
        orderForm.setUid(requestBookTicket.getUid());
        orderForm.setQueryKey(requestBookTicket.getQueryKey());
        //TODO
        orderForm.setTermTransId(requestBookTicket.getTermTransId());
        orderForm.setTrainNo(requestBookTicket.getTrainNo());
        orderForm.setPrice(requestBookTicket.getPrice());
        orderForm.setDepartStation(requestBookTicket.getDepartStation());
        orderForm.setArriveStation(requestBookTicket.getArriveStation());
        orderForm.setDepartStationCode(requestBookTicket.getDepartStationCode());
        orderForm.setArriveStationCode(requestBookTicket.getArriveStationCode());
        orderForm.setDepartDateTime(requestBookTicket.getDepartDateTime());
        orderForm.setArriveDateTime(requestBookTicket.getArriveDateTime());
        orderForm.setContactName(requestBookTicket.getContactName());
        orderForm.setContactMobile(requestBookTicket.getContactMobile());
        orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getId());
        orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_INITIALIZATION.getValue());
        this.orderFormService.add(orderForm);
        final List<RequestBookTicket.passenger> passengers = requestBookTicket.getPassengers();
        Preconditions.checkState(!passengers.isEmpty(), "乘客不可以为空");
        Lists.transform(passengers, new Function<RequestBookTicket.passenger, OrderFormDetail>() {
            @Override
            public OrderFormDetail apply(RequestBookTicket.passenger passenger) {
                //TODO
                final ContactForm contactForm = new ContactForm();

                final OrderFormDetail orderFormDetail = new OrderFormDetail();
                orderFormDetail.setOrderFormId(orderForm.getId());
                orderFormDetail.setPassengerName(contactForm.getUserName());
                orderFormDetail.setPassengerType(contactForm.getUserType());
                orderFormDetail.setCardType(contactForm.getCardType());
                orderFormDetail.setCardNo(contactForm.getCardNo());
                orderFormDetail.setSeatType(passenger.getSeatType());
                orderFormDetail.setTrainNo(orderForm.getTrainNo());
                orderFormDetail.setDepartStation(orderForm.getDepartStation());
                orderFormDetail.setArriveStation(orderForm.getArriveStation());
                orderFormDetail.setDepartStationCode(orderForm.getDepartStationCode());
                orderFormDetail.setArriveStationCode(orderForm.getArriveStationCode());
                orderFormDetail.setDepartDateTime(orderForm.getDepartDateTime());
                orderFormDetail.setArriveDateTime(orderForm.getArriveDateTime());
                orderFormDetail.setStatus(EnumOrderFormDetailStatus.TICKET_INITIALIZATION.getId());
                orderFormDetail.setRemark(EnumOrderFormDetailStatus.TICKET_INITIALIZATION.getValue());
                orderFormDetailService.add(orderFormDetail);
                return orderFormDetail;
            }
        });
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
//                .queryKey(orderForm.getQueryKey())
//                .build();
        Lists.transform(orderFormDetails, new Function<OrderFormDetail, YsTrainTicketsBookingRequest.passenger>() {
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

                return null;
            }
        });



        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Pair<Boolean, String> refund() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param response
     */
    @Override
    public void handleRefundCallbackMsg(YsRefundCallbackResponse response) {

    }
}
