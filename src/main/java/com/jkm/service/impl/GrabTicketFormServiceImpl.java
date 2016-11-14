package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.GrabTicketFromDao;
import com.jkm.entity.GrabTicketForm;
import com.jkm.entity.RefundOrderFlow;
import com.jkm.enums.EnumGrabTicketStatus;
import com.jkm.enums.EnumRefundOrderFlowStatus;
import com.jkm.service.GrabTicketFormService;
import com.jkm.service.RefundOrderFlowService;
import com.jkm.service.ReturnMoneyOrderService;
import com.jkm.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-07.
 */
@Service
public class GrabTicketFormServiceImpl implements GrabTicketFormService {

    @Autowired
    private GrabTicketFromDao grabTicketFromDao;
    @Autowired
    private ReturnMoneyOrderService returnMoneyOrderService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private RefundOrderFlowService refundOrderFlowService;
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

    /**
     * {@inheritDoc}
     *
     * @param grabTicketFormId
     */
    @Override
    public void handleExpiredOGrabForm(long grabTicketFormId) {

        final GrabTicketForm grabTicketForm = this.grabTicketFromDao.selectByIdWithLock(grabTicketFormId);
        //判断是否可以取消
        if (grabTicketForm.isCanCancel()){
            grabTicketForm.setStatus(EnumGrabTicketStatus.GRAB_FORM_PAY_OVERTIME.getId());
            this.grabTicketFromDao.update(grabTicketForm);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param grabTicketFormId
     */
    @Override
    public void handleNoPackageWaitRefund(long grabTicketFormId) {

        final GrabTicketForm grabTicketForm = this.grabTicketFromDao.selectByIdWithLock(grabTicketFormId);
        Optional<RefundOrderFlow> optional = this.refundOrderFlowService.selectByGrabTicketFormId(grabTicketFormId);
        if(optional.isPresent() && optional.get().getStatus() == EnumRefundOrderFlowStatus.INIT.getId()){
            return;
        }
        //判断该订单状态是否可以全额退款, 没购买套餐, 或者购买套餐抢票下单失败
        if(grabTicketForm.getStatus() == EnumGrabTicketStatus.WAIT_FOR_REFUND.getId() ||
                grabTicketForm.getStatus() == EnumGrabTicketStatus.GRAB_FORM_REQUEST_FAIL.getId()){
            this.ticketService.returnToGrabFail(grabTicketFormId);
        }
    }

    /**
     * {@inheritDoc}
     * @param uid
     * @return
     */
    @Override
    public List<GrabTicketForm> selectByUid(String uid) {
        return this.grabTicketFromDao.selectByUid(uid);
    }
}
