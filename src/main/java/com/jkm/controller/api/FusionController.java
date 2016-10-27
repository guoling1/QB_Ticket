package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.entity.fusion.AuthenData;
import com.jkm.entity.fusion.FastQueryData;
import com.jkm.service.AuthenService;
import com.jkm.util.fusion.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(value = "/authen")
public class FusionController extends BaseController {
    @Autowired
    private AuthenService authenService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
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

    /**
     * 快捷支付页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/fastPayQueryPage", method = RequestMethod.GET)
    public String fastPayQueryPage() {
        return "fastPayQueryPage";
    }

    @RequestMapping(value = "/fastPayCustomerPage", method = RequestMethod.GET)
    public String fastPayCustomerPage() {
        return "fastPayCustomerPage";
    }



    /**
     * 快捷支付(商户下发)
     *
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/fastPayCustomer", method = RequestMethod.POST)
    public ModelAndView fastPayCustomer(@ModelAttribute("requestData") AuthenData requestData) {

        String u1 = requestData.getMercId();
        System.out.println(u1);
        requestData.setReqSn(DateUtils.getDateString(new Date(), DateUtils.formate_string_yyyyMMddhhmmss));
        requestData.setStep("1");
        return new ModelAndView("retMsg", authenService.fastPay(requestData));
    }

    /**
     * 快捷支付查询
     *
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/fastPayQuery", method = RequestMethod.POST)
    public ModelAndView fastPayQuery(@ModelAttribute("requestData") FastQueryData requestData) {
        String u1 = requestData.getMercId();
        System.out.println(u1);

        return new ModelAndView("retMsg", authenService.fastPayQuery(requestData));
    }

}
