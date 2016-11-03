package com.jkm.service;

import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2016/11/3.
 */
public interface QueryTicketPriceService {
    /**
     * 查询余票接口（有价格）
     * @param partnerid
     * @param method
     * @param train_date
     * @param from_station
     * @param to_station
     * @param purpose_codes
     * @param needdistance
     * @return
     */
    JSONObject queryTicket(String partnerid, String method, String train_date, String from_station,
                     String to_station, String purpose_codes, String needdistance);
}
