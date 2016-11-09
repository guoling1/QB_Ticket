package com.jkm.service.impl;

import com.jkm.dao.QueryHistoryDao;
import com.jkm.service.QueryHistoryService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/11/8.
 */
@Service
public class QueryHistoryServiceImpl implements QueryHistoryService {

    @Autowired
    private QueryHistoryDao queryHistoryDao;


    @Override
    public JSONObject queryHistory(String uid) {
//        return this.queryHistoryDao.queryHistory(uid);
        return null;
    }

    @Override
    public JSONObject saveOrUpdate(String from_station, String to_station, String from_station_name, String to_station_name) {
        return null;
    }
}
