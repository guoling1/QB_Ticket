package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.HttpClientUtil;
import com.jkm.util.HttpMethod;
import com.jkm.util.MD5Util;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangbin on 2016/11/1.
 */
@Controller
@RequestMapping(value = "/queryTicketNoPrice")
public class QueryTicketNoPriceController extends BaseController {

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public JSONObject query() throws Exception{
        JSONObject responseJson = new JSONObject();

        JSONObject requestJson = super.getRequestJsonParams();
        String method = requestJson.getString("method");
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        String reqtime = date.format(new Date());
        String from_station = requestJson.getString("from_station");
        String train_date = requestJson.getString("train_date");
        String partnerid = requestJson.getString("partnerid");
        String purpose_codes = requestJson.getString("purpose_codes");
        String to_station = requestJson.getString("to_station");

        JSONObject jsonObject = new JSONObject();
        String sign = MD5Util.MD5(partnerid + method + reqtime + MD5Util.MD5(HySdkConstans.SIGN_KEY));
        jsonObject.put("method", method);
        jsonObject.put("reqtime", reqtime);
        jsonObject.put("sign", sign);
        jsonObject.put("from_station", from_station);
        jsonObject.put("train_date", train_date);
        jsonObject.put("partnerid", partnerid);
        jsonObject.put("purpose_codes", purpose_codes);
        jsonObject.put("to_station", to_station);

        responseJson = HttpClientUtil.sendPost(jsonObject, "http://searchtrain.hangtian123.net/trainSearch");
        return responseJson;
    }
}
