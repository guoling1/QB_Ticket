package com.jkm.service.impl;


import com.jkm.dao.RefundExceptionRecordDao;
import com.jkm.entity.RefundExceptionRecord;
import com.jkm.service.RefundExceptionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundExceptionRecordServiceImpl implements RefundExceptionRecordService {
    @Autowired
    private RefundExceptionRecordDao refundExceptionRecordDao;
    @Override
    public RefundExceptionRecord selectByPrimaryKey(Long id) {
        return refundExceptionRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(RefundExceptionRecord record) {
        return refundExceptionRecordDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(RefundExceptionRecord record) {
        return refundExceptionRecordDao.updateByPrimaryKeySelective(record);
    }
    @Override
    public int insertByCondition(String orderId,String reqSn,String type) {
        RefundExceptionRecord refundExceptionRecord = new RefundExceptionRecord();
        refundExceptionRecord.setStatus(0);
        refundExceptionRecord.setOrderId(orderId);
        refundExceptionRecord.setPayChannel("singleRefund");
        refundExceptionRecord.setRefundSn(reqSn);
        refundExceptionRecord.setType(type);
        return refundExceptionRecordDao.insertSelective(refundExceptionRecord);
    }
}
