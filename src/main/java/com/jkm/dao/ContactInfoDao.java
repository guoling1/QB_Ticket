package com.jkm.dao;

import com.jkm.entity.TbContactInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Created by yulong.zhang on 2016/11/2.
 */
public interface ContactInfoDao {

    /**
     * 插入
     *
     * @param contactInfo
     */
    void insert(TbContactInfo contactInfo);

    /**
     * 更新
     *
     * @param contactInfo
     * @return
     */
    int update(TbContactInfo contactInfo);

    /**
     * 按id
     *
     * @param id
     * @return
     */
    TbContactInfo selectById(@Param("id") long id);

    /**
     * 按id
     *
     * @param uid
     * @return
     */
    TbContactInfo selectByUid(@Param("uid") String uid);
}
