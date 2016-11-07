package com.jkm.service.impl;

import com.jkm.dao.GetParamRecordDao;
import com.jkm.entity.GetParamRecord;
import com.jkm.service.GetParamRecordService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/11/7.
 */
public class GetParamRecordServiceImpl implements GetParamRecordService {
    private final Logger logger = LoggerFactory.getLogger(GetParamRecordServiceImpl.class);

    @Autowired
    private GetParamRecordDao getParamRecordDao;
    @Override
    public GetParamRecord selectByPrimaryKey(@Param("id") Long id) {
        return getParamRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public int deleteByPrimaryKey(@Param("id") Long id) {
        return getParamRecordDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(GetParamRecord record) {
        return getParamRecordDao.insert(record);
    }

    @Override
    public int insertSelective(GetParamRecord record) {
        return getParamRecordDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(GetParamRecord record) {
        return getParamRecordDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(GetParamRecord record) {
        return getParamRecordDao.updateByPrimaryKey(record);
    }

    @Override
    public int selectByCondition(GetParamRecord record) {
        return getParamRecordDao.selectByCondition(record);
    }
}
