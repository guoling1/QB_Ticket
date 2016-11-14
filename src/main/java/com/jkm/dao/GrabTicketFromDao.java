package com.jkm.dao;

import com.jkm.entity.GrabTicketForm;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-07.
 */
public interface GrabTicketFromDao {

    /**
     * 初始化
     */
    void init(GrabTicketForm grabTicketForm);

    /**
     * 查询
     * @param id
     * @return
     */
    GrabTicketForm selectByIdWithLock(long id);

    /**
     * 更新
     * @param orderForm
     */
    void update(GrabTicketForm orderForm);

    /**
     * 查询
     *
     * @param orderId
     * @return
     */
    GrabTicketForm selectByOrderIdWithLock(String orderId);

    /**
     * 查询
     * @param id
     * @return
     */
    GrabTicketForm selectById(long id);

    /**
     * 更新状态
     * @param status
     * @param id
     */
    void updateStatusById(int status, long id);

    /**
     * 根据uid查询
     * @param uid
     * @return
     */
    List<GrabTicketForm> selectByUid(String uid);
}
