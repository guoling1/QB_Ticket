package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.ContactFormDao;
import com.jkm.entity.ContactForm;
import com.jkm.service.ContactFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param userId
     * @return
     */
    @Override
    public int delete(final long userId) {
        return this.contactFormDao.delete(userId);
    }

    /**
     *
     * @param contactForm
     * @return
     */
    @Override
    public int updata(final ContactForm contactForm) {

        return this.contactFormDao.update(contactForm);
    }

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
    public Optional<ContactForm> selectByUid(final long uid) {
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
}
