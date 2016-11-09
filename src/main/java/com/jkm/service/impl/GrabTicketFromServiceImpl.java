package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.GrabTicketFromDao;
import com.jkm.entity.GrabTicketForm;
import com.jkm.enums.EnumGrabTicketStatus;
import com.jkm.service.GrabTicketFromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-11-07.
 */
@Service
public class GrabTicketFromServiceImpl implements GrabTicketFromService {

    @Autowired
    private GrabTicketFromDao grabTicketFromDao;

    /**
     * {@inheritDoc}
     *
     * @param grabTicketForm
     */
    @Override
    public void init(GrabTicketForm grabTicketForm) {
        this.grabTicketFromDao.init(grabTicketForm);
    }

    /**
     * {@inheritDoc}
     *
     * @param gtabTicketFormId
     * @return
     */
    @Override
    public Optional<GrabTicketForm> selectByIdWithLock(long gtabTicketFormId) {
        return Optional.fromNullable(this.grabTicketFromDao.selectByIdWithLock(gtabTicketFormId));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderForm
     */
    @Override
    public void update(GrabTicketForm orderForm) {
        this.grabTicketFromDao.update(orderForm);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @return
     */
    @Override
    public Optional<GrabTicketForm> selectByOrderIdWithLock(String orderId) {
        return Optional.fromNullable(this.grabTicketFromDao.selectByOrderIdWithLock(orderId));
    }

    /**
     * {@inheritDoc}
     * @param grabTicketFormId
     * @return
     */
    @Override
    public Optional<GrabTicketForm> selectById(long grabTicketFormId) {
        return Optional.fromNullable(this.grabTicketFromDao.selectById(grabTicketFormId));
    }

    /**
     * {@inheritDoc}
     *
     * @param status
     * @param id
     */
    @Override
    public void updateStatusById(EnumGrabTicketStatus status, long id) {
        this.grabTicketFromDao.updateStatusById(status.getId(), id);
    }
}
