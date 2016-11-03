package com.jkm.service;

import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2016/11/2.
 */
public interface QueryTicketNoPriceService {

    /**
     * 查询余票（无价格）
     * @param partnerid
     * @param method
     * @param train_date
     * @param from_station
     * @param to_station
     * @param purpose_codes
     * @param needdistance
     * @return
     */
    JSONObject queryTicket(String partnerid, String method, String train_date, String from_station, String to_station, String purpose_codes, String needdistance);
}
