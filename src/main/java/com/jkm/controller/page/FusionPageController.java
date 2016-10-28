package com.jkm.controller.page;

import com.jkm.controller.common.BaseController;
import com.jkm.entity.fusion.AuthenData;
import com.jkm.service.AuthenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping(value = "/authenPage")
public class FusionPageController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(FusionPageController.class);

    @Autowired
    private AuthenService authenService;

    /**
     * 快捷支付页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/fastPayPage", method = RequestMethod.GET)
    public String fastPayPage() {
        return "fusion/fastPayPage.jsp";
    }

    /**
     * 鉴权获取验证码
     *
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/fastPayVerifyCode", method = RequestMethod.POST)
    public String authenVerifyCode(@ModelAttribute("requestData") AuthenData requestData) {
        requestData.setStep("0");
        Map<String, Object> map = authenService.fastPay(requestData);
        if ("true".equals(map.get("retCode").toString())) {
            request.getSession().setAttribute("requestData", requestData);
            request.getSession().setAttribute("reqSn", map.get("reqSn"));
            request.getSession().setAttribute("token", map.get("token"));
            request.getSession().setAttribute("mercId", requestData.getMercId());
            return "fusion/fastPayPage2.jsp";
        } else {
            request.setAttribute("retCode", map.get("retCode").toString());
            request.setAttribute("retMsg", "短信发送失败");
            request.setAttribute("retXml", map.get("retXml"));
            return "fusion/fastPaySendError.jsp";
        }
    }
}
