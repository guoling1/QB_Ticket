package com.jkm.service;


import net.sf.json.JSONObject;

public interface JkmPacketGetService {
    public JSONObject getRandomPacket(long userId, long sendId);
}
