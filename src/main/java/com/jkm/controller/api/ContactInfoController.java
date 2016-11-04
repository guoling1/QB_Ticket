package com.jkm.controller.api;

import com.google.common.base.Preconditions;
import com.jkm.controller.common.BaseController;
import com.jkm.entity.BindCard;
import com.jkm.entity.TbContactInfo;
import com.jkm.service.ContactInfoService;
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
@RequestMapping(value = "/contactInfo")
public class ContactInfoController extends BaseController {
    private static Logger log = Logger.getLogger(ContactInfoController.class);
    @Autowired
    private ContactInfoService contactInfoService;
    /**
     * 添加联系人信息
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JSONObject bind(){
        JSONObject responseJson = new JSONObject();
        try{
            JSONObject requestJson = super.getRequestJsonParams();
            log.info("联系人参数："+requestJson.toString());
            JSONObject jo = contactInfoService.insert((TbContactInfo)JSONObject.toBean(requestJson));
            if(jo.getBoolean("success")){
                responseJson.put("result", true);
                responseJson.put("data",jo.getLong("data"));
                responseJson.put("message", jo.getString("message"));
            }else{
                responseJson.put("result", false);
                responseJson.put("data", null);
                responseJson.put("message", jo.getString("message"));
            }
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
            responseJson.put("data", null);
            responseJson.put("message", "添加异常");
        }
        return responseJson;
    }
    /**
     * 根据id查询用户信息
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/selectOne", method = RequestMethod.POST)
    public JSONObject selectOne(){
        JSONObject responseJson = new JSONObject();
        try{
            JSONObject requestJson = super.getRequestJsonParams();
            Preconditions.checkNotNull(requestJson.get("id"),"缺失参数id");
            long id = requestJson.getLong("id");
            log.info("联系人参数："+requestJson.toString());
            TbContactInfo tbContactInfo = contactInfoService.selectOne(id);
            responseJson.put("result", true);
            responseJson.put("data",tbContactInfo);
            responseJson.put("message", "查询成功");
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
            responseJson.put("data", null);
            responseJson.put("message", "查询异常");
        }
        return responseJson;
    }

    /**
     * 删除联系人
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JSONObject delete(){
        JSONObject responseJson = new JSONObject();
        try{
            JSONObject requestJson = super.getRequestJsonParams();
            Preconditions.checkNotNull(requestJson.get("id"),"缺失参数id");
            long id = requestJson.getLong("id");
            log.info("联系人参数："+requestJson.toString());
            int rowNum = contactInfoService.updateStatus(id);
            responseJson.put("result", true);
            responseJson.put("data",rowNum);
            responseJson.put("message", "删除成功");
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
            responseJson.put("data", null);
            responseJson.put("message", "删除异常");
        }
        return responseJson;
    }

    /**
     * 修改联系人
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject update(){
        JSONObject responseJson = new JSONObject();
        try{
            JSONObject requestJson = super.getRequestJsonParams();
            log.info("联系人参数："+requestJson.toString());
            int rowNum = contactInfoService.updateByPrimaryKeySelective((TbContactInfo)JSONObject.toBean(requestJson));
            responseJson.put("result", true);
            responseJson.put("data",rowNum);
            responseJson.put("message", "修改成功");
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
            responseJson.put("data", null);
            responseJson.put("message", "修改异常");
        }
        return responseJson;
    }

    /**
     * 联系人列表
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public JSONObject list(){
        JSONObject responseJson = new JSONObject();
        try {
            JSONObject requestJson = super.getRequestJsonParams();
            Preconditions.checkNotNull(requestJson.get("uid"),"缺失参数uid");
            String uid = requestJson.getString("uid");
            log.info("联系人参数："+requestJson.toString());
            List<TbContactInfo> list = contactInfoService.selectListByUid(uid);
            responseJson.put("result", true);
            responseJson.put("data",list);
            responseJson.put("message", "调用成功");
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
            responseJson.put("data",null);
            responseJson.put("message", "调用失败");
        }
        return responseJson;
    }

}
