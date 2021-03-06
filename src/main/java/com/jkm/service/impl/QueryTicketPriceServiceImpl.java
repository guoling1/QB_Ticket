package com.jkm.service.impl;

import com.jkm.service.QueryHistoryService;
import com.jkm.service.QueryTicketPriceService;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.HttpMethod;
import com.jkm.util.MD5Util;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/3.
 */
@Service
public class QueryTicketPriceServiceImpl implements QueryTicketPriceService {
    @Autowired
    private QueryHistoryService queryHistoryService;
    @Override
    public JSONObject queryTicket(String uid, String partnerid, String method, String from_station, String to_station,
                                  String from_station_name, String to_station_name, String train_date, String purpose_codes) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        String reqtime = sdf.format(now);
        JSONObject jsonObject = new JSONObject();
        try {
            String sign = MD5Util.MD5(partnerid + method + reqtime + MD5Util.MD5(HySdkConstans.QUERY_SIGN_KEY));
            jsonObject.put("partnerid", partnerid);
            jsonObject.put("method", method);
            jsonObject.put("reqtime", reqtime);
            jsonObject.put("sign", sign);
            jsonObject.put("train_date", train_date);
            jsonObject.put("from_station", from_station);
            jsonObject.put("to_station", to_station);
            jsonObject.put("purpose_codes", purpose_codes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject responseJson = new JSONObject();
        responseJson = HttpMethod.httpClient(jsonObject, "http://searchtrain.hangtian123.net/trainSearch");
        queryHistoryService.save(uid,from_station,to_station,from_station_name,to_station_name);
        return responseJson;
    }

}
