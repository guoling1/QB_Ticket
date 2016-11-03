package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.service.QueryTicketPriceService;
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
@RequestMapping(value = "/queryTicketPrice")
public class QueryTicketPriceController extends BaseController {

    @Autowired
    private QueryTicketPriceService queryTicketPriceService;

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public JSONObject query() throws Exception{
        JSONObject responseJson = new JSONObject();

        JSONObject requestJson = super.getRequestJsonParams();
        String method = requestJson.getString("method");
        String from_station = requestJson.getString("from_station");
        String train_date = requestJson.getString("train_date");
        String partnerid = requestJson.getString("partnerid");
        String purpose_codes = requestJson.getString("purpose_codes");
        String to_station = requestJson.getString("to_station");
        String needdistance = requestJson.getString("needdistance");
        responseJson = this.queryTicketPriceService.queryTicket(partnerid, method, train_date,
                from_station, to_station, purpose_codes, needdistance);

        return responseJson;
    }
}
