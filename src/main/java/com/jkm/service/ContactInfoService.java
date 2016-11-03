package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.TbContactInfo;

/**
 * Created by yulong.zhang on 2016/11/2.
 */
public interface ContactInfoService {

    /**
     * 增加联系人
     *
     * @param contactInfo
     */
    void add(TbContactInfo contactInfo);

    /**
     * 更新
     *
     * @param contactInfo
     * @return
     */
    int update(TbContactInfo contactInfo);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<TbContactInfo> selectById(long id);

    /**
     * 按id查询
     *
     * @param uid
     * @return
     */
    Optional<TbContactInfo> selectByUid(String uid);
}