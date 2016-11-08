package com.jkm.service;

import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2016/11/8.
 */
public interface QueryHistoryService {
    /**
     * 根据用户uid查询历史
     * @param uid
     */
    JSONObject queryHistory(String uid);

    /**
     * 插入更新
     * @param from_station
     * @param to_station
     * @param from_station_name
     * @param to_station_name
     * @return
     */
    JSONObject saveOrUpdate(String from_station, String to_station, String from_station_name, String to_station_name);


}
