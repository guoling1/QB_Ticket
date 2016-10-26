package com.jkm.controller.api;


import com.jkm.service.JkmPacketGetService;
import com.jkm.service.JkmPacketSendService;
import com.jkm.controller.common.BaseController;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;


@Controller
@RequestMapping("/packet")
public class PacketController extends BaseController {
    private static Logger logger = Logger.getLogger(PacketController.class);
    @Resource
    private JkmPacketGetService jkmPacketGetService;
    @Resource
    private JkmPacketSendService jkmPacketSendService;

    /**
     * 发红包操作(缺验证)
     * @return
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendRedPacket() throws Exception {
        logger.info("进入发红包接口");
        JSONObject jo = super.getRequestJsonParams();//获取参数
        if(jo.getInt("amount")<1){
            throw new Exception("至少一分");
        }
        if(jo.getInt("count")<1){
            throw new Exception("请输入随机个数");
        }
        if(jo.getInt("count")>jo.getInt("amount")){
            throw new Exception("红包个数不能总金额（分）");
        }
        long backId = jkmPacketSendService.insert(jo.getString("type"),jo.getLong("userId"),jo.getInt("amount"),jo.getInt("count"));
        JSONObject result = new JSONObject();
        if(backId>0){
            result.put("message", "新增成功");
            result.put("data",backId);
            result.put("code", "200");
        }else{
            result.put("message", "新增失败");
            result.put("data",backId);
            result.put("code", "401");
        }
        return result;
    }

    /**
     * 抢红包操作
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getRedPacket() throws IOException {
        JSONObject jo = super.getRequestJsonParams();//获取参数
        JSONObject result = null;
        try{
            result = jkmPacketGetService.getRandomPacket(jo.getLong("userId"),jo.getLong("sendId"));
        }catch(Exception e){
            logger.info("错误信息为："+e.getMessage());
        }finally {
            logger.info("函数执行完毕");
        }
        return result;
    }
}
