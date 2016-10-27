package com.jkm.controller.api.ys;

import com.jkm.service.TicketService;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import com.jkm.util.ResponseWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Slf4j
@Controller
@RequestMapping("/callback/ys")
public class YsCallBackController {
    @Autowired
    private YsSdkService ysSdkService;
    @Autowired
    private TicketService ticketService;

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
                log.debug("request param[{}]:{}", key, parameterMap.get(key));
            }
        }

        log.info("收到ys退票的异步通知:{},签名结果", response, response.isSignCorrect());
        //this.ysSdkService.recordRefundCallBackParams(response);
        if (response.isSignCorrect()) {
            this.ticketService.handleRefundCallbackMsg(response);
            ResponseWriter.writeTxtResponse(httpServletResponse, "success");
        } else {
            log.error("######收到一个yxt代发异步通知 sign check error,request[{}]", request.getParameterMap());
        }
        ResponseWriter.writeTxtResponse(httpServletResponse, "success");
    }

}
