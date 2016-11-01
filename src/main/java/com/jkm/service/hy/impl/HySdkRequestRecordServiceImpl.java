package com.jkm.service.hy.impl;

import com.jkm.dao.HyChannelRequestRecordDao;
import com.jkm.dao.YsChannelRequestRecordDao;
import com.jkm.entity.HyChannelRequestRecord;
import com.jkm.entity.YsChannelRequestRecord;
import com.jkm.service.hy.HySdkRequestRecordService;
import com.jkm.service.ys.YsSdkRequestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-10-28.
 */
@Service
public class HySdkRequestRecordServiceImpl implements HySdkRequestRecordService {

    @Autowired
    private HyChannelRequestRecordDao hyChannelRequestRecordDao;

    @Override
    public void record(HyChannelRequestRecord record) {
        this.hyChannelRequestRecordDao.insert(record);
    }
}
