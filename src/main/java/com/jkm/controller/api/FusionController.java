package com.jkm.controller.api;

import com.google.common.base.Preconditions;
import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestTicketRefund;
import com.jkm.controller.helper.response.ResponseTicketRefund;
import com.jkm.entity.fusion.AuthenData;
import com.jkm.entity.fusion.QueryQuickPayData;
import com.jkm.entity.fusion.QueryRefundData;
import com.jkm.entity.fusion.SingleRefundData;
import com.jkm.enums.notifier.EnumVerificationCodeType;
import com.jkm.service.AuthenService;
import com.jkm.service.notifier.SmsAuthService;
import com.jkm.util.ValidationUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
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
     * 大订单立即支付(首次)
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toPay", method = RequestMethod.POST)
    public ResponseEntityBase<JSONObject> toPay() {
        ResponseEntityBase<JSONObject> responseEntityBase = new ResponseEntityBase<JSONObject>();
        try{
            JSONObject jo = super.getRequestJsonParams();
            JSONObject responseJo = authenService.toPay(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setData(responseJo.getJSONObject("data"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.info("立即支付(首次)失败");
            responseEntityBase.setMessage(e.getMessage().toString());
            responseEntityBase.setCode(500);
        }
        return responseEntityBase;
    }
    /**
     * 大订单立即支付(多次)
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toPayByCid", method = RequestMethod.POST)
    public ResponseEntityBase<JSONObject> toPayByCid() {
        ResponseEntityBase<JSONObject> responseEntityBase = new ResponseEntityBase<JSONObject>();
        try{
            JSONObject jo = super.getRequestJsonParams();
            JSONObject responseJo = authenService.toPayByCid(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setData(responseJo.getJSONObject("data"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.info("立即支付(多次)失败");
            responseEntityBase.setMessage(e.getMessage().toString());
            responseEntityBase.setCode(500);
        }
        return responseEntityBase;
    }

    /**
     * 抢票单立即支付(首次)
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toPayGrab", method = RequestMethod.POST)
    public ResponseEntityBase<JSONObject> toPayGrab() {
        ResponseEntityBase<JSONObject> responseEntityBase = new ResponseEntityBase<JSONObject>();
        try{
            JSONObject jo = super.getRequestJsonParams();
            JSONObject responseJo = authenService.toPayGrab(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setData(responseJo.getJSONObject("data"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.info("抢票单立即支付(首次)失败");
            responseEntityBase.setMessage(e.getMessage().toString());
            responseEntityBase.setCode(500);
        }
        return responseEntityBase;
    }
    /**
     * 抢票单立即支付(多次)
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toPayGrabByCid", method = RequestMethod.POST)
    public ResponseEntityBase<JSONObject> toPayGrabByCid() {
        ResponseEntityBase<JSONObject> responseEntityBase = new ResponseEntityBase<JSONObject>();
        try{
            JSONObject jo = super.getRequestJsonParams();
            JSONObject responseJo = authenService.toPayGrabByCid(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setData(responseJo.getJSONObject("data"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.info("立即支付(多次)异常");
            responseEntityBase.setMessage(e.getMessage().toString());
            responseEntityBase.setCode(500);
        }
        return responseEntityBase;
    }
    /**
     * 获取验证码
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    public ResponseEntityBase<Object> getCode() {
        ResponseEntityBase<Object> responseEntityBase = new ResponseEntityBase<Object>();
        try{
            JSONObject jo = super.getRequestJsonParams();
            String uid = super.getUid(jo.getString("appid"),jo.getString("uid"));
            jo.put("uid",uid);
            JSONObject responseJo = authenService.getCode(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setMessage(responseJo.getString("message"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.info("获取验证码异常");
            responseEntityBase.setMessage(e.getMessage().toString());
            responseEntityBase.setCode(500);
        }
        return responseEntityBase;
    }
    /**
     * 验证查询方法
     *
     * @param requestData
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value = "/getTest", method = RequestMethod.POST)
//    public JSONObject getTest() {
//        JSONObject responseJo = new JSONObject();
//        try{
//            JSONObject jo = super.getRequestJsonParams();
//            String uid = super.getUid(jo.getString("appid"),jo.getString("uid"));
//            jo.put("uid",uid);
//            QueryQuickPayData queryQuickPayData =new QueryQuickPayData();
//            queryQuickPayData.setMercOrdDt();
//            responseJo = authenService.queryQuickPay()
//        }catch(Exception e){
//            responseJo.put("result",false);
//            responseJo.put("message",e.getMessage());
//        }
//        return responseJo;
//    }

}
