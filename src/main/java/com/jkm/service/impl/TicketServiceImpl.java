package com.jkm.service.impl;

import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import com.jkm.service.ys.entity.YsRefundTicketRequest;
import com.jkm.service.ys.entity.YsRefundTicketResponse;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.SnGenerator;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Service
public class TicketServiceImpl implements TicketService{

    @Autowired
    private OrderFormDetailService orderFormDetailService;
    @Autowired
    private OrderFormService orderFormService;
    @Autowired
    private YsSdkService ysSdkService;
    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Pair<Boolean, String> refund(final long id) {
        
        final YsRefundTicketRequest request = YsRefundTicketRequest.builder().termTransID(SnGenerator.generate()).transID("")
                .passengerID("").build();
        request.setReqDateTime(DateFormatUtil.format(new Date(),"yyyyMMddHHmmss"));
        final YsRefundTicketResponse response = this.ysSdkService.refundTicket(request);
        if(response.getStatus().equals("0000")){
            return Pair.of(true , "退票请求成功");
        }
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
