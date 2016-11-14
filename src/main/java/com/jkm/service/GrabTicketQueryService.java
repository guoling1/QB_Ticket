package com.jkm.service;

import net.sf.json.JSONObject;

/**
 * Created by zhangbin on 2016/11/2.
 */
public interface GrabTicketQueryService {

    /**
     * 抢票查询
     * @param method
     * @param train_date
     * @param from_station
     * @param to_station
     * @return
     */
    JSONObject grabTicketQuery(String partnerid, String method, String from_station, String to_station, String train_date, String purpose_codes);
}
