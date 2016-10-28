package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestBookTicket;
import com.jkm.controller.helper.request.RequestTicketRefund;
import com.jkm.controller.helper.response.ResponseBookTicket;
import com.jkm.controller.helper.response.ResponseTicketRefund;
import com.jkm.service.TicketService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by yuxiang on 2016-10-27.
 */
@Controller
@RequestMapping("/ticket")
public class TicketController extends BaseController{

    private final Logger logger = Logger.getLogger(TicketController.class);

    @Autowired
    private TicketService ticketService;


    @RequestMapping(value = "/bookTicket", method = RequestMethod.POST)
    public ResponseEntityBase<ResponseBookTicket> bookTicket(final RequestBookTicket requestBookTicket) {
        final ResponseEntityBase<ResponseBookTicket> result = new ResponseEntityBase<>();
        logger.info("订票初始化，生成订单 -- start");
        this.ticketService.bookTicket(requestBookTicket);
        logger.info("订票初始化，生成订单 -- end");
        return result;
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
