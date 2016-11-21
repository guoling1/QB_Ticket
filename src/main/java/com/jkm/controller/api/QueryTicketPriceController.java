package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.service.QueryTicketPriceService;
import com.jkm.service.hy.helper.HySdkConstans;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/queryTicketPrice")
public class QueryTicketPriceController extends BaseController {

    @Autowired
    private QueryTicketPriceService queryTicketPriceService;


    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntityBase<JSONArray> query() {
        final ResponseEntityBase<JSONArray> results = new ResponseEntityBase<JSONArray>();
        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = null;
        try {
            requestJson = super.getRequestJsonParams();
            String uid = super.getUid(requestJson.get("appid"), requestJson.get("uid"));
            String partnerid = HySdkConstans.QUERY_PARTNER_ID;
            String method = "train_query";
            String from_station = requestJson.getString("from_station");
            String to_station = requestJson.getString("to_station");
            String from_station_name = requestJson.getString("from_station_name");
            String to_station_name = requestJson.getString("to_station_name");
            String train_date = requestJson.getString("train_date");
            String purpose_codes = "ADULT";
            responseJson = this.queryTicketPriceService.queryTicket(uid, partnerid, method, from_station, to_station, from_station_name, to_station_name, train_date, purpose_codes);

            JSONArray arrayResult = new JSONArray();
            JSONArray ja = responseJson.getJSONArray("data");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            Date date = new Date();
            String now = sdf.format(date.getTime() + 30 * 60 * 1000);
            String now1 = now.substring(8, 12);
            String times = now.substring(0, 8);
            long times1 = Long.parseLong(times);
            long now2 = Long.parseLong(now1);
            if (ja.size() > 0) {
                for (int i = 0; i < ja.size(); i++) {
                    String starTime = ja.getJSONObject(i).getString("start_time");
                    String trainStartDate = ja.getJSONObject(i).getString("train_start_date");
                    //// TODO: 2016/11/17 将字符串转为long
                    starTime = starTime.replace(":", "");
                    trainStartDate = trainStartDate.replace("-", "");
                    long trainStartDates = Long.parseLong(trainStartDate);
                    long startLongTime = Long.parseLong(starTime);
                    if (trainStartDates == times1) {
                        if (startLongTime > now2) {
                            arrayResult.add(ja.getJSONObject(i));
                        }

                    } else {
                        arrayResult.add(ja.getJSONObject(i));
                    }
                }
            }

            results.setData(arrayResult);
            return results;

        } catch (Exception e) {
            e.printStackTrace();
            results.setCode(-1);
            results.setMessage("没有符合条件的车次信息");
        }
        return results;
    }

}
