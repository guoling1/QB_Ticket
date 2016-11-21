package com.jkm.service.impl;

import com.jkm.dao.MerchantInfoDao;
import com.jkm.entity.MerchantInfo;
import com.jkm.service.MerchantInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantInfoServiceImpl implements MerchantInfoService {
    private final Logger logger = LoggerFactory.getLogger(MerchantInfoServiceImpl.class);
    @Autowired
    private MerchantInfoDao merchantInfoDao;

    @Override
    public MerchantInfo selectByPrimaryKey(Long id) {
        return merchantInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(MerchantInfo record) {
        return merchantInfoDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(MerchantInfo record) {
        return merchantInfoDao.updateByPrimaryKeySelective(record);
    }
}
