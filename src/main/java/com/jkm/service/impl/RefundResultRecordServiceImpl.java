package com.jkm.service.impl;


import com.jkm.dao.RefundResultRecordDao;
import com.jkm.entity.RefundResultRecord;
import com.jkm.service.RefundResultRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundResultRecordServiceImpl implements RefundResultRecordService {
    @Autowired
    private RefundResultRecordDao refundResultRecordDao;
    @Override
    public int insertSelective(RefundResultRecord record) {
        return refundResultRecordDao.insertSelective(record);
    }
}
