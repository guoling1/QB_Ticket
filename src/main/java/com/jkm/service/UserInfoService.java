package com.jkm.service;

import com.jkm.entity.UserInfo;
import net.sf.json.JSONObject;


public interface UserInfoService {
    /**
     *
     * 查询(根据uid查询)
     *
     **/
    UserInfo selectByUid (String uid);
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    UserInfo selectByPrimaryKey (Long id );

    /**
     *
     * 删除（根据主键ID删除）
     *
     **/
    int deleteByPrimaryKey (Long id );

    /**
     *
     * 添加
     *
     **/
    int insert( UserInfo record );

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective( UserInfo record );

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKeySelective( UserInfo record );

    /**
     *
     * 修改（根据主键ID修改）
     *
     **/
    int updateByPrimaryKey ( UserInfo record );

    /**
     * 添加用户信息
     * @param uid
     */
    void insertUser(String uid,String phone);
    /**
     *
     * 修改（根据uid修改）
     *
     **/
    int updatePhoneByUid ( UserInfo record );
    /**
     *
     * 实名
     *
     **/
    JSONObject bindAuthenUserInfo(UserInfo record );
}
