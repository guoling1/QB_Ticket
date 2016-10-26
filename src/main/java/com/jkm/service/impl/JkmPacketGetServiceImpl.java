package com.jkm.service.impl;

import com.jkm.dao.JkmPacketGetDao;
import com.jkm.service.JkmPacketGetService;
import com.jkm.entity.JkmPacketGet;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class JkmPacketGetServiceImpl implements JkmPacketGetService {
    @Resource
    private JkmPacketGetDao jkmPacketGetDao;
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public JSONObject getRandomPacket(long userId, long sendId) {
        int restCount = jkmPacketGetDao.selectCountUnGet(sendId);
        JSONObject result = new JSONObject();
        if(restCount<=0){
            result.put("status","1");
            result.put("message","红包已抢完");
            result.put("code",2001);
            return result;
        }

        Map<String,Long> map1 = new HashMap<String,Long>();
        map1.put("getUserId",userId);
        map1.put("sendId",sendId);
        int hasCount = jkmPacketGetDao.selectHasCount(map1);
        if(hasCount>0){
            result.put("status","1");
            result.put("message","您已经抢过该红包");
            result.put("code",2002);
            return result;
        }
        JkmPacketGet jkmPacketGet = jkmPacketGetDao.selectTopOnePacket();//查出一条没领的红包
        Map<String,Long> map2 = new HashMap<String,Long>();
        map2.put("getUserId",userId);
        map2.put("id",jkmPacketGet.getId());
        jkmPacketGetDao.updateByPrimaryKey(map2);
        result.put("status","0");
        result.put("message","");
        result.put("code",2000);
        return result;
    }
}
