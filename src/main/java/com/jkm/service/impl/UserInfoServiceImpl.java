package com.jkm.service.impl;

import com.jkm.dao.UserInfoDao;
import com.jkm.entity.UserInfo;
import com.jkm.service.UserInfoService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static Logger log = Logger.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo selectByUid(String uid) {
        return userInfoDao.selectByUid(uid);
    }

    @Override
    public UserInfo selectByPrimaryKey(Long id) {
        return userInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userInfoDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserInfo record) {
        return userInfoDao.insert(record);
    }

    @Override
    public int insertSelective(UserInfo record) {
        return userInfoDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(UserInfo record) {
        return userInfoDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserInfo record) {
        return userInfoDao.updateByPrimaryKey(record);
    }

    @Override
    public void insertUser(String uid,String phone) {
        UserInfo userInfo = userInfoDao.selectByUid(uid);
        if(userInfo==null){
            UserInfo u = new UserInfo();
            u.setAppId(uid.split("_")[0]);
            u.setUid(uid);
            u.setStatus(0);
            userInfoDao.insert(u);
        }else{
            userInfo.setPhone(phone);
            userInfoDao.updatePhoneByUid(userInfo);
        }
    }

    @Override
    public int updatePhoneByUid(UserInfo record) {
        int rown = userInfoDao.updatePhoneByUid(record);
        return rown;
    }

    @Override
    public JSONObject bindAuthenUserInfo(UserInfo record) {
        JSONObject jo = new JSONObject();
        UserInfo userInfo = userInfoDao.selectByUid(record.getUid());
        if(userInfo==null){
            UserInfo u = new UserInfo();
            u.setAppId(record.getAppId());
            u.setUid(record.getUid());
            u.setPhone(record.getPhone());
            u.setAccount(record.getAccount());
            u.setPwd(record.getPwd());
            u.setCardId(record.getCardId());
            u.setCardNo(record.getCardNo());
            u.setRealName(record.getRealName());
            u.setStatus(0);
            userInfoDao.insert(u);
            jo.put("result",true);
        }else{
            if(userInfo.getCardId()!=null&&userInfo.getRealName()!=null
                    &&!"".equals(userInfo.getCardId())&&!"".equals(userInfo.getRealName())){
                if(record.getCardId().equals(userInfo.getCardId())&&record.getRealName().equals(userInfo.getRealName())){
                    jo.put("result",true);
                }else{
                    jo.put("result",false);
                    jo.put("message","请绑定自己银行卡");
                }
            }else{
                userInfo.setRealName(record.getRealName());
                userInfo.setCardNo(record.getCardNo());
                userInfo.setCardId(record.getCardId());
                userInfoDao.updateByPrimaryKeySelective(userInfo);
                jo.put("result",true);
            }
        }
        return jo;
    }
}
