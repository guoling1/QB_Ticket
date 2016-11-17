package com.jkm.dao;

import com.jkm.entity.BindCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public interface BindCardDao {
    /**
     *
     * 查询（根据uid查询）
     *
     **/
    List<BindCard> selectByUid (@Param("uid") String uid );
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    BindCard selectByPrimaryKey (@Param("id") Long id );

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
    long insert( BindCard record );

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    long insertSelective( BindCard record );

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKeySelective( BindCard record );

    /**
     *
     * 修改（根据主键ID修改）
     *
     **/
    int updateByPrimaryKey ( BindCard record );
    /**
     *
     * 修改状态
     *
     **/
    int updateState (@Param("id") Long id );
    /**
     *
     * 验证是否添加过银行卡
     *
     **/
    int isAdd (@Param("cardNo") String cardNo,@Param("uid") String uid );
}
