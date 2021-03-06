package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.entity.OrderForm;
import com.jkm.entity.PayExceptionRecord;
import com.jkm.entity.RefundExceptionRecord;
import com.jkm.entity.fusion.QueryQuickPayData;
import com.jkm.entity.fusion.QueryRefundData;
import com.jkm.entity.fusion.SingleRefundData;
import com.jkm.enums.EnumGrabTicketStatus;
import com.jkm.enums.EnumOrderFormStatus;
import com.jkm.service.*;
import com.jkm.util.SnGenerator;
import com.jkm.util.mq.MqConfig;
import com.jkm.util.mq.MqProducer;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    private OrderFormService orderFormService;

    @Autowired
    private GrabTicketFormService grabTicketFormService;

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
        long orderId = 0;
        try{
            JSONObject jo = super.getRequestJsonParams();
            String uid = super.getUid(jo.get("appid"),jo.get("uid"));
            jo.put("uid",uid);
            orderId = jo.getLong("orderId");
            JSONObject responseJo = authenService.toPay(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setData(responseJo.getJSONObject("data"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.info("立即支付(首次)失败:", e);
            responseEntityBase.setMessage("支付异常,请稍后再试");
            responseEntityBase.setCode(500);
            try{
                OrderForm orderForm = new OrderForm();
                orderForm.setId(orderId);
                orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_FAIL.getId());
                orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_FAIL.getValue());
                orderFormService.updateStatus(orderForm);
            }catch (Exception ex){
                responseEntityBase.setMessage("支付异常,请稍后再试");
                responseEntityBase.setCode(500);
            }
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
        long orderId = 0;
        try{
            JSONObject jo = super.getRequestJsonParams();
            String uid = super.getUid(jo.get("appid"),jo.get("uid"));
            jo.put("uid",uid);
            orderId = jo.getLong("orderId");
            JSONObject responseJo = authenService.toPayByCid(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setData(responseJo.getJSONObject("data"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.error("立即支付(多次)异常",e);
            responseEntityBase.setMessage("支付异常,请稍后再试");
            responseEntityBase.setCode(500);
            try{
                OrderForm orderForm = new OrderForm();
                orderForm.setId(orderId);
                orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_FAIL.getId());
                orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_FAIL.getValue());
                orderFormService.updateStatus(orderForm);
            }catch (Exception ex){
                responseEntityBase.setMessage("支付异常,请稍后再试");
                responseEntityBase.setCode(500);
            }
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
        long orderId = 0;
        try{
            JSONObject jo = super.getRequestJsonParams();
            String uid = super.getUid(jo.get("appid"),jo.get("uid"));
            jo.put("uid",uid);
            orderId = jo.getLong("orderId");
            JSONObject responseJo = authenService.toPayGrab(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setData(responseJo.getJSONObject("data"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.info("抢票单立即支付(首次)失败",e);
            responseEntityBase.setMessage("支付异常,请稍后再试");
            responseEntityBase.setCode(500);
            try{
                grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_PAY_FAIL,orderId);
            }catch (Exception ex){
                responseEntityBase.setMessage("支付异常,请稍后再试");
                responseEntityBase.setCode(500);
            }
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
        long orderId = 0;
        try{
            JSONObject jo = super.getRequestJsonParams();
            String uid = super.getUid(jo.get("appid"),jo.get("uid"));
            jo.put("uid",uid);
            orderId = jo.getLong("orderId");
            JSONObject responseJo = authenService.toPayGrabByCid(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setData(responseJo.getJSONObject("data"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.error("立即支付(多次)异常",e);
            responseEntityBase.setMessage("支付异常,请稍后再试");
            responseEntityBase.setCode(500);
            try{
                grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_PAY_FAIL,orderId);
            }catch (Exception ex){
                responseEntityBase.setMessage("支付异常,请稍后再试");
                responseEntityBase.setCode(500);
            }
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
    public ResponseEntityBase<Long> getCode() {
        ResponseEntityBase<Long> responseEntityBase = new ResponseEntityBase<Long>();
        try{
            JSONObject jo = super.getRequestJsonParams();
            String uid = super.getUid(jo.get("appid"),jo.get("uid"));
            jo.put("uid",uid);
            JSONObject responseJo = authenService.getCode(jo);
            if(responseJo.getBoolean("result")==true){
                responseEntityBase.setData(responseJo.getLong("data"));
                responseEntityBase.setMessage(responseJo.getString("message"));
            }else{
                responseEntityBase.setMessage(responseJo.getString("message"));
                responseEntityBase.setCode(400);
            }
        }catch(Exception e){
            logger.error("获取验证码异常",e);
            responseEntityBase.setMessage("获取验证码异常");
            responseEntityBase.setCode(500);
        }
        return responseEntityBase;
    }
    /**
     * 验证支付查询
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTest", method = RequestMethod.POST)
    public JSONObject getTest() {
        JSONObject responseJo = new JSONObject();
        try{
            JSONObject jo = super.getRequestJsonParams();
            QueryQuickPayData queryQuickPayData =new QueryQuickPayData();
            queryQuickPayData.setMercOrdNo(jo.getString("payment"));
            queryQuickPayData.setReqSn(SnGenerator.generate());
            queryQuickPayData.setMercOrdDt(jo.getString("payment").substring(0,8));
            Map<String, Object> result = authenService.queryQuickPay(queryQuickPayData);
            logger.info("结果："+result);
        }catch(Exception e){
            responseJo.put("result",false);
            responseJo.put("message",e.getMessage());
        }
        return responseJo;
    }
    /**
     * 验证退款
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTest1", method = RequestMethod.POST)
    public JSONObject getTest1() {
        JSONObject responseJo = new JSONObject();
        try{
            JSONObject jo = super.getRequestJsonParams();
            SingleRefundData singleRefundData =new SingleRefundData();
            singleRefundData.setReqSn(SnGenerator.generate());
            singleRefundData.setOrgSn(jo.getString("orgSn"));
            singleRefundData.setOrgDate(jo.getString("orgDate"));
            singleRefundData.setRefundAmount(jo.getString("refundAmount"));
            singleRefundData.setOrgAmount(jo.getString("orgAmount"));
            Map<String, Object> result = authenService.singlRefund(singleRefundData);
            logger.info("结果："+result);
        }catch(Exception e){
            responseJo.put("result",false);
            responseJo.put("message",e.getMessage());
        }
        return responseJo;
    }
    /**
     * 验证退款查询
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTest2", method = RequestMethod.POST)
    public JSONObject getTest2() {
        JSONObject responseJo = new JSONObject();
        try{
            JSONObject jo = super.getRequestJsonParams();
            QueryRefundData queryRefundData =new QueryRefundData();
            queryRefundData.setQuerySn(jo.getString("querySn"));
            queryRefundData.setReqSn(SnGenerator.generate());
            queryRefundData.setQueryDate(jo.getString("querySn").substring(0,8));
            Map<String, Object> result = authenService.queryRefund(queryRefundData);
            logger.info("结果："+result);
        }catch(Exception e){
            logger.info("错误信息"+e.getStackTrace());
            responseJo.put("result",false);
            responseJo.put("message",e.getMessage());
        }
        return responseJo;
    }

    /**
     * 验证消息
     *
     * @param requestData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTest3", method = RequestMethod.POST)
    public JSONObject getTest3() {
        JSONObject responseJo = new JSONObject();
        try{
            JSONObject mqJo = new JSONObject();
            mqJo.put("orderFormDetailId", 200);
            mqJo.put("reqSn", "20161117155508149191");
            mqJo.put("sendCount", 0);
            mqJo.put("reqToken", "20161117151617515507");

            MqProducer.sendMessage(mqJo, MqConfig.RETURN_TICKET_REFUND_ING, 2000);
        }catch(Exception e){
            logger.info("错误信息"+e.getStackTrace());
            responseJo.put("result",false);
            responseJo.put("message",e.getMessage());
        }
        return responseJo;
    }


}
