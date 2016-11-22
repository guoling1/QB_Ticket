package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.entity.QueryHistory;
import com.jkm.service.QueryHistoryService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangbin on 2016/11/9.
 */
@Controller
@RequestMapping(value = "/queryHistory")
public class QueryHistoryController extends BaseController {

    @Autowired
    private QueryHistoryService queryHistoryService;

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntityBase<List<QueryHistory>> query() {

        ResponseEntityBase<List<QueryHistory>> results = new ResponseEntityBase<List<QueryHistory>>();

        try {
            JSONObject requestJson = super.getRequestJsonParams();
            String uid = super.getUid(requestJson.get("appid"),requestJson.get("uid"));
            List<QueryHistory> list = this.queryHistoryService.queryHistory(uid);
            results.setData(list);
        } catch (Exception e) {
            results.setCode(500);
            results.setMessage("系统异常");
        }

        return results;
    }
}
