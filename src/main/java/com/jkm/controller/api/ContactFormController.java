package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestContactForm;
import com.jkm.controller.helper.response.ResponseContactForm;
import com.jkm.service.ContactFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zhangbin on 2016/10/28.
 */
@RequestMapping("/contactForm")
@Controller
public class ContactFormController extends BaseController {
    @Autowired
    private ContactFormService contactFormService;

    /**
     * 添加乘客
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntityBase<ResponseContactForm> add(@RequestBody final RequestContactForm req){
        final ResponseEntityBase<ResponseContactForm> result = new ResponseEntityBase<>();


        return result;
    }

}
