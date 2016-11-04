package com.jkm.service;

import com.jkm.entity.PayResultRecord;

/**
 * Created by Administrator on 2016/11/4.
 */
public interface PayResultRecordService {
    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective(PayResultRecord record);
}
