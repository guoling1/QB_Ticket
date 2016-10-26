package com.jkm.dao;


import com.jkm.entity.JkmPacketGet;

import java.util.Map;

public interface JkmPacketGetDao {
    int insertSelective(JkmPacketGet jkmPacketGet);

    int selectCountUnGet(long sendId);

    JkmPacketGet selectTopOnePacket();

    void updateByPrimaryKey(Map<String, Long> map);

    int selectHasCount(Map<String, Long> map);

}
