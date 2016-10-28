package com.jkm.service.ys;

import com.jkm.entity.YsChannelRequestRecord;

/**
 * Created by yuxiang on 2016-10-28.
 */
public interface YsSdkRequestRecordService {

    /**
     * 记录请求
     * @param record
     */
    void record(YsChannelRequestRecord record);
}
