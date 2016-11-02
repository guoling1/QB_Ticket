package com.jkm.controller.api.hy;

import com.jkm.controller.common.BaseController;
import com.jkm.enums.EnumHTHYMethodCode;
import com.jkm.service.TicketService;
import com.jkm.service.hy.entity.HySubmitOrderCallbackResponse;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.service.ys.entity.YsTrainTicketBookingCallbackResponse;
import com.jkm.util.MD5Util;
import com.jkm.util.ResponseWriter;
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
    private TicketService ticketService;

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
