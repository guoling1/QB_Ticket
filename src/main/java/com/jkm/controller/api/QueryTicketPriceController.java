package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.service.QueryHistoryService;
import com.jkm.service.QueryTicketPriceService;
import com.jkm.service.hy.helper.HySdkConstans;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by zhangbin on 2016/11/1.
 */
@Controller
@RequestMapping(value = "/queryTicketPrice")
public class QueryTicketPriceController extends BaseController {

    @Autowired
    private QueryTicketPriceService queryTicketPriceService;
    @Autowired
    private QueryHistoryService queryHistoryService;

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntityBase<Object> query() throws IOException {

        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = null;
        requestJson = super.getRequestJsonParams();
        String partnerid = HySdkConstans.QUERY_PARTNER_ID;
        String method = "train_query";
        String from_station = requestJson.getString("from_station");
        String to_station = requestJson.getString("to_station");
        String train_date = requestJson.getString("train_date");
        String purpose_codes = "ADULT";
        responseJson = this.queryTicketPriceService.queryTicket(partnerid, method, from_station, to_station, train_date, purpose_codes);
        final ResponseEntityBase<Object> results = new ResponseEntityBase<>();
        results.setData(responseJson.get("data"));

        return results;
    }

    @ResponseBody
    @RequestMapping(value = "/queryHistory", method = RequestMethod.POST)
    public ResponseEntityBase<Object> queryHistory() throws IOException {
        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = null;
        requestJson = super.getRequestJsonParams();
        String uid = super.getUid(requestJson.getString("appid"),requestJson.getString("uid"));
        requestJson = queryHistoryService.queryHistory(uid);
        if (requestJson.isEmpty()){
            String from_station = requestJson.getString("fromStation");
            String to_station = requestJson.getString("toStation");
            String from_station_name = requestJson.getString("fromStationName");
            String to_station_name = requestJson.getString("toStationName");
            this.queryHistoryService.saveOrUpdate(from_station,to_station,from_station_name,to_station_name);
        }
        final ResponseEntityBase<Object> results = new ResponseEntityBase<>();
        results.setData(requestJson);

        return results;
    }
}
