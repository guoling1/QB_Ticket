package com.jkm.service.hy;

import com.jkm.entity.HyChannelRequestRecord;

/**
 * Created by yuxiang on 2016-10-28.
 */
public interface HySdkRequestRecordService {

    /**
     * 记录请求
     * @param record
     */
    void record(HyChannelRequestRecord record);
}
