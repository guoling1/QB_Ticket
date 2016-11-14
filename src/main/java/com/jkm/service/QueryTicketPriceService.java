package com.jkm.service;

import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2016/11/3.
 */
public interface QueryTicketPriceService {
    /**
     * 查询余票接口（有价格）
     * @param method
     * @param train_date
     * @param from_station
     * @param to_station
     * @return
     */
    JSONObject queryTicket(String uid,String partnerid, String method, String from_station,String to_station,String from_station_name,String to_station_name, String train_date, String purpose_codes);
}
