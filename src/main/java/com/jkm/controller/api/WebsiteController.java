package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.service.WebsiteService;
import com.jkm.util.DESUtil;
import com.jkm.util.HttpClientUtil;
import com.jkm.util.HttpMethod;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/11/1.
 */
@Controller
@RequestMapping(value = "/website")
public class WebsiteController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(WebsiteController.class);

    @Autowired
    private WebsiteService websiteService;
    /**
     * 12306账号验证接口
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public JSONObject validate() throws Exception {
        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = super.getRequestJsonParams();
        String data = requestJson.getString("data");
        String accountversion = requestJson.getString("accountversion");
        data = DESUtil.encrypt(data);
        JSONObject jo = new JSONObject();
        jo.put("data",data);
        jo.put("accountversion",accountversion);
        responseJson = HttpMethod.httpClient(jo,"http://trainorder.ws.hangtian123.com/cn_interface/trainAccount/validate");
        return responseJson;
    }
    /**
     * 查询常用联系人接口
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public JSONObject query() throws Exception {
        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = super.getRequestJsonParams();
        String data = requestJson.getString("data");
        String accountversion = requestJson.getString("accountversion");
        data = DESUtil.encrypt(data);
        JSONObject jo = new JSONObject();
        jo.put("data",data);
        jo.put("accountversion",accountversion);
        responseJson = HttpClientUtil.sendPost(jo,"http://trainorder.ws.hangtian123.com/cn_interface/trainAccount/contact/query");
        String tempData = DESUtil.decrypt(responseJson.getString("data"));
        responseJson.put("data",tempData);
        return responseJson;
    }
    /**
     * 增加和修改常用联系人接口
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public JSONObject saveOrUpdate() throws Exception {
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
     * 删除常用联系人接口
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JSONObject delete() throws Exception {
        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = super.getRequestJsonParams();
        String data = requestJson.getString("data");
        String accountversion = requestJson.getString("accountversion");
        data = DESUtil.encrypt(data);
        JSONObject jo = new JSONObject();
        jo.put("data",data);
        jo.put("accountversion",accountversion);
        responseJson = HttpClientUtil.sendPost(jo,"http://trainorder.ws.hangtian123.com/trainAccount/contact/delete");
        String tempData = DESUtil.decrypt(responseJson.getString("data"));
        responseJson.put("data",tempData);
        return responseJson;
    }

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
            responseJson.put("data", "");
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
    public JSONObject importContacts() throws Exception {
        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = super.getRequestJsonParams();
        String uid = requestJson.getString("uid");
        String appid = requestJson.getString("appid");
        websiteService.importContacts(uid,appid);
        return responseJson;
    }
}
