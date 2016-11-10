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
     * 立即支付
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toPay", method = RequestMethod.POST)
    public ResponseEntityBase<JSONObject> toPay() {
        ResponseEntityBase<JSONObject> result = new ResponseEntityBase<>();
        try{
            JSONObject jo = super.getRequestJsonParams();
            Map<String, Object> map = authenService.toPay(jo);
            if ((boolean)map.get("retCode")==true) {
                result.setCode(2000);
                result.setData((JSONObject)map.get("retData"));
                result.setMessage("支付成功");
            }else {
                result.setCode(2001);
                result.setMessage(map.get("retMsg").toString());
            }
        }catch(Exception e){
            result.setCode(2001);
            result.setMessage("支付失败");
        }
        return result;
    }

}
