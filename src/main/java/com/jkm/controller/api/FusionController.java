package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.entity.fusion.QueryQuickPayData;
import com.jkm.entity.fusion.QueryRefundData;
import com.jkm.entity.fusion.SingleRefundData;
import com.jkm.service.AuthenService;
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
            logger.info(e.getMessage());
            if(e.getMessage()==null){
                responseEntityBase.setMessage("支付异常");
            }else{
                responseEntityBase.setMessage(e.getMessage().toString());
            }
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
            logger.info(e.getMessage());
            if(e.getMessage()==null){
                responseEntityBase.setMessage("支付异常");
            }else{
                responseEntityBase.setMessage(e.getMessage().toString());
            }
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
            logger.info(e.getMessage());
            if(e.getMessage()==null){
                responseEntityBase.setMessage("支付异常");
            }else{
                responseEntityBase.setMessage(e.getMessage().toString());
            }
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
            logger.info(e.getMessage());
            if(e.getMessage()==null){
                responseEntityBase.setMessage("支付异常");
            }else{
                responseEntityBase.setMessage(e.getMessage().toString());
            }
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
    public ResponseEntityBase<Long> getCode() {
        ResponseEntityBase<Long> responseEntityBase = new ResponseEntityBase<Long>();
        try{
            JSONObject jo = super.getRequestJsonParams();
            String uid = super.getUid(jo.getString("appid"),jo.getString("uid"));
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
            logger.info("获取验证码异常");
            logger.info(e.getMessage());
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
            logger.info(e.getMessage());
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
            mqJo.put("reqSn","20161111132457210572");
            mqJo.put("dt", "20161111");
            mqJo.put("sendCount",0);
            mqJo.put("orderId",165);
            MqProducer.sendMessage(mqJo, MqConfig.FAST_PAY_QUERY,2000);
        }catch(Exception e){
            logger.info(e.getMessage());
            responseJo.put("result",false);
            responseJo.put("message",e.getMessage());
        }
        return responseJo;
    }

}
