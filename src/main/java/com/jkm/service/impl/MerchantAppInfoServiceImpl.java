package com.jkm.service.impl;

import com.jkm.dao.MerchantAppInfoDao;
import com.jkm.entity.MerchantAppInfo;
import com.jkm.service.MerchantAppInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantAppInfoServiceImpl implements MerchantAppInfoService {
    private final Logger logger = LoggerFactory.getLogger(MerchantAppInfoServiceImpl.class);
    @Autowired
    private MerchantAppInfoDao merchantAppInfoDao;

    @Override
    public MerchantAppInfo selectByPrimaryKey(Long id) {
        return merchantAppInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(MerchantAppInfo record) {
        return merchantAppInfoDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(MerchantAppInfo record) {
        return merchantAppInfoDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public MerchantAppInfo selectByOpenId(String openId) {
        return merchantAppInfoDao.selectByOpenId(openId);
    }

    @Override
    public String selectSecretKeyByOpenId(String openId) {
        return merchantAppInfoDao.selectSecretKeyByOpenId(openId);
    }
}
