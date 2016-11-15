package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.service.DeleteHistoryService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by zhangbin on 2016/11/15.
 */
@Controller
@RequestMapping(value = "/deleteHistory")
public class DeleteHistoryContrller extends BaseController {
    private static Logger log = Logger.getLogger(ContactInfoController.class);
    @Autowired
    private DeleteHistoryService deleteHistoryService;

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete() throws IOException {
//        final ResponseEntityBase<Object> results = new ResponseEntityBase<>();
//        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = super.getRequestJsonParams();
        String uid = super.getUid(requestJson.getString("appid"),requestJson.getString("uid"));
        this.deleteHistoryService.delete(uid);
//        results.setData(responseJson);
//        results.setMessage("删除成功");

    }
}
