package com.jkm.service;

import com.jkm.entity.QueryHistory;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */
public interface QueryHistoryService {
    /**
     * 根据用户uid查询历史
     * @param uid
     */
    List<QueryHistory> queryHistory(String uid);

    /**
     * 插入
     * @param uid
     * @param from_station
     * @param to_station
     * @param from_station_name
     * @param to_station_name
     */
    void save(String uid, String from_station, String to_station, String from_station_name, String to_station_name);


    /**
     * 更新
     * @param uid
     * @param from_station
     * @param to_station
     * @param from_station_name
     * @param to_station_name
     */
    void update(String uid, String from_station, String to_station, String from_station_name, String to_station_name);



}
