package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.util.DESUtil;
import com.jkm.util.HttpClientUtil;
import net.sf.json.JSONObject;
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
        responseJson = HttpClientUtil.sendPost(jo,"http://trainorder.ws.hangtian123.com/cn_interface/trainAccount/validate");
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
}
