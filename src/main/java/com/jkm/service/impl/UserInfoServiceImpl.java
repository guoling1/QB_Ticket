package com.jkm.service.impl;

import com.jkm.dao.ContactInfoDao;
import com.jkm.dao.UserInfoDao;
import com.jkm.entity.UserInfo;
import com.jkm.service.UserInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class UserInfoServiceImpl implements UserInfoService {
    private static Logger log = Logger.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo selectByUid(String uid) {
        return userInfoDao.selectByUid(uid);
    }

    @Override
    public UserInfo selectByPrimaryKey(Long id) {
        return userInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userInfoDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserInfo record) {
        return userInfoDao.insert(record);
    }

    @Override
    public int insertSelective(UserInfo record) {
        return userInfoDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(UserInfo record) {
        return userInfoDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserInfo record) {
        return userInfoDao.updateByPrimaryKey(record);
    }
}
