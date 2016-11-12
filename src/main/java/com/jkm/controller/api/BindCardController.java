package com.jkm.controller.api;

import com.google.common.base.Preconditions;
import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
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
    public ResponseEntityBase<Long> bind(){
        ResponseEntityBase<Long> responseEntityBase = new ResponseEntityBase<Long>();
        try{
            JSONObject requestJson = super.getRequestJsonParams();
            log.info("银行卡参数："+requestJson.toString());
            requestJson.put("uid",super.getUid(requestJson.getString("appid"),requestJson.getString("uid")));
            JSONObject jo = bindCardService.insertSelective(requestJson);
            if(jo.getBoolean("success")){
                responseEntityBase.setMessage(jo.getString("message"));
                responseEntityBase.setData(jo.getLong("data"));
            }else{
                responseEntityBase.setCode(400);
                responseEntityBase.setMessage(jo.getString("message"));
            }
        }catch (Exception e){
            log.info("绑定银行卡异常");
            responseEntityBase.setCode(500);
            responseEntityBase.setMessage(e.getMessage());
        }
        return responseEntityBase;
    }

    /**
     * 银行卡列表
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntityBase<JSONObject> list(){
        ResponseEntityBase<JSONObject> responseEntityBase = new ResponseEntityBase<JSONObject>();
        try {
            JSONObject requestJson = super.getRequestJsonParams();
            String uid = super.getUid(requestJson.getString("appid"),requestJson.getString("uid"));
            log.info("银行卡参数："+requestJson.toString());
            JSONObject list = bindCardService.selectByUid(uid);
            responseEntityBase.setData(list);
        }catch (Exception e){
            log.info("银行卡列表异常");
            responseEntityBase.setCode(500);
            responseEntityBase.setMessage("fail");
        }
        return responseEntityBase;
    }

    /**
     * 删除银行卡
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntityBase<Integer> delete(){
        ResponseEntityBase<Integer> responseEntityBase = new ResponseEntityBase<Integer>();
        try {
            JSONObject requestJson = super.getRequestJsonParams();
            Preconditions.checkNotNull(requestJson.get("id"),"缺失参数id");
            long id = requestJson.getLong("id");
            log.info("银行卡参数："+requestJson.toString());
            int backRows = bindCardService.updateState(id);
            if(backRows>0){
                responseEntityBase.setMessage("删除成功");
                responseEntityBase.setData(backRows);
            }else{
                responseEntityBase.setCode(400);
                responseEntityBase.setMessage("删除失败");
            }
        }catch (Exception e){
            log.info("删除银行卡异常");
            responseEntityBase.setCode(500);
            responseEntityBase.setMessage("删除失败");
        }
        return responseEntityBase;
    }

}
