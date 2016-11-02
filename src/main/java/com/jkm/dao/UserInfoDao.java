package com.jkm.dao;


import com.jkm.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserInfoDao {
    /**
     *
     * 查询（根据Uid和Appid查询）
     *
     **/
    UserInfo selectByUidAndAppid (UserInfo record );
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    UserInfo selectByPrimaryKey (@Param("id") Long id );

    /**
     *
     * 删除（根据主键ID删除）
     *
     **/
    int deleteByPrimaryKey ( @Param("id") Long id );

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
}
