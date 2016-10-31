package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestTicketRefund;
import com.jkm.controller.helper.request.RequestTrainTripsQuery;
import com.jkm.controller.helper.response.ResponseTicketRefund;
import com.jkm.controller.helper.response.ResponseTrainTripsQuery;
import com.jkm.service.TicketService;
import com.jkm.service.TrainTripsQueryService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * Created by yuxiang on 2016-10-27.
 */
@RequestMapping("/ticket")
@Controller
public class TicketController extends BaseController{

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
