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
     * 立即支付(首次)
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toPay", method = RequestMethod.POST)
    public JSONObject toPay() {
        JSONObject responseJo = new JSONObject();
        try{
            JSONObject jo = super.getRequestJsonParams();
            responseJo = authenService.toPay(jo);
        }catch(Exception e){
            logger.info("立即支付(首次)失败");
            responseJo.put("result",false);
            responseJo.put("message",e.getMessage());
        }
        return responseJo;
    }
    /**
     * 立即支付(多次)
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toPayByCid", method = RequestMethod.POST)
    public JSONObject toPayByCid() {
        JSONObject responseJo = new JSONObject();
        try{
            JSONObject jo = super.getRequestJsonParams();
            responseJo = authenService.toPayByCid(jo);
        }catch(Exception e){
            logger.info("立即支付(多次)");
            responseJo.put("result",false);
            responseJo.put("message",e.getMessage());
        }
        return responseJo;
    }

}
