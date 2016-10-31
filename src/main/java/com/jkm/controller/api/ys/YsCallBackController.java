package com.jkm.controller.api.ys;

import com.jkm.entity.YsChannelRequestRecord;
import com.jkm.enums.EnumBusinessType;
import com.jkm.service.TicketService;
import com.jkm.service.ys.YsSdkRequestRecordService;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import com.jkm.util.JsonUtil;
import com.jkm.service.ys.entity.YsTrainTicketBookingCallbackResponse;
import com.jkm.util.ResponseWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Controller
@RequestMapping("/callback/ys")
public class YsCallBackController {
    private static Logger log = org.apache.log4j.Logger.getLogger(YsCallBackController.class);

    @Autowired
    private YsSdkService ysSdkService;
    @Autowired
    private TicketService ticketService;

    /**
     * 处理退票结果推送
     *
     * @return
     */
    @RequestMapping(value = "/bookTicket", method = RequestMethod.POST)
    public void handleBookTicketCallBackMsg(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final YsTrainTicketBookingCallbackResponse callbackResponse) throws IOException {
        final Map parameterMap = request.getParameterMap();
        if (log.isDebugEnabled()) {
            final Set keySet = parameterMap.keySet();
            for (final Object key : keySet) {
              //  log.debug("request param[{}]:{}", key, parameterMap.get(key));
            }
        }
        final boolean signCorrect = callbackResponse.isSignCorrect();
       // log.info("收到充值异步通知:[{}],签名结果[{}]", callbackResponse, signCorrect);
        this.ysSdkService.recordBookTicketCallbackParams(callbackResponse);
        if (signCorrect) {
            this.ticketService.handleBookTicketCallbackResponse(callbackResponse);
            ResponseWriter.writeTxtResponse(response, "success");
            log.info("还款处理结束！！ 已经发送[success]");
        } else {
            //log.error("#####receive repay notify content sign check error,request[{}]", request.getParameterMap());
            ResponseWriter.writeTxtResponse(response, "签名失败。");
        }
    }

    @Autowired
    private YsSdkRequestRecordService ysSdkRequestRecordService;
    /**
     * 处理退票结果推送
     *
     * @return
     */
    @RequestMapping(value = "/playmoney", method = RequestMethod.POST)
    public void handleRefundCallBackMsg(final HttpServletRequest request,
                                           final HttpServletResponse httpServletResponse,
                                           final YsRefundCallbackResponse response) throws IOException {
        final Map parameterMap = request.getParameterMap();
        if (log.isDebugEnabled()) {
            for (final Object key : parameterMap.keySet()) {
                log.debug("request param["+ key +"]:" + parameterMap.get(key));
            }
        }

        log.info("收到ys退票的异步通知:"+ response + "签名结果:" + response.isSignCorrect());
        //记录回调请求
        this.postHandle(response.getTermTransID(), EnumBusinessType.REFUND_TICKET_CALL_BACK.getType(), JsonUtil.toJsonString(response),"",0l);
        if (response.isSignCorrect()) {
            this.ticketService.handleRefundCallbackMsg(response);
            ResponseWriter.writeTxtResponse(httpServletResponse, "SUCCESS");
        } else {
            log.error("######收到一个yxt代发异步通知 sign check error,request[" + request.getParameterMap() + "]" );
        }
        ResponseWriter.writeTxtResponse(httpServletResponse, "SUCCESS");
    }

    private void postHandle(final String sn,
                            final String businessType,
                            final String requestParams,
                            final String returnParams,
                            final long millisecond) {
        final YsChannelRequestRecord record = YsChannelRequestRecord.builder().businessType(businessType).sn(sn).request(requestParams)
                .response(returnParams).time(millisecond).build();
        this.ysSdkRequestRecordService.record(record);
    }
}
