package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.ContactFormDao;
import com.jkm.entity.ContactForm;
import com.jkm.service.ContactFormService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
@Service
public class ContactFormServiceImpl implements ContactFormService {
    @Autowired
    private ContactFormDao contactFormDao;

    /**
     *
     * @param contactForm
     * @return
     */
    @Override
    public long add(final ContactForm contactForm) {
        this.contactFormDao.insert(contactForm);
        return contactForm.getId();
    }

    /**
     *
     * @param id
     * @return
     */
//    @Override
//    public int delete(final long id) {
//        return this.contactFormDao.delete(id);
////    }

    /**
     *
     * @param contactForm
     * @return
     */
//    @Override
//    public int updata(final ContactForm contactForm) {
//
//        return this.contactFormDao.update(contactForm);
//    }

    /**
     *
     * @param userName
     * @return
     */
    @Override
    public List<ContactForm> selectByUserName(final String userName) {
        return (List<ContactForm>) this.contactFormDao.selectByUserName(userName);
    }

    /**
     * {@inheritDoc}
     *
     * @param uid
     * @return
     */
    @Override
    public Optional<ContactForm> selectByUid(final String uid) {
        return Optional.fromNullable(this.contactFormDao.selectByUid(uid));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ContactForm> selectById(final long id) {
        return Optional.fromNullable(this.contactFormDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param ids
     * @return
     */
    @Override
    public List<ContactForm> selectByIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return this.contactFormDao.selectByIds(ids);
    }
}
