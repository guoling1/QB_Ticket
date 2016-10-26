package com.jkm.service;


public interface JkmPacketSendService {
    long insert(String type,long userId,int amount,int count);
}
