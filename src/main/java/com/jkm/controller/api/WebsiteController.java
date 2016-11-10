package com.jkm.controller.api;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
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

/**
 * 12306网站信息
 */
@Controller
@RequestMapping(value = "/website")
public class WebsiteController extends BaseController {
    private static Logger log = Logger.getLogger(WebsiteController.class);

    @Autowired
    private WebsiteService websiteService;

    /**
     * 添加12306账号
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/addWebSite", method = RequestMethod.POST)
    public JSONObject addWebSite() throws Exception {
        JSONObject responseJson = new JSONObject();
        try{

            JSONObject requestJson = super.getRequestJsonParams();
            Preconditions.checkNotNull(requestJson.get("data"),"缺失参数data");
            Preconditions.checkNotNull(requestJson.get("uid"),"缺失参数uid");
            Preconditions.checkNotNull(requestJson.get("appid"),"缺失参数appid");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(requestJson.getString("data")), "data不能为空");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(requestJson.getString("uid")), "uid不能为空");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(requestJson.getString("appid")), "appid不能为空");
            String data = requestJson.getString("data");
            String uid = requestJson.getString("uid");
            String appid = requestJson.getString("appid");
            long backId = websiteService.addWebSite(data,super.getUid(appid,uid),appid);
            if(backId>0){
                responseJson.put("result", true);
                responseJson.put("data",backId);
                responseJson.put("message", "登录成功");
            }else{
                responseJson.put("result", false);
                responseJson.put("message", "登录失败");
            }
        }catch(Exception e){
            log.info("添加12306账号失败");
            responseJson.put("result", false);
            responseJson.put("message", e.getMessage());
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
            String uid = super.getUid(requestJson.getString("appid"),requestJson.getString("uid"));
            websiteService.importContacts(uid);
            responseJson.put("result", true);
            responseJson.put("message", "导入成功");
        } catch (Exception e) {
            log.info("导入失败");
            responseJson.put("result", false);
            responseJson.put("message", "导入成功");
        }
        return responseJson;
    }
}
