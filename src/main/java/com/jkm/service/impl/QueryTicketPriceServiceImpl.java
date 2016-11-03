package com.jkm.service.impl;

import com.jkm.service.QueryTicketPriceService;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.HttpMethod;
import com.jkm.util.MD5Util;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/3.
 */
@Service
public class QueryTicketPriceServiceImpl implements QueryTicketPriceService {
    @Override
    public JSONObject queryTicket(String partnerid, String method, String train_date,
            String from_station, String to_station, String purpose_codes, String needdistance) {

        JSONObject jsonObject = null;
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        String reqtime = date.format(new Date());
        jsonObject = new JSONObject();
        String sign = null;
        try {
            sign = MD5Util.MD5(partnerid + method + reqtime + MD5Util.MD5(HySdkConstans.SIGN_KEY));
            jsonObject.put("sign", sign);
            jsonObject.put("partnerid", partnerid);
            jsonObject.put("method", method);
            jsonObject.put("from_station", from_station);
            jsonObject.put("to_station", to_station);
            jsonObject.put("reqtime", reqtime);
            jsonObject.put("train_date", train_date);
            jsonObject.put("purpose_codes", purpose_codes);
            jsonObject.put("needdistance", needdistance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject responseJson = new JSONObject();
        responseJson = HttpMethod.httpClient(jsonObject, "http://searchtrain.hangtian123.net/trainSearch");
        return responseJson;
    }
}
