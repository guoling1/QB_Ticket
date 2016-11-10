package com.jkm.service;

import com.jkm.entity.BindCard;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public interface BindCardService {
    /**
     *
     * 查询（根据uid查询）
     *
     **/
    List<BindCard> selectByUid (String uid);
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    BindCard selectByPrimaryKey (Long id );

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
    long insert( BindCard record );

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    JSONObject insertSelective(JSONObject record );

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
    int updateState ( Long id );
}
