package com.jkm.dao;


import com.jkm.entity.JkmPacketSend;

public interface JkmPacketSendDao {
    long insertSelective(JkmPacketSend jkmPacketSend);
}
