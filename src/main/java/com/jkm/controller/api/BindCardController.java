package com.jkm.controller.api;

import com.google.common.base.Preconditions;
import com.jkm.controller.common.BaseController;
import com.jkm.entity.BindCard;
import com.jkm.service.BindCardService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/card")
public class BindCardController extends BaseController {
    private static Logger log = Logger.getLogger(BindCardController.class);
    @Autowired
    private BindCardService bindCardService;
    /**
     * 绑定银行卡
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public JSONObject bind(){
        JSONObject responseJson = new JSONObject();
        try{
            JSONObject requestJson = super.getRequestJsonParams();
            log.info("银行卡参数："+requestJson.toString());
            BindCard bindCard = new BindCard();
            bindCard.setUid(super.getUid(requestJson.getString("appid"),requestJson.getString("uid")));
            bindCard.setCardNo(requestJson.getString("cardNo"));
            bindCard.setAccountName(requestJson.getString("accountName"));
            bindCard.setCardType(requestJson.getString("cardType"));
            bindCard.setCardId(requestJson.getString("cardId"));
            bindCard.setPhone(requestJson.getString("phone"));
            JSONObject jo = bindCardService.insertSelective(bindCard);
            if(jo.getBoolean("success")){
                responseJson.put("result", true);
                responseJson.put("data",jo.getLong("data"));
                responseJson.put("message", jo.getString("message"));
            }else{
                responseJson.put("result", false);
                responseJson.put("data", 0);
                responseJson.put("message", jo.getString("message"));
            }
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
            responseJson.put("data", 0);
            responseJson.put("message", "绑定异常");
        }
        return responseJson;
    }

    /**
     * 银行卡列表
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public JSONObject list(){
        JSONObject responseJson = new JSONObject();
        try {
            JSONObject requestJson = super.getRequestJsonParams();
            String uid = super.getUid(requestJson.getString("appid"),requestJson.getString("uid"));
            log.info("银行卡参数："+requestJson.toString());
            List<BindCard> list = bindCardService.selectByUid(uid);
            responseJson.put("result", true);
            responseJson.put("data",list);
            responseJson.put("message", "调用成功");
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
            responseJson.put("message", "调用失败");
        }
        return responseJson;
    }

    /**
     * 删除银行卡
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JSONObject delete(){
        JSONObject responseJson = new JSONObject();
        try {
            JSONObject requestJson = super.getRequestJsonParams();
            Preconditions.checkNotNull(requestJson.get("id"),"缺失参数id");
            long id = requestJson.getLong("id");
            log.info("银行卡参数："+requestJson.toString());
            int backRows = bindCardService.updateState(id);
            if(backRows>0){
                responseJson.put("result", true);
                responseJson.put("data",backRows);
                responseJson.put("message", "删除成功");
            }else{
                responseJson.put("result", false);
                responseJson.put("message", "删除失败");
            }
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
            responseJson.put("message", "删除失败");
        }
        return responseJson;
    }

}
