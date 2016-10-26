package com.jkm.controller.page;

import com.jkm.controller.common.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Scope("prototype")
@RequestMapping("alipayPage")
public class AlipayPageController extends BaseController {
    /**
     * web支付页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model) {
        return "alipay/web/index.jsp";
    }

    @RequestMapping("alipayApi")
    public String alipayapi(HttpServletRequest request, Model model) {
        return "alipay/web/alipayapi.jsp";
    }

    @RequestMapping("returnUrl")
    public String returnUrl(HttpServletRequest request, Model model) {
        return "alipay/web/return_url.jsp";
    }

    @RequestMapping("notifyUrl")
    public String notifyUrl(HttpServletRequest request, Model model) {
        return "alipay/web/notify_url.jsp";
    }

    /**
     * wap支付
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("indexWap")
    public String indexWap(HttpServletRequest request, Model model) {
        return "alipay/wap/index.jsp";
    }

    @RequestMapping("alipayWapApi")
    public String alipayWapapi(HttpServletRequest request, Model model) {
        return "alipay/wap/alipayapi.jsp";
    }

    @RequestMapping("returnWapUrl")
    public String returnWapUrl(HttpServletRequest request, Model model) {
        return "alipay/wap/return_url.jsp";
    }

    @RequestMapping("notifyWapUrl")
    public String notifyWapUrl(HttpServletRequest request, Model model) {
        return "alipay/wap/notify_url.jsp";
    }
}
