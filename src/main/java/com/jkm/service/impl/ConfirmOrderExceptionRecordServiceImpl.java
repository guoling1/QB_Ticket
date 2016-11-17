package com.jkm.service.impl;

import com.jkm.dao.ConfirmOrderExceptionRecordDao;
import com.jkm.entity.ConfirmOrderExceptionRecord;
import com.jkm.service.ConfirmOrderExceptionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/11/17.
 */
@Service
public class ConfirmOrderExceptionRecordServiceImpl implements ConfirmOrderExceptionRecordService {

    @Autowired
    private ConfirmOrderExceptionRecordDao confirmOrderExceptionRecordDao;

    /**
     * {@inheritDoc}
     *
     * @param confirmOrderExceptionRecord
     * @return
     */
    @Override
    public long add(final ConfirmOrderExceptionRecord confirmOrderExceptionRecord) {
        this.confirmOrderExceptionRecordDao.insert(confirmOrderExceptionRecord);
        return confirmOrderExceptionRecord.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public int markProcessedById(final long id) {
        return this.confirmOrderExceptionRecordDao.markProcessedById(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public ConfirmOrderExceptionRecord selectById(final long id) {
        return this.confirmOrderExceptionRecordDao.selectById(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     * @return
     */
    @Override
    public ConfirmOrderExceptionRecord selectByOrderFormId(final long orderFormId) {
        return this.confirmOrderExceptionRecordDao.selectByOrderFormId(orderFormId);
    }
}
