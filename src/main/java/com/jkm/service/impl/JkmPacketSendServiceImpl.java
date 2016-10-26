package com.jkm.service.impl;

import com.jkm.service.JkmPacketSendService;
import com.jkm.dao.JkmPacketGetDao;
import com.jkm.dao.JkmPacketSendDao;
import com.jkm.entity.JkmPacketGet;
import com.jkm.entity.JkmPacketSend;
import com.jkm.util.RandomUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class JkmPacketSendServiceImpl implements JkmPacketSendService {
    @Resource
    private JkmPacketSendDao jkmPacketSendDao;
    @Resource
    private JkmPacketGetDao jkmPacketGetDao;

    public long insert(String type, long userId, int amount, int count) {
        JkmPacketSend jkmPacketSend = new JkmPacketSend();
        jkmPacketSend.setDescr("");
        jkmPacketSend.setIsOver("0");
        jkmPacketSend.setPacketAmount(amount);
        jkmPacketSend.setPacketCount(count);
        jkmPacketSend.setPacketType(type);
        jkmPacketSend.setStatus("0");
        jkmPacketSend.setUserId(userId);
        jkmPacketSendDao.insertSelective(jkmPacketSend);
        RandomUtil randomUtil = new RandomUtil();
        int[] p = randomUtil.RandomAsign(amount,count);
        for(int i=0;i<count;i++){
            JkmPacketGet jkmPacketGet = new JkmPacketGet();
            jkmPacketGet.setGetUserId(0l);
            jkmPacketGet.setIsGet("0");
            jkmPacketGet.setSendId(jkmPacketSend.getId());
            jkmPacketGet.setSendUserId(userId);
            jkmPacketGet.setSingleAmount(p[i]);
            jkmPacketGet.setState("0");
            jkmPacketGetDao.insertSelective(jkmPacketGet);
        }
        return jkmPacketSend.getId();
    }
}
