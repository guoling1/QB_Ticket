package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.service.QueryTicketNoPriceService;
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
@RequestMapping(value = "/queryTicketNoPrice")
public class QueryTicketNoPriceController extends BaseController {

    @Autowired
    private QueryTicketNoPriceService queryTicketNoPriceService;

    @ResponseBody
    @RequestMapping(value = "/query",method = RequestMethod.POST)
    public ResponseEntityBase<Object> query() throws IOException {

        JSONObject responseJson = new JSONObject();

        JSONObject requestJson = null;
//        try {
        requestJson = super.getRequestJsonParams();
        String partnerid = HySdkConstans.QUERY_PARTNER_ID;
        String method = "train_query_remain";
        String from_station = requestJson.getString("from_station");
        String to_station = requestJson.getString("to_station");
        String train_date = requestJson.getString("train_date");
        String purpose_codes = "ADULT";
        responseJson = this.queryTicketNoPriceService.queryTicket(partnerid, method, from_station, to_station, train_date, purpose_codes);
        final ResponseEntityBase<Object> results = new ResponseEntityBase<>();
        results.setData(responseJson.get("data"));


        return results;
    }
}
