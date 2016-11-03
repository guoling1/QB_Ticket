package com.jkm.controller.api;

import com.google.common.base.Preconditions;
import com.jkm.controller.common.BaseController;
import com.jkm.service.WebsiteService;
import com.jkm.util.DESUtil;
import com.jkm.util.HttpClientUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@Controller
@RequestMapping(value = "/website")
public class WebsiteController extends BaseController {
    private static Logger log = Logger.getLogger(WebsiteController.class);

    @Autowired
    private WebsiteService websiteService;
    /**
     * 增加和修改金开门网站常用联系人接口
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrUpdateOfJkm", method = RequestMethod.POST)
    public JSONObject saveOrUpdateOfJkm() throws Exception {
        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = super.getRequestJsonParams();
        String data = requestJson.getString("data");
        String accountversion = requestJson.getString("accountversion");
        data = DESUtil.encrypt(data);
        JSONObject jo = new JSONObject();
        jo.put("data",data);
        jo.put("accountversion",accountversion);
        responseJson = HttpClientUtil.sendPost(jo,"http://trainorder.ws.hangtian123.com/trainAccount/contact/saveOrUpdate");
        String tempData = DESUtil.decrypt(responseJson.getString("data"));
        responseJson.put("data",tempData);
        return responseJson;
    }

    /**
     * 添加12306账号
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/addWebSite", method = RequestMethod.POST)
    public JSONObject addWebSite() throws Exception {
        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = super.getRequestJsonParams();
        Preconditions.checkNotNull(requestJson.get("data"),"缺失参数data");
        Preconditions.checkNotNull(requestJson.get("uid"),"缺失参数uid");
        Preconditions.checkNotNull(requestJson.get("appid"),"缺失参数appid");
        String data = requestJson.getString("data");
        String uid = requestJson.getString("uid");
        String appid = requestJson.getString("appid");
        long backId = websiteService.addWebSite(data,uid,appid);
        if(backId>0){
            responseJson.put("result", true);
            responseJson.put("data",backId);
            responseJson.put("message", "登录成功");
        }else{
            responseJson.put("result", false);
            responseJson.put("data", null);
            responseJson.put("message", "登录失败");
        }
        return responseJson;
    }
    /**
     * 导入12306联系人
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/importContacts", method = RequestMethod.POST)
    public JSONObject importContacts(){
        JSONObject responseJson = new JSONObject();
        try {
            JSONObject requestJson = super.getRequestJsonParams();
            Preconditions.checkNotNull(requestJson.get("uid"),"缺失参数uid");
            String uid = requestJson.getString("uid");
            websiteService.importContacts(uid);
            responseJson.put("result", true);
            responseJson.put("data",null);
            responseJson.put("message", "导入成功");
        } catch (IOException e) {
            log.info("导入失败");
            responseJson.put("result", false);
            responseJson.put("data",null);
            responseJson.put("message", "导入失败");
        }
        return responseJson;
    }
}
