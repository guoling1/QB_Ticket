package com.jkm.dao;

import com.jkm.entity.JkmPacketGet;
import com.jkm.entity.OrderForm;
import com.jkm.entity.QbBindCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface QbBindCardDao {
    /**
     * 新增
     *
     * @param qbBindCard
     */
    long  insert(QbBindCard qbBindCard);
    /**
     * 根据用户id查询银行卡列表
     * @param uid
     * @return
     */
    List<QbBindCard> select(String uid);
    /**
     * 银行卡列表
     * @param id
     * @return
     */
    QbBindCard selectOne(long id);
    /**
     * 删除银行卡列表
     * @param id
     */
    void delete(long id);
}
