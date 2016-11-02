package com.jkm.service.impl;

import com.jkm.dao.UserInfoDao;
import com.jkm.entity.UserInfo;
import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.service.WebsiteService;
import com.jkm.util.DESUtil;
import com.jkm.util.HttpClientUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 1.登录12306
 * 2.如果有用户信息直接登录，如果没有保存用户信息
 *
 */
public class WebsiteServiceImpl implements WebsiteService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Override
    public long addWebSite(String data, String uid, String appid) {
        long backId = 0;
        try {
            JSONObject webSiteInfo = JSONObject.fromObject(data);
            String tempData = DESUtil.encrypt(data);
            JSONObject jo = new JSONObject();
            jo.put("data",tempData);
            jo.put("accountversion","2");
            //①
            JSONObject responseJson = HttpClientUtil.sendPost(jo,"http://trainorder.ws.hangtian123.com/cn_interface/trainAccount/validate");
            if(responseJson.getBoolean("success")){
                UserInfo userInfo = new UserInfo();
                userInfo.setStatus(0);
                userInfo.setUid(uid);
                userInfo.setAppId(appid);
                userInfo.setAccount(webSiteInfo.getString("trainAccount"));
                userInfo.setPwd(UserBankCardSupporter.encryptPwd(webSiteInfo.getString("pass")));
                UserInfo userInfoResult = userInfoDao.selectByUidAndAppid(userInfo);
                //②
                if(userInfoResult==null){
                    backId = userInfoDao.insert(userInfo);
                }else{
                    userInfo.setId(userInfoResult.getId());
                    backId = userInfoDao.updateByPrimaryKeySelective(userInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backId;
    }
}
