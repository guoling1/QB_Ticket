package com.jkm.service.impl;

import com.google.common.base.Preconditions;
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
    public JSONObject insertSelective(JSONObject requestJson) {
        JSONObject jo = new JSONObject();
        Preconditions.checkNotNull(requestJson.get("cardNo"),"缺少银行卡号");
        Preconditions.checkNotNull(requestJson.get("accountName"),"缺少开户姓名");
        Preconditions.checkNotNull(requestJson.get("cardId"),"缺少身份证号");
        Preconditions.checkNotNull(requestJson.get("phone"),"缺少手机号");
        Preconditions.checkNotNull(requestJson.get("isAgree"),"同意协议才能绑定银行卡");
        Preconditions.checkNotNull(requestJson.get("vCode"),"请输入验证码");
        if(requestJson.getInt("isAgree")!=0){
            jo.put("result",false);
            jo.put("message","请同意协议");
        }
        BindCard bindCard = new BindCard();
        bindCard.setCardType("00");
        bindCard.setUid(requestJson.getString("uid"));
        bindCard.setCardNo(requestJson.getString("cardNo"));
        bindCard.setAccountName(requestJson.getString("accountName"));
        bindCard.setCardId(requestJson.getString("cardId"));
        bindCard.setPhone(requestJson.getString("phone"));

        int returnNum = bindCardDao.isAdd(bindCard.getCardNo());
        if(returnNum>0){
            jo.put("result",false);
            jo.put("message","该银行卡已绑定");
        }else{
            long l = bindCardDao.insertSelective(bindCard);
            if(l>0){
                jo.put("result",true);
                jo.put("data",bindCard.getId());
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
