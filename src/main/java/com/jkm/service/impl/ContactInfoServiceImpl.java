package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.ContactInfoDao;
import com.jkm.entity.TbContactInfo;
import com.jkm.service.ContactInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/2.
 */
@Service
public class ContactInfoServiceImpl implements ContactInfoService {

    @Autowired
    private ContactInfoDao contactInfoDao;

    /**
     * {@inheritDoc}
     *
     * @param contactInfo
     */
    @Override
    public void add(final TbContactInfo contactInfo) {
        this.contactInfoDao.insert(contactInfo);
    }

    /**
     * {@inheritDoc}
     *
     * @param contactInfo
     * @return
     */
    @Override
    public int update(final TbContactInfo contactInfo) {
        return this.contactInfoDao.update(contactInfo);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<TbContactInfo> selectById(final long id) {
        return Optional.fromNullable(this.contactInfoDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param ids
     * @return
     */
    @Override
    public List<TbContactInfo> selectByIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return this.contactInfoDao.selectByIds(ids);
    }

    /**
     * {@inheritDoc}
     *
     * @param uid
     * @return
     */
    @Override
    public Optional<TbContactInfo> selectByUid(final String uid) {
        return Optional.fromNullable(this.contactInfoDao.selectByUid(uid));
    }
}
