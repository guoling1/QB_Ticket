package com.jkm.controller.api;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestQueryOrderForm;
import com.jkm.controller.helper.response.ResponseQueryOrderForm;
import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.enums.EnumTrainTicketType;
import com.jkm.service.ContactInfoService;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.util.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /**
     * 查询我的订单
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryMyOrder")
    public ResponseEntityBase<Object[]> queryMyOrderForm(final RequestQueryOrderForm request) {
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
    @RequestMapping(value = "queryById")
    public ResponseEntityBase<ResponseQueryOrderForm> queryById(final RequestQueryOrderForm request) {
        final ResponseEntityBase<ResponseQueryOrderForm> results = new ResponseEntityBase<>();
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectById(request.getOrderFormId());
        final OrderForm orderForm = orderFormOptional.get();
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByOrderFormId(request.getOrderFormId());
        results.setData(this.getResponse(orderForm, orderFormDetails));
        return results;
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
        final Iterator<OrderFormDetail> iterator = orderFormDetails.iterator();
        while (iterator.hasNext()) {
            final OrderFormDetail next = iterator.next();
            if (orderForm.getId() == next.getOrderFormId()) {
                final ResponseQueryOrderForm.passenger passenger = responseQueryOrderForm.new passenger();
                passenger.setName(next.getPassengerName());
                passenger.setPassportSeNo(next.getPassportSeNo());
                passenger.setPiaoType(next.getPiaoType());
                passenger.setPiaoTypeName(EnumTrainTicketType.of(next.getPiaoType()).getValue());
                passenger.setCxin(next.getCxin());
                passenger.setPrice(next.getPrice());
                passengers.add(passenger);
                iterator.remove();
            }
        }
        return responseQueryOrderForm;
    }
}
