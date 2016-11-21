package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.entity.QueryHistory;
import com.jkm.service.DeleteHistoryService;
import com.jkm.service.QueryHistoryService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangbin on 2016/11/15.
 */
@Controller
@RequestMapping(value = "/deleteHistory")
public class DeleteHistoryContrller extends BaseController {
    private static Logger log = Logger.getLogger(ContactInfoController.class);
    @Autowired
    private DeleteHistoryService deleteHistoryService;
    @Autowired
    private QueryHistoryService queryHistoryService;

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntityBase<List<QueryHistory>> delete() throws IOException {
        ResponseEntityBase<List<QueryHistory>> results = new ResponseEntityBase<List<QueryHistory>>();
        JSONObject requestJson = super.getRequestJsonParams();
        String uid = super.getUid(requestJson.getString("appid"),requestJson.getString("uid"));
        this.deleteHistoryService.delete(uid);
        List<QueryHistory> list = this.queryHistoryService.queryHistory(uid);
        results.setData(list);
        return results;

    }
}
