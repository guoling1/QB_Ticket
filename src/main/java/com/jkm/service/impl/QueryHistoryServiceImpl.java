package com.jkm.service.impl;

import com.jkm.dao.QueryHistoryDao;
import com.jkm.entity.QueryHistory;
import com.jkm.service.QueryHistoryService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2016/11/8.
 */
@Service
public class QueryHistoryServiceImpl implements QueryHistoryService {

    @Autowired
    private QueryHistoryDao queryHistoryDao;


    /**
     * 查询记录
     * @param uid
     * @return
     */
    @Override
    public List<QueryHistory> queryHistory(String uid) {
        List<QueryHistory> list = this.queryHistoryDao.queryHistory(uid);

        return list;
    }

    /**
     * 插入
     * @param uid
     * @param from_station
     * @param to_station
     * @param from_station_name
     * @param to_station_name
     */
    @Override
    public void save(String uid, String from_station, String to_station, String from_station_name, String to_station_name) {
        JSONObject jsonObject = null;
        JSONObject responseJson = new JSONObject();
        jsonObject = new JSONObject();
        jsonObject.put("uid",uid);
        jsonObject.put("from_station",from_station);
        jsonObject.put("to_station",to_station);
        jsonObject.put("from_station_name",from_station_name);
        jsonObject.put("to_station_name",to_station_name);
        this.queryHistoryDao.save(jsonObject);

    }

    /**
     * 更新
     * @param uid
     * @param from_station
     * @param to_station
     * @param from_station_name
     * @param to_station_name
     */
    @Override
    public void update(String uid, String from_station, String to_station, String from_station_name, String to_station_name) {

        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        String update_time = date.format(new Date());
        jsonObject.put("uid",uid);
        jsonObject.put("from_station",from_station);
        jsonObject.put("to_station",to_station);
        jsonObject.put("from_station_name",from_station_name);
        jsonObject.put("to_station_name",to_station_name);
        jsonObject.put("update_time",update_time);
        this.queryHistoryDao.update(jsonObject);
    }



}
