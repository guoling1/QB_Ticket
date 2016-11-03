package com.jkm.service;

import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2016/11/2.
 */
public interface QueryTicketNoPriceService {

    /**
     * 查询余票（无价格）
     * @param method
     * @param train_date
     * @param from_station
     * @param to_station
     * @return
     */
    JSONObject queryTicket(String partnerid, String method, String from_station, String to_station, String train_date, String purpose_codes);
}
