package com.jkm.controller.api;

import com.jkm.controller.helper.ResponseEntityBase;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yuxiang on 2016-11-01.
 */
@Controller
@RequestMapping(value = "/policy")
public class PolicyController {

    /**
     * 购买保险单
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntityBase<Object> add(){
        return null;
    }
}
