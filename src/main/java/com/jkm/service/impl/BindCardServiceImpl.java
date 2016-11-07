package com.jkm.service.impl;

import com.jkm.dao.BindCardDao;
import com.jkm.entity.BindCard;
import com.jkm.service.BindCardService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BindCardServiceImpl implements BindCardService{
    private final Logger logger = LoggerFactory.getLogger(BindCardServiceImpl.class);
    @Autowired
    private BindCardDao bindCardDao;

    @Override
    public List<BindCard> selectByUid(String uid) {
        return bindCardDao.selectByUid(uid);
    }

    @Override
    public BindCard selectByPrimaryKey(Long id) {
        return bindCardDao.selectByPrimaryKey(id);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return bindCardDao.deleteByPrimaryKey(id);
    }

    @Override
    public long insert(BindCard record) {
        return bindCardDao.insert(record);
    }

    @Override
    public JSONObject insertSelective(BindCard record) {
        JSONObject jo = new JSONObject();
        int returnNum = bindCardDao.isAdd(record.getCardNo());
        if(returnNum>0){
            jo.put("result",false);
            jo.put("message","该银行卡已绑定");
        }else{
            long l = bindCardDao.insertSelective(record);
            if(l>0){
                jo.put("result",true);
                jo.put("data",record.getId());
                jo.put("message","绑定成功");
            }else{
                jo.put("result",false);
                jo.put("message","绑定失败");
            }
        }
        return jo;
    }

    @Override
    public int updateByPrimaryKeySelective(BindCard record) {
        return bindCardDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(BindCard record) {
        return bindCardDao.updateByPrimaryKey(record);
    }

    @Override
    public int updateState(Long id) {
        return bindCardDao.updateState(id);
    }
}
