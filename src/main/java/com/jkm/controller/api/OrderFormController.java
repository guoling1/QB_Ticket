package com.jkm.controller.api;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestQueryMyOrderForm;
import com.jkm.controller.helper.response.ResponseQueryMyOrderForm;
import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
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
@RequestMapping(value = "/orderForm")
public class OrderFormController extends BaseController {
    private static Logger log = Logger.getLogger(OrderFormController.class);

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private OrderFormDetailService orderFormDetailService;

    @RequestMapping(value = "query")
    @ResponseBody
    public ResponseEntityBase<Object[]> QueryMyOrderForm(final RequestQueryMyOrderForm request) {
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
        final List<ResponseQueryMyOrderForm> orderFormList = Lists.transform(orderForms, new Function<OrderForm, ResponseQueryMyOrderForm>() {
            @Override
            public ResponseQueryMyOrderForm apply(OrderForm orderForm) {
                final ResponseQueryMyOrderForm responseQueryMyOrderForm = new ResponseQueryMyOrderForm();
                final ArrayList<ResponseQueryMyOrderForm.passenger> passengers = new ArrayList<>();
                responseQueryMyOrderForm.setPassengers(passengers);
                responseQueryMyOrderForm.setOrderFormId(orderForm.getId());
                responseQueryMyOrderForm.setUid(orderForm.getUid());
                responseQueryMyOrderForm.setPrice(orderForm.getPrice());
                responseQueryMyOrderForm.setTotalPrice(orderForm.getTotalPrice());
                responseQueryMyOrderForm.setTrainNo(orderForm.getTrainNo());
                responseQueryMyOrderForm.setDepartStation(orderForm.getDepartStation());
                responseQueryMyOrderForm.setArriveStation(orderForm.getArriveStation());
                responseQueryMyOrderForm.setDepartDate(orderForm.getDepartDate());
                responseQueryMyOrderForm.setArriveDate(orderForm.getArriveDate());
                responseQueryMyOrderForm.setDepartTime(orderForm.getDepartTime());
                responseQueryMyOrderForm.setArriveTime(orderForm.getArriveTime());
                responseQueryMyOrderForm.setContactName(orderForm.getContactName());
                responseQueryMyOrderForm.setContactMobile(orderForm.getContactMobile());
                responseQueryMyOrderForm.setStatus(orderForm.getStatus());
                final Iterator<OrderFormDetail> iterator = orderFormDetails.iterator();
                while (iterator.hasNext()) {
                    final OrderFormDetail next = iterator.next();
                    if (orderForm.getId() == next.getOrderFormId()) {
                        final ResponseQueryMyOrderForm.passenger passenger = responseQueryMyOrderForm.new passenger();
                        passenger.setPassengerName(next.getPassengerName());
                        passenger.setPassengerType(next.getPassengerType());
                        passenger.setSeatType(next.getSeatType());
                        passenger.setSeatName(next.getSeatName());
                        passenger.setPrice(next.getPrice());
                        passengers.add(passenger);
                        iterator.remove();
                    }
                }
                return responseQueryMyOrderForm;
            }
        });
        results.setData(orderFormList.toArray());
        return results;
    }

}
