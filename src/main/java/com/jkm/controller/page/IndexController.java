package com.jkm.controller.page;

import com.jkm.controller.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "")
public class IndexController extends BaseController {
    /**
     * 快捷支付页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String fastPayPage() {
        return "index.html";
    }
}
