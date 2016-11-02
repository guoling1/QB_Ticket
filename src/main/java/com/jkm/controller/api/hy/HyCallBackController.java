package com.jkm.controller.api.hy;

import com.jkm.entity.HyChannelRequestRecord;
import com.jkm.service.TicketService;
import com.jkm.service.hy.HySdkRequestRecordService;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.HyRefundCallbackResponse;
import com.jkm.util.ResponseWriter;
import com.jkm.controller.common.BaseController;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.MD5Util;
import net.sf.json.JSONObject;
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
 * Created by yuxiang on 2016-11-01.
 */
@Controller
@RequestMapping("/callback/hy")
public class HyCallBackController extends BaseController {

    private static Logger log = Logger.getLogger(HyCallBackController.class);

    @Autowired
    private HySdkService hySdkService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private HySdkRequestRecordService hySdkRequestRecordService;

    /**
     * 处理线上线下退票结果推送
     *
     * @return
     */
    @RequestMapping(value = "/refund/ticket", method = RequestMethod.POST)
    public void handleRefundCallBackMsg(final HttpServletRequest request,
                                        final HttpServletResponse httpServletResponse,
                                        final HyRefundCallbackResponse response) throws Exception {
        final Map parameterMap = request.getParameterMap();
        if (log.isDebugEnabled()) {
            for (final Object key : parameterMap.keySet()) {
                log.debug("request param[" + key + "]:" + parameterMap.get(key));
            }
        }

        log.info("收到hy退票的异步通知:" + response + "签名结果:" + response.isSignCorrect());
        //记录回调请求
        this.postHandle("", "线上线下退票结果推送", 0, response.toString(), "", 0);
        if (response.isSignCorrect()) {
            this.ticketService.handleRefundCallbackMsg(response);
            ResponseWriter.writeTxtResponse(httpServletResponse, "SUCCESS");
        } else {
            log.error("######收到一个hy代发异步通知 sign check error,request[" + request.getParameterMap() + "]");
        }
        ResponseWriter.writeTxtResponse(httpServletResponse, "success");

    }
        private void postHandle(final String orderId,
                                final String method,
                                final int retCode,
                                final String requestParams,
                                final String returnParams,
                                final long millisecond) {
            final HyChannelRequestRecord record = HyChannelRequestRecord.builder().orderId(orderId).method(method).retCode(retCode).request(requestParams).response(returnParams).time(millisecond).build();
            this.hySdkRequestRecordService.record(record);
    }

    /**
     * 提交订单回调
     *
     * @return
     */
    @RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
    public void handleSubmitOrderCallBackMsg(final HttpServletRequest request,
                                            final HttpServletResponse response) throws IOException {
        this.checkParams(request);
        final JSONObject jsonParams = this.getRequestJsonParams();
        final boolean signCorrect = this.isSignCorrect(jsonParams);
        log.info("收到订单提交的异步通知:[" + jsonParams + "],签名结果[" + signCorrect + "]");
//        this.ysSdkService.recordBookTicketCallbackParams(callbackResponse);
        if (signCorrect) {
            this.ticketService.handleSubmitOrderCallbackResponse(this.getRequestJsonParams());
            ResponseWriter.writeTxtResponse(response, "success");
            log.info("订单提交异步通知处理结束！！ 已经发送[success]");
        } else {
            log.error("#####receive a submitOrder asking, sign check error,request[" + request.getParameterMap() + "]");
            ResponseWriter.writeTxtResponse(response, "false");
        }
    }

    /**
     * 确认订单回调
     *
     * @return
     */
    @RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
    public void handleConfirmOrderCallbackMsg(final HttpServletRequest request,
                                              final HttpServletResponse response) throws IOException {
        this.checkParams(request);
        final JSONObject jsonParams = this.getRequestJsonParams();
        final boolean signCorrect = this.isSignCorrect(jsonParams);
        log.info("收到确认订单的异步通知:[" + jsonParams + "],签名结果[" + signCorrect + "]");
//        this.ysSdkService.recordBookTicketCallbackParams(callbackResponse);
        if (signCorrect) {
            this.ticketService.handleConfirmOrderCallbackResponse(this.getRequestJsonParams());
            ResponseWriter.writeTxtResponse(response, "success");
            log.info("确认订单的异步通知处理结束！！ 已经发送[success]");
        } else {
            log.error("##### receive a confirmOrder asking,  sign check error,request[" + request.getParameterMap() + "]");
            ResponseWriter.writeTxtResponse(response, "false");
        }
    }


    private boolean isSignCorrect(final JSONObject jsonObject) {
        try {
            String sign = MD5Util.MD5(HySdkConstans.PARTNERID + jsonObject.getString("reqtime") + MD5Util.MD5(HySdkConstans.SIGN_KEY));
            return sign.equals(jsonObject.getString("sign"));
        } catch (Exception e) {
            log.info(e);
            e.printStackTrace();
        }
        return false;
    }

    private void checkParams(final HttpServletRequest request) {
        final Map parameterMap = request.getParameterMap();
        if (log.isDebugEnabled()) {
            final Set keySet = parameterMap.keySet();
            for (final Object key : keySet) {
                log.debug("request param[" + key + "]:" + parameterMap.get(key));
            }
        }
    }
}
