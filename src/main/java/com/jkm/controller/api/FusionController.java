package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestTicketRefund;
import com.jkm.controller.helper.response.ResponseTicketRefund;
import com.jkm.entity.fusion.AuthenData;
import com.jkm.entity.fusion.QueryQuickPayData;
import com.jkm.entity.fusion.QueryRefundData;
import com.jkm.entity.fusion.SingleRefundData;
import com.jkm.service.AuthenService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

/**
 * 合众快捷支付
 */
@Controller
@RequestMapping(value = "/authen")
public class FusionController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(FusionController.class);
    @Autowired
    private AuthenService authenService;

    /**
     * 获取短信验证码
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVerifyCode", method = RequestMethod.POST)
    public JSONObject getVerifyCode(@ModelAttribute("requestData") AuthenData requestData) {
        JSONObject result = new JSONObject();
        //测试数据开始
        requestData.setCrdNo("6228481698729890079");
        requestData.setCapCrdNm("张三");
        requestData.setAmount("100");
        requestData.setIdType("00");
        requestData.setIdNo("430225198301087889");
        requestData.setPhoneNo("13146716739");
        //测试数据结束
        requestData.setStep("0");
        Map<String, Object> map = authenService.fastPay(requestData);
        if ("true".equals(map.get("retCode").toString())) {
            request.getSession().setAttribute("requestData", requestData);
            request.getSession().setAttribute("reqSn", map.get("reqSn"));
            request.getSession().setAttribute("token", map.get("token"));
            request.getSession().setAttribute("mercId", requestData.getMercId());
            result.put("message", "短信发送成功");
            result.put("data",map);
            result.put("code", "200");
            //模拟
            AuthenData data = (AuthenData) request.getSession().getAttribute("requestData");
            data.setVerifyCode("123456");
            data.setReqSn((String) (request.getSession().getAttribute("reqSn")));
            data.setToken((String) (request.getSession().getAttribute("token")));
            data.setMercId((String) (request.getSession().getAttribute("mercId")));
            data.setStep("1");
            map = authenService.fastPay(data);
            if ("true".equals(map.get("retCode").toString())) {
                result.put("message", "支付成功");
                result.put("data",map);
                result.put("code", "200");
                //此处修改订单状态和更改流水号
            } else {
                result.put("message", map.get("retMsg").toString());
                result.put("code", "401");
                //不做任何处理
            }
        } else {
            request.setAttribute("retCode", map.get("retCode").toString());
            request.setAttribute("retMsg", "短信发送失败");
            request.setAttribute("retXml", map.get("retXml"));
            result.put("message", "短信发送失败");
            result.put("code", "401");
        }
        return result;
    }

    /**
     * 快捷支付
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fastPay", method = RequestMethod.POST)
    public ModelAndView fastPay(@ModelAttribute("requestData") AuthenData requestData) {
        JSONObject result = new JSONObject();
        AuthenData data = (AuthenData) request.getSession().getAttribute("requestData");
        data.setVerifyCode("123456");
        data.setReqSn((String) (request.getSession().getAttribute("reqSn")));
        data.setToken((String) (request.getSession().getAttribute("token")));
        data.setMercId((String) (request.getSession().getAttribute("mercId")));
        data.setStep("1");
//        Map<String, Object> map = authenService.fastPay(data);
//        if ("true".equals(map.get("retCode").toString())) {
//            result.put("message", "支付成功");
//            result.put("data",map);
//            result.put("code", "200");
//            //此处修改订单状态和更改流水号
//        } else {
//            result.put("message", map.get("retMsg").toString());
//            result.put("code", "401");
//            //不做任何处理
//        }
//        return  result;
        return new ModelAndView("fusion/retMsg.jsp", authenService.fastPay(data));
    }

    /**
     * 立即支付
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toPay", method = RequestMethod.POST)
    public ResponseEntityBase<JSONObject> toPay() throws IOException {
        final ResponseEntityBase<JSONObject> result = new ResponseEntityBase<>();
        JSONObject jo = super.getRequestJsonParams();
        Map<String, Object> map = authenService.toPay(jo);
        if ("true".equals(map.get("retCode").toString())) {
            result.setCode(2000);
            result.setData((JSONObject)map.get("retData"));
            result.setMessage("支付成功");
        }else {
            result.setCode(2001);
            result.setMessage(map.get("retMsg").toString());
        }
        return result;
    }
    /**
     * 单笔退款
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/singlRefund", method = RequestMethod.POST)
    public JSONObject singlRefund(@ModelAttribute("requestData") SingleRefundData requestData) {
        JSONObject result = new JSONObject();
//        requestData.setOrgSn("20161031184113");
//        requestData.setOrgDate("20161031");
//        requestData.setOrgAmount("100");
//        requestData.setRefundAmount("100");
        Map<String, Object> map = authenService.singlRefund(requestData);
        if ("true".equals(map.get("retCode").toString())) {
            result.put("message", "支付成功");
            result.put("data",map);
            result.put("code", "200");
            //此处修改订单状态和更改流水号
        } else {
            result.put("message", map.get("retMsg").toString());
            result.put("code", "401");
            //不做任何处理
        }
        return  result;
    }

}
