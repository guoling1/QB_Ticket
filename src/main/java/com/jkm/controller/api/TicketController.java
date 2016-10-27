package com.jkm.controller.api;

import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestTicketRefund;
import com.jkm.controller.helper.response.ResponseTicketRefund;
import com.jkm.service.TicketService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by yuxiang on 2016-10-27.
 */
@RequestMapping("/ticket")
@Controller
public class TicketController {

    private static Logger logger = Logger.getLogger(PacketController.class);

    @Autowired
    private TicketService ticketService;

    /**
     * 火车车票退票受理
     * @param req
     * @return
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public ResponseEntityBase<ResponseTicketRefund> delete(@RequestBody final RequestTicketRefund req) {
        final ResponseEntityBase<ResponseTicketRefund> result = new ResponseEntityBase<>();

            return result;

    }

}
