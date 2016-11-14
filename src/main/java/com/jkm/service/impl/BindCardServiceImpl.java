package com.jkm.service.impl;

import com.google.common.base.Preconditions;
import com.jkm.dao.BindCardDao;
import com.jkm.entity.BindCard;
import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.service.BankCardBinService;
import com.jkm.service.BindCardService;
import com.jkm.util.ValidateUtils;
import com.jkm.util.ValidationUtil;
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
    @Autowired
    private BankCardBinService bankCardBinService;

    @Override
    public JSONObject selectByUid(String uid) {
        JSONObject jo = new JSONObject();
        List<BindCard> cardList = bindCardDao.selectByUid(uid);
        if(cardList!=null&&cardList.size()>0){
            for(int i=0;i<cardList.size();i++){
                cardList.get(i).setCardId(UserBankCardSupporter.decryptCardId(cardList.get(i).getCardId()));
                cardList.get(i).setCardNo(ValidationUtil.getShortCardNo(UserBankCardSupporter.decryptCardNo(cardList.get(i).getCardNo())));
            }
            jo.put("cardList",cardList);
            JSONObject ju = new JSONObject();
            ju.put("accountName",cardList.get(0).getAccountName());
            ju.put("cardId",cardList.get(0).getCardId());
            jo.put("userCardInfo",ju);
        }
        return jo;
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
        Preconditions.checkNotNull(requestJson.get("bankCode"),"卡宾不能为空");
//        if(!ValidateUtils.isIDCard(requestJson.getString("cardNo"))){
//            jo.put("result",false);
//            jo.put("message","银行卡号不正确");
//        }
        if(!ValidationUtil.isIdCard(requestJson.getString("cardId"))){
            jo.put("result",false);
            jo.put("message","身份证号不正确");
        }
        if(!ValidationUtil.isMobile(requestJson.getString("phone"))){
            jo.put("result",false);
            jo.put("message","手机号不正确");
        }
        if(requestJson.getInt("isAgree")!=0){
            jo.put("result",false);
            jo.put("message","请同意协议");
        }
        BindCard bindCard = new BindCard();
        bindCard.setCardType("00");
        bindCard.setUid(requestJson.getString("uid"));
        bindCard.setCardNo(UserBankCardSupporter.encryptCardNo(requestJson.getString("cardNo")));
        bindCard.setAccountName(requestJson.getString("accountName"));
        bindCard.setCardId(UserBankCardSupporter.encryptCardId(requestJson.getString("cardId")));
        bindCard.setPhone(requestJson.getString("phone"));
        bindCard.setBankCode(requestJson.getString("bankCode"));
        bindCard.setStatus(0);
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
    public JSONObject insertBindCard(BindCard record) {
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
