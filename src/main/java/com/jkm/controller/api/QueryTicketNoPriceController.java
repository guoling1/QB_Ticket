package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.service.QueryTicketNoPriceService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangbin on 2016/11/1.
 */
@Controller
@RequestMapping(value = "/queryTicketNoPrice")
public class QueryTicketNoPriceController extends BaseController {

    @Autowired
    private QueryTicketNoPriceService queryTicketNoPriceService;

    @ResponseBody
    @RequestMapping(value = "/query",method = RequestMethod.POST)
    public JSONObject query() throws Exception {

        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = super.getRequestJsonParams();
        /**
         * 获取请求参数
         */
        String partnerid = requestJson.getString("partnerid");
        String method = requestJson.getString("method");
        String train_date = requestJson.getString("train_date");
        String from_station = requestJson.getString("from_station");
        String to_station = requestJson.getString("to_station");
        String purpose_codes = requestJson.getString("purpose_codes");
        String needdistance = requestJson.getString("needdistance");

//        JSONObject requestJson = super.getRequestJsonParams();
        responseJson = this.queryTicketNoPriceService.query(partnerid, method, train_date, from_station, to_station, purpose_codes, needdistance);

        return responseJson;
    }
}
