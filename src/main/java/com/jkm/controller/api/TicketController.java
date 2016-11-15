package com.jkm.controller.api;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.*;
import com.jkm.controller.helper.response.*;
import com.jkm.entity.GrabTicketForm;
import com.jkm.entity.OrderForm;
import com.jkm.enums.EnumGrabTicketStatus;
import com.jkm.service.GrabTicketFormService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.service.TrainTripsQueryService;
import com.jkm.util.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.http.client.fluent.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


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

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private GrabTicketFormService grabTicketFormService;

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

    @RequestMapping(value = "/test", method =RequestMethod.GET)
    public void select(final long id){
        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectById(id);
        this.ticketService.confirmOrder(orderFormOptional.get());
    }
    /**
     * 火车车票订票 - 提交订单
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
    public ResponseEntityBase<ResponseSubmitOrder> submitOrder(@RequestBody final RequestSubmitOrder request) {
        final ResponseEntityBase<ResponseSubmitOrder> results = new ResponseEntityBase<>();
        request.setUid(super.getUid(request.getAppId(), request.getUid()));
        Preconditions.checkState(StringUtils.isEmpty(request.getMobile()));
        Preconditions.checkState(ValidateUtils.isMobile(request.getMobile()));
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
    @ResponseBody
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public ResponseEntityBase<ResponseCancelOrder> cancelOrder(@RequestBody final RequestCancelOrder request) {
        final ResponseEntityBase<ResponseCancelOrder> results = new ResponseEntityBase<>();
        final Pair<Boolean, String> cancelOrderResult = this.ticketService.cancelOrder(request.getOrderFormId());
        final ResponseCancelOrder responseCancelOrder = new ResponseCancelOrder();
        if (!cancelOrderResult.getLeft()) {
            results.setCode(-1);
            results.setMessage("fail");
        }
        responseCancelOrder.setMsg(cancelOrderResult.getRight());
        responseCancelOrder.setOrderFormId(request.getOrderFormId());
        results.setData(responseCancelOrder);
        return results;
    }


    /**
     * 火车车票退票受理
     * @param req
     * @return
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntityBase<ResponseTicketRefund> refund(@RequestBody final RequestTicketRefund req) {
        final ResponseEntityBase<ResponseTicketRefund> result = new ResponseEntityBase<>();

        try{
            Pair<Boolean,String> pair = this.ticketService.refund(req.getOrderFormDetailId());
            if(pair.getLeft()){
                result.setCode(1);
                result.setMessage(pair.getRight());
            }else{
                result.setCode(-1);
                result.setMessage(pair.getRight());
            }
        }catch(final Throwable throwable){
            result.setCode(-1);
            result.setMessage("退票失败");
            logger.error(req.getOrderFormDetailId() + "订单退票失败,失败原因:" + throwable.getMessage());
        }
        return result;
    }

    /**
     * 火车车票抢票受理
     * @param requset
     * @return
     */
    @RequestMapping(value = "/grab", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntityBase<ResponseGrabTicket> grabTicket(@RequestBody final RequestGrabTicket requset) throws Exception {
        this.ticketService.requestGrabImpl(37);
        requset.setUid(super.getUid(requset.getAppId(), requset.getUid()));
        Preconditions.checkState(ValidateUtils.isMobile(requset.getPhone()));
        final ResponseEntityBase<ResponseGrabTicket> result = new ResponseEntityBase<>();
        try{
            logger.info("用户uid=" + requset.getUid() + "下了一个抢票单");
            ResponseGrabTicket responseGrabTicket = this.ticketService.grabTicket(requset);
                result.setCode(1);
                result.setMessage("抢票单受理成功");
                result.setData(responseGrabTicket);
        }catch(final Throwable throwable){
            logger.error("下抢票单异常 异常信息:" + throwable.getMessage());
            result.setCode(-1);
            result.setMessage("抢票订单受理失败");
        }
        return result;
    }


    /**
     * 火车车票取消抢票受理(付款后)
     * @param req
     * @return
     */
    @RequestMapping(value = "/cancel/grab", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntityBase<Object> cancelGrabTicket(@RequestBody final RequestCancelGrabTicket req) {
        final ResponseEntityBase<Object> result = new ResponseEntityBase<>();

        try{
            Pair<Boolean,String> pair = this.ticketService.cancelGrabTicket(req.getGrabTicketFormId());
            if(pair.getLeft()){
                result.setCode(1);
                result.setMessage("订单取消成功");
            }else{
                result.setCode(-1);
                result.setMessage(pair.getRight());
            }
        }catch(final Throwable throwable){
            result.setCode(-1);
            result.setMessage("取消订单失败");
            logger.error(req.getGrabTicketFormId() + "抢票单取消订单失败,失败原因:"+ throwable.getMessage());
        }
        return result;
    }

    /**
     * 火车车票取消抢票订单(付款前)
     * 抢票单未付款前取消
     * @param req
     * @return
     */
    @RequestMapping(value = "/cancel/grabForm", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntityBase<Object> cancelGrabForm(@RequestBody final RequestCancelGrabTicket req) {
        final ResponseEntityBase<Object> result = new ResponseEntityBase<>();
        try{
            final Optional<GrabTicketForm> grabTicketFormOptional = this.grabTicketFormService.selectByIdWithLock(req.getGrabTicketFormId());
            Preconditions.checkState(grabTicketFormOptional.isPresent(), "抢票订单不存在");
            final GrabTicketForm grabTicketForm = grabTicketFormOptional.get();
            if (grabTicketForm.isCanCancelBeforeCharge()){
                //修改状态
                this.grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_CANCEL, grabTicketForm.getId());
                result.setCode(1);
                result.setMessage("订单取消成功");
                return result;
            }else{
                result.setCode(-1);
                result.setMessage("取消订单失败");
                return result;
            }

        }catch(final Throwable throwable){
            result.setCode(-1);
            result.setMessage("取消订单失败");
            logger.error(req.getGrabTicketFormId() + "抢票单取消订单失败,失败原因:"+ throwable.getMessage());
        }
        return result;
    }

}
