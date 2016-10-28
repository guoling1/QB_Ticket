package com.jkm.controller.api;

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
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/authen")
public class FusionController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(FusionController.class);
    @Autowired
    private AuthenService authenService;
    /**
     * 快捷支付
     *
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/fastPay", method = RequestMethod.POST)
    public ModelAndView fastPay(@ModelAttribute("requestData") AuthenData requestData) {
        String u1 = requestData.getMercId();
        System.out.println(u1);
        AuthenData data = (AuthenData) request.getSession().getAttribute("requestData");
        data.setVerifyCode(requestData.getVerifyCode());
        data.setReqSn((String) (request.getSession().getAttribute("reqSn")));
        data.setToken((String) (request.getSession().getAttribute("token")));
        data.setMercId((String) (request.getSession().getAttribute("mercId")));
        data.setStep("1");
        return new ModelAndView("fusion/retMsg.jsp", authenService.fastPay(data));
    }

}
