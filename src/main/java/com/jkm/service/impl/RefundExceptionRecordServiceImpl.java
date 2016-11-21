package com.jkm.service.impl;


import com.jkm.entity.RefundExceptionRecord;
import com.jkm.service.RefundExceptionRecordService;
import org.springframework.stereotype.Service;

@Service
public class RefundExceptionRecordServiceImpl implements RefundExceptionRecordService {
    @Override
    public RefundExceptionRecord selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public int insertSelective(RefundExceptionRecord record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(RefundExceptionRecord record) {
        return 0;
    }
}
