package com.jkm.controller.api;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestQueryGrabOrder;
import com.jkm.controller.helper.request.RequestQueryOrderForm;
import com.jkm.controller.helper.response.ResponseQueryGrabOrder;
import com.jkm.controller.helper.response.ResponseQueryOrderForm;
import com.jkm.entity.GrabTicketForm;
import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.enums.EnumTrainTicketType;
import com.jkm.service.GrabTicketFormService;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by yulong.zhang on 2016/10/31.
 */
@Controller
@RequestMapping(value = "/order")
public class OrderFormController extends BaseController {
    private static Logger log = Logger.getLogger(OrderFormController.class);

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private OrderFormDetailService orderFormDetailService;

    @Autowired
    private GrabTicketFormService grabTicketFormService;

    /**
     * 查询我的订单
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryMyOrder", method = RequestMethod.POST)
    public ResponseEntityBase<Object[]> queryMyOrderForm(@RequestBody final RequestQueryOrderForm request) {
        final String uid = super.getUid(request.getAppid(), request.getUid());
        request.setUid(uid);
        final ResponseEntityBase<Object[]> results = new ResponseEntityBase<>();
        final List<OrderForm> orderForms = this.orderFormService.selectByUid(request.getUid());
        if (CollectionUtils.isEmpty(orderForms)) {
            results.setData(new Object[0]);
            return results;
        }
        final List<Long> orderFormIds = Lists.transform(orderForms, new Function<OrderForm, Long>() {
            @Override
            public Long apply(OrderForm orderForm) {
                return orderForm.getId();
            }
        });
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByOrderFormIds(orderFormIds);
        final List<ResponseQueryOrderForm> orderFormList = Lists.transform(orderForms, new Function<OrderForm, ResponseQueryOrderForm>() {
            @Override
            public ResponseQueryOrderForm apply(OrderForm orderForm) {
                return getResponse(orderForm, orderFormDetails);
            }
        });
        results.setData(orderFormList.toArray());
        return results;
    }


    /**
     * 按id查询订单
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryById", method = RequestMethod.POST)
    public ResponseEntityBase<ResponseQueryOrderForm> queryById(@RequestBody final RequestQueryOrderForm request) {
        final ResponseEntityBase<ResponseQueryOrderForm> results = new ResponseEntityBase<>();
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectById(request.getOrderFormId());
        final OrderForm orderForm = orderFormOptional.get();
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByOrderFormId(request.getOrderFormId());
        results.setData(this.getResponse(orderForm, orderFormDetails));
        return results;
    }

    /**
     * 查询我的抢票订单(抢票中, 已抢到)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryGrabOrder", method = RequestMethod.POST)
    public ResponseEntityBase<Object[]> queryGrabOrderForm(@RequestBody final RequestQueryGrabOrder request) {
        final String uid = super.getUid(request.getAppid(), request.getUid());
        request.setUid(uid);
        final ResponseEntityBase<Object[]> results = new ResponseEntityBase<>();
        final List<GrabTicketForm> grabTicketForms = this.grabTicketFormService.selectByUid(request.getUid());
        if (CollectionUtils.isEmpty(grabTicketForms)) {
            results.setData(new Object[0]);
            return results;
        }
        final List<Long> grabTicketFormIds = Lists.transform(grabTicketForms, new Function<GrabTicketForm, Long>() {
            @Override
            public Long apply(GrabTicketForm grabTicketForm) {
                return grabTicketForm.getId();
            }
        });
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByGrabTicketFormIds(grabTicketFormIds);
        final List<ResponseQueryGrabOrder> orderFormList = Lists.transform(grabTicketForms, new Function<GrabTicketForm, ResponseQueryGrabOrder>() {
            @Override
            public ResponseQueryGrabOrder apply(GrabTicketForm grabTicketForm) {
                //判断是否抢到票
                if(!(grabTicketForm.getCheci() == null)){
                    //抢到
                    return getGrabResponse(grabTicketForm, orderFormDetails);
                }else{
                    //未抢到
                    final ResponseQueryGrabOrder responseQueryGrabOrder = new ResponseQueryGrabOrder();
                    responseQueryGrabOrder.setUid(grabTicketForm.getUid());
                    responseQueryGrabOrder.setGrabTicketFormId(grabTicketForm.getId());
                    responseQueryGrabOrder.setIsGrab(0);
                    responseQueryGrabOrder.setGrabTotalPrice(grabTicketForm.getGrabTotalPrice());
                    responseQueryGrabOrder.setGrabStartTime(grabTicketForm.getFirstStartTime().substring(0,10));
                    responseQueryGrabOrder.setGrabTimeType(grabTicketForm.getGrabTimeType());
                    responseQueryGrabOrder.setFromStationName(grabTicketForm.getFromStationName());
                    responseQueryGrabOrder.setToStationName(grabTicketForm.getToStationName());
                    responseQueryGrabOrder.setTrainCodes(grabTicketForm.getTrainCodes());
                    responseQueryGrabOrder.setSeatTypes(grabTicketForm.getSeatTypes());
                    responseQueryGrabOrder.setPassengerInfo(grabTicketForm.getPassengerInfo());
                    responseQueryGrabOrder.setExpireTime(grabTicketForm.getExpireTime());
                    responseQueryGrabOrder.setStatus(grabTicketForm.getStatus());
                    return responseQueryGrabOrder;
                }
            }
        });
        results.setData(orderFormList.toArray());
        return results;
    }

    /**
     * 按id查询抢票订单
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/grab/queryById", method = RequestMethod.POST)
    public ResponseEntityBase<ResponseQueryGrabOrder> grabQueryById(@RequestBody final RequestQueryGrabOrder request) {
        final ResponseEntityBase<ResponseQueryGrabOrder> results = new ResponseEntityBase<>();
        final Optional<GrabTicketForm> grabTicketFormOptional = this.grabTicketFormService.selectById(request.getGrabTicketFormId());
        final GrabTicketForm grabTicketForm = grabTicketFormOptional.get();
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByGrabTicketFormId(request.getGrabTicketFormId());
        //判断是否抢到票
        if(!(grabTicketForm.getCheci() == null)){
            //抢到
            results.setData(getGrabResponse(grabTicketForm, orderFormDetails));
            return results;
        }else{
            //未抢到
            final ResponseQueryGrabOrder responseQueryGrabOrder = new ResponseQueryGrabOrder();
            responseQueryGrabOrder.setUid(grabTicketForm.getUid());
            responseQueryGrabOrder.setGrabTicketFormId(grabTicketForm.getId());
            responseQueryGrabOrder.setIsGrab(0);
            responseQueryGrabOrder.setGrabTotalPrice(grabTicketForm.getGrabTotalPrice());
            responseQueryGrabOrder.setGrabStartTime(grabTicketForm.getFirstStartTime().substring(0,10));
            responseQueryGrabOrder.setGrabTimeType(grabTicketForm.getGrabTimeType());
            responseQueryGrabOrder.setFromStationName(grabTicketForm.getFromStationName());
            responseQueryGrabOrder.setToStationName(grabTicketForm.getToStationName());
            responseQueryGrabOrder.setTrainCodes(grabTicketForm.getTrainCodes());
            responseQueryGrabOrder.setSeatTypes(grabTicketForm.getSeatTypes());
            responseQueryGrabOrder.setPassengerInfo(grabTicketForm.getPassengerInfo());
            responseQueryGrabOrder.setExpireTime(grabTicketForm.getExpireTime());
            responseQueryGrabOrder.setPassengerInfo(grabTicketForm.getPassengerInfo());
            responseQueryGrabOrder.setStatus(grabTicketForm.getStatus());
            results.setData(responseQueryGrabOrder);
            return results;
        }

    }

    private ResponseQueryGrabOrder getGrabResponse(GrabTicketForm grabTicketForm, List<OrderFormDetail> orderFormDetails) {
        final ResponseQueryGrabOrder responseQueryGrabOrder = new ResponseQueryGrabOrder();
        final ArrayList<ResponseQueryGrabOrder.passenger> passengers = new ArrayList<>();
        responseQueryGrabOrder.setPassengers(passengers);
        responseQueryGrabOrder.setUid(grabTicketForm.getUid());
        responseQueryGrabOrder.setGrabTicketFormId(grabTicketForm.getId());
        responseQueryGrabOrder.setIsGrab(1);
        responseQueryGrabOrder.setPrice(grabTicketForm.getPrice());
        responseQueryGrabOrder.setTicketTotalPrice(grabTicketForm.getTicketTotalPrice());
        responseQueryGrabOrder.setTotalPrice(grabTicketForm.getTotalPrice());
        responseQueryGrabOrder.setFromStationName(grabTicketForm.getFromStationName());
        responseQueryGrabOrder.setFromStationCode(grabTicketForm.getFromStationCode());
        responseQueryGrabOrder.setToStationCode(grabTicketForm.getToStationCode());
        responseQueryGrabOrder.setToStationName(grabTicketForm.getToStationName());
        responseQueryGrabOrder.setCheci(grabTicketForm.getCheci());
        responseQueryGrabOrder.setStartDate(grabTicketForm.getStartDate());
        responseQueryGrabOrder.setStartTime(grabTicketForm.getStartTime());
        responseQueryGrabOrder.setEndDate(grabTicketForm.getEndDate());
        responseQueryGrabOrder.setEndTime(grabTicketForm.getEndTime());
        responseQueryGrabOrder.setRunTime(grabTicketForm.getRunTime());
        responseQueryGrabOrder.setExpireTime(grabTicketForm.getExpireTime());
        responseQueryGrabOrder.setStatus(grabTicketForm.getStatus());
        final Iterator<OrderFormDetail> iterator = orderFormDetails.iterator();
        while (iterator.hasNext()) {
            final OrderFormDetail next = iterator.next();
            if (grabTicketForm.getId() == next.getGrabTicketFormId()) {
                final ResponseQueryGrabOrder.passenger passenger = responseQueryGrabOrder.new passenger();
                passenger.setOrderFormDetailId(next.getId());
                passenger.setName(next.getPassengerName());
                passenger.setPassportSeNo(next.getPassportSeNoPlain().substring(0, 3) + "***********" + next.getPassportSeNoPlain().substring(14));
                passenger.setPiaoType(next.getPiaoType());
                passenger.setPiaoTypeName(EnumTrainTicketType.of(next.getPiaoType()).getValue());
                passenger.setCxin(next.getCxin());
                passenger.setPrice(next.getPrice());
                passenger.setStatus(next.getStatus());
                passengers.add(passenger);
                iterator.remove();
            }
        }
        return responseQueryGrabOrder;
    }

    private ResponseQueryOrderForm getResponse(final OrderForm orderForm, final List<OrderFormDetail> orderFormDetails) {
        final ResponseQueryOrderForm responseQueryOrderForm = new ResponseQueryOrderForm();
        final ArrayList<ResponseQueryOrderForm.passenger> passengers = new ArrayList<>();
        responseQueryOrderForm.setPassengers(passengers);
        responseQueryOrderForm.setOrderFormId(orderForm.getId());
        responseQueryOrderForm.setUid(orderForm.getUid());
        responseQueryOrderForm.setPrice(orderForm.getPrice());
        responseQueryOrderForm.setTotalPrice(orderForm.getTotalPrice());
        responseQueryOrderForm.setTicketTotalPrice(orderForm.getTicketTotalPrice());
        responseQueryOrderForm.setFromStationName(orderForm.getFromStationName());
        responseQueryOrderForm.setFromStationCode(orderForm.getFromStationCode());
        responseQueryOrderForm.setToStationName(orderForm.getToStationName());
        responseQueryOrderForm.setToStationCode(orderForm.getToStationCode());
        responseQueryOrderForm.setCheci(orderForm.getCheci());
        responseQueryOrderForm.setZwCode(orderForm.getZwCode());
        responseQueryOrderForm.setZwName(orderForm.getZwName());
        responseQueryOrderForm.setStartDate(orderForm.getStartDate());
        responseQueryOrderForm.setEndDate(orderForm.getEndDate());
        responseQueryOrderForm.setStartTime(orderForm.getStartTime());
        responseQueryOrderForm.setEndTime(orderForm.getEndTime());
        responseQueryOrderForm.setRunTime(orderForm.getRunTime());
        responseQueryOrderForm.setExpireTime(orderForm.getExpireTime());
        responseQueryOrderForm.setStatus(orderForm.getStatus());
        responseQueryOrderForm.setRemark(orderForm.getRemark());
        final Iterator<OrderFormDetail> iterator = orderFormDetails.iterator();
        while (iterator.hasNext()) {
            final OrderFormDetail next = iterator.next();
            if (orderForm.getId() == next.getOrderFormId()) {
                final ResponseQueryOrderForm.passenger passenger = responseQueryOrderForm.new passenger();
                passenger.setOrderFormDetailId(next.getId());
                passenger.setName(next.getPassengerName());
                passenger.setPassportSeNo(next.getPassportSeNoPlain().substring(0, 3) + "***********" + next.getPassportSeNoPlain().substring(14));
                passenger.setPiaoType(next.getPiaoType());
                passenger.setPiaoTypeName(EnumTrainTicketType.of(next.getPiaoType()).getValue());
                passenger.setCxin(next.getCxin());
                passenger.setPrice(next.getPrice());
                passenger.setStatus(next.getStatus());
                passengers.add(passenger);
                iterator.remove();
            }
        }
        return responseQueryOrderForm;
    }
}
