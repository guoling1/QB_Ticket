package com.jkm.controller.api;

import com.google.common.base.Preconditions;
import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.entity.UserInfo;
import com.jkm.service.BindCardService;
import com.jkm.service.UserInfoService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping(value = "/userInfo")
public class UserInfoController extends BaseController {
    private static Logger log = Logger.getLogger(UserInfoController.class);
    @Autowired
    private UserInfoService userInfoService;
    /**
     * 绑定银行卡
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/findPhone", method = RequestMethod.POST)
    public ResponseEntityBase<String> findPhone(){
        ResponseEntityBase<String> responseEntityBase = new ResponseEntityBase<String>();
        try{
            JSONObject requestJson = super.getRequestJsonParams();
            String uid = super.getUid(requestJson.getString("appid"),requestJson.getString("uid"));
            UserInfo u = userInfoService.selectByUid(uid);
            if(u==null){
                responseEntityBase.setData("");
            }else{
                responseEntityBase.setMessage(u.getPhone());
            }
        }catch (Exception e){
            log.info("查找用户手机号异常");
            log.info(e.getStackTrace());
            responseEntityBase.setCode(500);
            responseEntityBase.setMessage("fail");
        }
        return responseEntityBase;
    }


}
