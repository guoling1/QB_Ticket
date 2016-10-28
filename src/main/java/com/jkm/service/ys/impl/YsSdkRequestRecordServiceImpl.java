package com.jkm.service.ys.impl;

import com.jkm.dao.YsChannelRequestRecordDao;
import com.jkm.entity.YsChannelRequestRecord;
import com.jkm.service.ys.YsSdkRequestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-10-28.
 */
@Service
public class YsSdkRequestRecordServiceImpl implements YsSdkRequestRecordService {

    @Autowired
    private YsChannelRequestRecordDao ysChannelRequestRecordDao;

    @Override
    public void record(YsChannelRequestRecord record) {
        this.ysChannelRequestRecordDao.insert(record);
    }
}
