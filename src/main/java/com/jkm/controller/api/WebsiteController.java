package com.jkm.controller.api;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.controller.helper.request.RequestAddWebSite;
import com.jkm.controller.helper.request.RequestUserInfo;
import com.jkm.service.WebsiteService;
import com.jkm.util.DESUtil;
import com.jkm.util.HttpClientUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntityBase<Long> addWebSite(@RequestBody RequestAddWebSite req) throws Exception {
        ResponseEntityBase<Long> responseEntityBase = new ResponseEntityBase<Long>();
        try{

            JSONObject requestJson = super.getRequestJsonParams();
            Preconditions.checkArgument(!Strings.isNullOrEmpty(req.getUid()), "uid不能为空");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(req.getAppid()), "appid不能为空");

            JSONObject backObject = websiteService.addWebSite(req.getData(),super.getUid(req.getAppid(),req.getUid()),req.getAppid());
            if(backObject.getBoolean("result")&&backObject.getLong("data")>0){
                responseEntityBase.setMessage("登录成功");
                responseEntityBase.setData(backObject.getLong("data"));
            }else{
                responseEntityBase.setCode(400);
                responseEntityBase.setMessage(backObject.getString("message"));
            }
        }catch(Exception e){
            log.error("添加12306账号失败",e);
            if(e.getMessage()==null){
                responseEntityBase.setMessage("添加账号失败");
            }else{
                responseEntityBase.setMessage(e.getMessage().toString());
            }
            responseEntityBase.setCode(500);
        }
        return responseEntityBase;
    }
    /**
     * 导入12306联系人
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/importContacts", method = RequestMethod.POST)
    public ResponseEntityBase importContacts(){
        ResponseEntityBase responseEntityBase = new ResponseEntityBase();
        try {
            JSONObject requestJson = super.getRequestJsonParams();
            String uid = super.getUid(requestJson.get("appid"),requestJson.get("uid"));
            websiteService.importContacts(uid);
            responseEntityBase.setMessage("导入成功");
        } catch (Exception e) {
            log.error("导入失败",e);
            if(e.getMessage()==null){
                responseEntityBase.setMessage("导入异常");
            }else{
                responseEntityBase.setMessage(e.getMessage().toString());
            }
            responseEntityBase.setCode(500);
        }
        return responseEntityBase;
    }
}
