package com.jkm.service.impl;


import com.jkm.dao.PayExceptionRecordDao;
import com.jkm.entity.PayExceptionRecord;
import com.jkm.service.PayExceptionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayExceptionRecordServiceImpl implements PayExceptionRecordService{
    @Autowired
    private PayExceptionRecordDao payExceptionRecordDao;
    @Override
    public PayExceptionRecord selectByPrimaryKey(Long id) {
        return payExceptionRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(PayExceptionRecord record) {
        return payExceptionRecordDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(PayExceptionRecord record) {
        return payExceptionRecordDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insertByCondition(String orderId, String reqSn, String type) {
        PayExceptionRecord payExceptionRecord = new PayExceptionRecord();
        payExceptionRecord.setStatus(0);
        payExceptionRecord.setPayChannel("fastpay");
        payExceptionRecord.setOrderId(orderId);
        payExceptionRecord.setReqSn(reqSn);
        payExceptionRecord.setType(type);
        return payExceptionRecordDao.insertSelective(payExceptionRecord);
    }
}
