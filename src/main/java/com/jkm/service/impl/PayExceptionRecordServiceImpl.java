package com.jkm.service.impl;


import com.jkm.dao.PayExceptionRecordDao;
import com.jkm.entity.PayExceptionRecord;
import com.jkm.service.PayExceptionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayExceptionRecordServiceImpl implements PayExceptionRecordService{
    @Autowired
    private PayExceptionRecordDao payExceptionRecord;
    @Override
    public PayExceptionRecord selectByPrimaryKey(Long id) {
        return payExceptionRecord.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(PayExceptionRecord record) {
        return payExceptionRecord.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(PayExceptionRecord record) {
        return payExceptionRecord.updateByPrimaryKeySelective(record);
    }
}
