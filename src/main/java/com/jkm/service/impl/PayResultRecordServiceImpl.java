package com.jkm.service.impl;


import com.jkm.dao.PayResultRecordDao;
import com.jkm.entity.PayResultRecord;
import com.jkm.service.PayResultRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayResultRecordServiceImpl implements PayResultRecordService{
    @Autowired
    private PayResultRecordDao payResultRecordDao;
    @Override
    public int insertSelective(PayResultRecord record) {
        return payResultRecordDao.insertSelective(record);
    }
}
