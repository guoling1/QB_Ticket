package com.jkm.service.impl;

import com.jkm.dao.PaySequenceDao;
import com.jkm.entity.PaySequence;
import com.jkm.service.PaySequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaySequenceServiceImpl implements PaySequenceService{
    private final Logger logger = LoggerFactory.getLogger(PaySequenceServiceImpl.class);
    @Autowired
    private PaySequenceDao paySequenceDao;
    @Override
    public PaySequence selectByPrimaryKey(Long id) {
        return paySequenceDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(PaySequence record) {
        return paySequenceDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(PaySequence record) {
        return paySequenceDao.updateByPrimaryKeySelective(record);
    }
}
