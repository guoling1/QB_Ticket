package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.GrabTicketForm;
import com.jkm.enums.EnumGrabTicketStatus;

/**
 * Created by yuxiang on 2016-11-07.
 */
public interface GrabTicketFromService {

    /**
     *
     * 初始化
     */
    void init(GrabTicketForm grabTicketForm);

    /**
     * 查询
     * @param gtabTicketFormId
     * @return
     */
    Optional<GrabTicketForm> selectByIdWithLock(long gtabTicketFormId);

    /**
     * 更新
     * @param orderForm
     */
    void update(GrabTicketForm orderForm);

    /**
     * 根据订单id查询抢票单
     * @param orderId
     * @return
     */
    Optional<GrabTicketForm> selectByOrderIdWithLock(String orderId);

    /**
     *
     * @param grabTicketFormId
     * @return
     */
    Optional<GrabTicketForm> selectById(long grabTicketFormId);

    /**
     * 根据id更新状态
     * @param status
     * @param id
     */
    void updateStatusById(EnumGrabTicketStatus status, long id);
}
