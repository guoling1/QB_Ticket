package com.jkm.service.impl;

import com.jkm.dao.IUserDao;
import com.jkm.entity.User;
import com.jkm.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IUserServiceImpl implements IUserService {
    @Resource
    private IUserDao iUserDao;

    public User getUserById(Integer userId) {
        return this.iUserDao.selectByPrimaryKey(userId);
    }
}
