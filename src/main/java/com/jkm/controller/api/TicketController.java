package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestSubmitOrder;
import com.jkm.controller.helper.request.RequestCancelOrder;
import com.jkm.controller.helper.request.RequestTicketRefund;
import com.jkm.controller.helper.request.RequestTrainTripsQuery;
import com.jkm.controller.helper.response.ResponseSubmitOrder;
import com.jkm.controller.helper.response.ResponseCancelOrder;
import com.jkm.controller.helper.response.ResponseTicketRefund;
import com.jkm.controller.helper.response.ResponseTrainTripsQuery;
import com.jkm.service.TicketService;
import com.jkm.service.TrainTripsQueryService;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yuxiang on 2016-10-27.
 */
@RequestMapping("/ticket")
@Controller
public class TicketController extends BaseController{
    private static Logger logger = Logger.getLogger(TicketController.class);
    @Autowired
    private TicketService ticketService;

    @Autowired
    private TrainTripsQueryService trainTripsQueryService;


    /**
     * 火车车次查询
     *
     */
    @RequestMapping(value = "/select", method =RequestMethod.POST)
    public ResponseEntityBase<ResponseTrainTripsQuery> select(@RequestBody final List<RequestTrainTripsQuery> requestTrainTripsQueries){
        final ResponseEntityBase<ResponseTrainTripsQuery> result = new ResponseEntityBase<>();
        try {
            List<RequestTrainTripsQuery> list = this.trainTripsQueryService.select(requestTrainTripsQueries);
        }catch(final Throwable throwable){
            result.setCode(-1);
            result.setMessage("查询失败");
        }
        return result;
    }

    /**
     * 火车车票订票 - 提交订单
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
    public ResponseEntityBase<ResponseSubmitOrder> submitOrder(final RequestSubmitOrder request) throws IOException {
        final ResponseEntityBase<ResponseSubmitOrder> results = new ResponseEntityBase<>();
        final RequestSubmitOrder.Passenger passenger = request.new Passenger();
        passenger.setId(1);
        passenger.setPiaoType("1");
        final ArrayList<RequestSubmitOrder.Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);
        request.setPassengers(passengers);
        final Triple<Boolean, String, Long> submitOrderResult = this.ticketService.submitOrder(request);
        final ResponseSubmitOrder responseBookTicket = new ResponseSubmitOrder();
        if (submitOrderResult.getLeft()) {
            responseBookTicket.setOrderFormId(submitOrderResult.getRight());
            responseBookTicket.setMsg(submitOrderResult.getMiddle());
        } else {
            results.setCode(-1);
            results.setMessage("fail");
            responseBookTicket.setMsg(submitOrderResult.getMiddle());
        }
        results.setData(responseBookTicket);
        return results;
    }

    /**
     * 取消订单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntityBase<ResponseCancelOrder> cancelOrder(final RequestCancelOrder request) {
        final ResponseEntityBase<ResponseCancelOrder> results = new ResponseEntityBase<>();
        final Pair<Boolean, String> cancelOrderResult = this.ticketService.cancelOrder(request.getOrderFormId());
        final ResponseCancelOrder responseCancelOrder = new ResponseCancelOrder();
        if (!cancelOrderResult.getLeft()) {
            results.setCode(-1);
            results.setMessage("fail");
        }
        responseCancelOrder.setMsg(cancelOrderResult.getRight());
        results.setData(responseCancelOrder);
        return results;
    }


    /**
     * 火车车票退票受理
     * @param req
     * @return
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public ResponseEntityBase<ResponseTicketRefund> refund(@RequestBody final RequestTicketRefund req) {
        final ResponseEntityBase<ResponseTicketRefund> result = new ResponseEntityBase<>();

        try{
            Pair<Boolean,String> pair = this.ticketService.refund(req.getOrderFormDetailId());
        }catch(final Throwable throwable){

            result.setCode(-1);
            result.setMessage("退票失败");
        }

        return result;

    }

}
