package com.jkm.controller.api;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jkm.controller.common.BaseController;
import com.jkm.entity.TbContactInfo;
import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.service.ContactInfoService;
import com.jkm.util.ValidationUtil;
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

            TbContactInfo ti = new TbContactInfo();
            ti.setUid(super.getUid(requestJson.getString("appid"),requestJson.getString("uid")));
            Preconditions.checkNotNull(requestJson.get("name"),"姓名不能为空");
            Preconditions.checkNotNull(requestJson.get("identy"),"证件号码不能为空");
            Preconditions.checkNotNull(requestJson.get("identyType"),"请选择证件类型");
            Preconditions.checkNotNull(requestJson.get("personType"),"请选择乘客类型");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(requestJson.getString("name")), "姓名不能为空");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(requestJson.getString("identy")), "证件号码不能为空");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(requestJson.getString("identyType")), "请选择证件类型");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(requestJson.getString("personType")), "请选择乘客类型");

            if("1".equals(requestJson.getString("identyType"))&&!ValidationUtil.isIdCard(requestJson.getString("identy"))){
                responseJson.put("result",false);
                responseJson.put("message","身份证号不正确");
                return responseJson;
            }

            if(requestJson.get("name")!=null){
                ti.setName(requestJson.getString("name"));
            }
            if(requestJson.get("sex")!=null){
                ti.setSex(requestJson.getInt("sex"));
            }
            if(requestJson.get("identyType")!=null){
                ti.setIdentyType(requestJson.getString("identyType"));
            }
            if(requestJson.get("identy")!=null){
                ti.setIdenty(UserBankCardSupporter.encryptCardId(requestJson.getString("identy")));
            }
            if(requestJson.get("tel")!=null){
                ti.setTel(requestJson.getString("tel"));
            }
            if(requestJson.get("personType")!=null){
                ti.setPersonType(requestJson.getInt("personType"));
            }

            JSONObject jo = contactInfoService.insert(ti);
            if(jo.getBoolean("result")){
                responseJson.put("result", true);
                responseJson.put("data",jo.getLong("data"));
                responseJson.put("message", jo.getString("message"));
            }else{
                responseJson.put("result", false);
                responseJson.put("message", jo.getString("message"));
            }
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
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
            Preconditions.checkArgument(!Strings.isNullOrEmpty(requestJson.getLong("id")+""), "缺失参数id");
            long id = requestJson.getLong("id");
            log.info("联系人参数："+requestJson.toString());
            TbContactInfo tbContactInfo = contactInfoService.selectOne(id);
            tbContactInfo.setIdenty(UserBankCardSupporter.decryptCardId(tbContactInfo.getIdenty()));
            responseJson.put("result", true);
            responseJson.put("data",tbContactInfo);
            responseJson.put("message", "查询成功");
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
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
            Preconditions.checkArgument(!Strings.isNullOrEmpty(requestJson.getLong("id")+""), "缺失参数id");
            long id = requestJson.getLong("id");
            log.info("联系人参数："+requestJson.toString());
            int rowNum = contactInfoService.updateStatus(id);
            responseJson.put("result", true);
            responseJson.put("data",rowNum);
            responseJson.put("message", "删除成功");
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
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
            TbContactInfo ti = new TbContactInfo();
            ti.setUid(super.getUid(requestJson.getString("appid"),requestJson.getString("uid")));
            if(requestJson.get("name")!=null){
                ti.setName(requestJson.getString("name"));
            }
            if(requestJson.get("sex")!=null){
                ti.setSex(requestJson.getInt("sex"));
            }
            if(requestJson.get("identyType")!=null){
                ti.setIdentyType(requestJson.getString("identyType"));
            }
            if(requestJson.get("identy")!=null){
                ti.setIdenty(UserBankCardSupporter.encryptCardId(requestJson.getString("identy")));
            }
            if(requestJson.get("tel")!=null){
                ti.setTel(requestJson.getString("tel"));
            }
            if(requestJson.get("personType")!=null){
                ti.setPersonType(requestJson.getInt("personType"));
            }
            if(requestJson.get("id")!=null){
                ti.setId(requestJson.getLong("id"));
            }
            int rowNum = contactInfoService.updateByPrimaryKeySelective(ti);
            responseJson.put("result", true);
            responseJson.put("data",rowNum);
            responseJson.put("message", "修改成功");
        }catch (Exception e){
            log.info(e.getMessage());
            responseJson.put("result", false);
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
            String uid = super.getUid(requestJson.getString("appid"),requestJson.getString("uid"));
            log.info("联系人参数："+requestJson.toString());
            List<TbContactInfo> list = contactInfoService.selectListByUid(uid);
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

}
