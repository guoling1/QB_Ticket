package com.jkm.controller.api.hy;

import com.jkm.entity.HyChannelRequestRecord;
import com.jkm.service.TicketService;
import com.jkm.service.hy.HySdkRequestRecordService;
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

/**
 * Created by yuxiang on 2016-11-01.
 */
@Controller
@RequestMapping("/callback/hy")
public class HyCallBackController extends BaseController {

    private static Logger log = Logger.getLogger(HyCallBackController.class);

    @Autowired
    private TicketService ticketService;
    @Autowired
    private HySdkRequestRecordService hySdkRequestRecordService;

    /**
     * 处理抢票回调通知
     *
     * @return
     */
    @RequestMapping(value = "/grab/ticket", method = RequestMethod.POST)
    public void handleGrabCallBackMsg(final HttpServletRequest request,
                                        final HttpServletResponse httpServletResponse) throws Exception {
        log.info("callback request params : " + request.getParameter("data"));
        final String data = request.getParameter("data");
        final JSONObject jsonParams = JSONObject.fromObject(data);
       // final boolean flag = this.isSignCorrect(jsonParams);
        log.info("收到hy抢票回调通知:" + jsonParams.toString());
        //记录回调请求
        this.postHandle("", "抢票回调通知", 0, jsonParams.toString(), "", 0);
        if (true) {
            log.info(jsonParams.toString() + "给hy返回success成功");
            ResponseWriter.writeTxtResponse(httpServletResponse, "SUCCESS");
            this.ticketService.handleGrabCallBackMsg(jsonParams);
        } else {
            log.error("######收到hy抢票回调通知 sign check error,request[" + request.getParameterMap() + "]");
            ResponseWriter.writeTxtResponse(response, "false");
        }
    }

    /**
     * 处理线上线下退票结果推送
     *
     * @return
     */
    @RequestMapping(value = "/refund/ticket", method = RequestMethod.POST)
    public void handleRefundCallBackMsg(final HttpServletRequest request,
                                        final HttpServletResponse httpServletResponse) throws Exception {
        log.info("callback request params : " + request.getParameter("data"));
        final String data = request.getParameter("data");
        final JSONObject jsonParams = JSONObject.fromObject(data);
        //记录回调请求
        //判断是线上退票还是线下退票
        final boolean flag;
        if (jsonParams.getInt("returntype") == 1) {
            this.postHandle("", "收到hy线上退票结果推送", 0, jsonParams.toString(), "", 0);
            //线上 线上退票或线上改签数字签名
            //md5(partnerid+returntype+timestamp+apiorderid+trainorderid+token+returnmoney+returnstate+md5(key))
             final String sign = MD5Util.MD5(HySdkConstans.ORDER_PARTNER_ID + jsonParams.getString("returntype") +jsonParams.getString("timestamp") +
                  jsonParams.getString("apiorderid") + jsonParams.getString("trainorderid") + jsonParams.getString("token") +
                  jsonParams.getString("returnmoney") + jsonParams.getString("returnstate") + MD5Util.MD5(HySdkConstans.ORDER_SIGN_KEY));
            flag = sign.equals(jsonParams.getString("sign"));
            System.out.println(sign);
            log.info("收到hy线上退票的异步通知:" + jsonParams.toString() + "签名结果:" + flag);
        }else{
            this.postHandle("", "收到hy线下退票结果推送", 0, jsonParams.toString(), "", 0);
            //线下 线下退票或线下改签数字签名
            //md5(partnerid+returntype+timestamp+apiorderid+trainorderid+returnmoney+returnstate+md5(key))
            flag = MD5Util.MD5(HySdkConstans.ORDER_PARTNER_ID + jsonParams.getString("returntype") + jsonParams.getString("timestamp") +
                    jsonParams.getString("apiorderid") + jsonParams.getString("trainorderid") + jsonParams.getString("returnmoney")
                    + jsonParams.getString("returnstate") + MD5Util.MD5(HySdkConstans.ORDER_SIGN_KEY)).equals(jsonParams.getString("sign"));
            log.info("收到hy线下退票或线下改签的异步通知:" + jsonParams.toString() + "签名结果:" + flag);
        }
        try{
            this.ticketService.handleRefundCallbackMsg(jsonParams);
        }catch (final Throwable throwable){
            log.error("退票回调处理异常" + jsonParams.toString() + "异常信息:" + throwable.getMessage());
        }
        if (flag) {
            log.info(jsonParams.toString() + "给hy返回success成功");
            ResponseWriter.writeTxtResponse(httpServletResponse, "SUCCESS");
        } else {
            log.error("######收到一个hy退票结果推送 sign check error,request[" + jsonParams.toString() + "]");
            ResponseWriter.writeTxtResponse(response, "false");
        }
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
        log.info("callback request params : " + request.getParameter("data"));
        final String data = request.getParameter("data");
        final JSONObject jsonParams = JSONObject.fromObject(data);
        log.info("收到订单提交的异步通知:[" + jsonParams + "]");
        this.postHandle("", "订单提交回调", 0, jsonParams.toString(), "", 0);
        this.ticketService.handleSubmitOrderCallbackResponse(jsonParams);
        ResponseWriter.writeTxtResponse(response, "success");
        log.info("订单提交异步通知处理结束！！ 已经发送[success]");
    }

    /**
     * 确认订单回调
     *
     * @return
     */
    @RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
    public void handleConfirmOrderCallbackMsg(final HttpServletRequest request,
                                              final HttpServletResponse response) throws IOException {
        final JSONObject jsonParams = new JSONObject();
        jsonParams.put("reqtime", request.getParameter("reqtime"));
        jsonParams.put("sign", request.getParameter("sign"));
        jsonParams.put("orderid", request.getParameter("orderid"));
        jsonParams.put("transactionid", request.getParameter("transactionid"));
        jsonParams.put("isSuccess", request.getParameter("isSuccess"));

        final boolean signCorrect = this.isSignCorrect(jsonParams);
        log.info("收到确认订单的异步通知:[" + jsonParams + "],签名结果[" + signCorrect + "]");
        this.postHandle("", "确认订单回调", 0, response.toString(), "", 0);
        if (signCorrect) {
            this.ticketService.handleConfirmOrderCallbackResponse(jsonParams);
            ResponseWriter.writeTxtResponse(response, "success");
            log.info("确认订单的异步通知处理结束！！ 已经发送[success]");
        } else {
            log.error("##### receive a confirmOrder asking,  sign check error,request[" + jsonParams.toString() + "]");
            ResponseWriter.writeTxtResponse(response, "false");
        }
    }


    private boolean isSignCorrect(final JSONObject jsonObject) {
        try {
            String sign = MD5Util.MD5(HySdkConstans.ORDER_PARTNER_ID + jsonObject.getString("reqtime") + MD5Util.MD5(HySdkConstans.ORDER_SIGN_KEY));
            return sign.equals(jsonObject.getString("sign"));
        } catch (Exception e) {
            log.info(e);
            e.printStackTrace();
        }
        return false;
    }
}
