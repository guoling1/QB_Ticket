package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.entity.QbBindCard;
import com.jkm.entity.fusion.AuthenData;
import com.jkm.entity.fusion.SingleRefundData;
import com.jkm.service.AuthenService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

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
    @RequestMapping(value = "/getVerifyCode", method = RequestMethod.POST)
    public JSONObject getVerifyCode(@ModelAttribute("requestData") AuthenData requestData) {
        JSONObject result = new JSONObject();
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
    @RequestMapping(value = "/fastPay", method = RequestMethod.POST)
    public JSONObject fastPay(@ModelAttribute("requestData") AuthenData requestData) {
        JSONObject result = new JSONObject();
        AuthenData data = (AuthenData) request.getSession().getAttribute("requestData");
        data.setVerifyCode(requestData.getVerifyCode());
        data.setReqSn((String) (request.getSession().getAttribute("reqSn")));
        data.setToken((String) (request.getSession().getAttribute("token")));
        data.setMercId((String) (request.getSession().getAttribute("mercId")));
        data.setStep("1");
        Map<String, Object> map = authenService.fastPay(data);
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

    /**
     * 单笔退款
     *
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/singlRefund", method = RequestMethod.POST)
    public JSONObject singlRefund() {
        JSONObject result = new JSONObject();
        SingleRefundData singleRefundData = new SingleRefundData();
        Map<String, Object> map = authenService.singlRefund(singleRefundData);
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
    /**
     * 绑定银行卡
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/bindCard", method = RequestMethod.POST)
    public JSONObject QbBindCard(@ModelAttribute("requestData") QbBindCard requestData) {
        JSONObject result = new JSONObject();
        long backId = authenService.bindCard(requestData);
        if(backId>0){
            result.put("message", "新增成功");
            result.put("data",backId);
            result.put("code", "200");
        }else{
            result.put("message", "新增失败");
            result.put("data",backId);
            result.put("code", "401");
        }
        return result;
    }
    /**
     * 删除银行卡
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/deleteCard", method = RequestMethod.POST)
    public JSONObject deleteCard(@ModelAttribute("requestData") QbBindCard requestData) {
        JSONObject result = new JSONObject();
        authenService.deleteCard(requestData.getId());
        result.put("message", "删除成功");
        result.put("code", "200");
        return result;
    }

    /**
     * 用户银行卡列表
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/cardList", method = RequestMethod.POST)
    public JSONObject cardList(@ModelAttribute("requestData") QbBindCard requestData) {
        JSONObject result = new JSONObject();
        List<QbBindCard> list =  authenService.cardList(requestData.getUid());
        result.put("message", "删除成功");
        result.put("data",list);
        result.put("code", "200");
        return result;
    }
}
