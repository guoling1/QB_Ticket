package com.jkm.service.impl;

import com.google.common.base.Preconditions;
import com.jkm.entity.UserInfo;
import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.service.ContactInfoService;
import com.jkm.service.UserInfoService;
import com.jkm.service.WebsiteService;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.DESUtil;
import com.jkm.util.HttpClientUtil;
import com.jkm.entity.TbContactInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebsiteServiceImpl implements WebsiteService {
    private final Logger logger = LoggerFactory.getLogger(WebsiteServiceImpl.class);
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ContactInfoService contactInfoService;

    /**
     * 1.登录12306
     * 2.如果有用户信息直接登录，如果没有保存用户信息
     * @param data
     * @param uid
     * @param appid
     * @return
     */
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
            JSONObject responseJson = HttpClientUtil.sendPost(jo, HySdkConstans.ACCOUNT_VALIDATE_URL);
            if(responseJson.getBoolean("success")){
                UserInfo userInfo = new UserInfo();
                userInfo.setStatus(0);
                userInfo.setUid(appid+"_"+uid);
                userInfo.setAppId(appid);
                userInfo.setAccount(webSiteInfo.getString("trainAccount"));
                userInfo.setPwd(UserBankCardSupporter.encryptPwd(webSiteInfo.getString("pass")));
                UserInfo userInfoResult = userInfoService.selectByUid(uid);
                //②
                if(userInfoResult==null){
                    backId = userInfoService.insert(userInfo);
                }else{
                    userInfo.setId(userInfoResult.getId());
                    int updataRows = userInfoService.updateByPrimaryKeySelective(userInfo);
                    if(updataRows>0){//修改成功
                        backId = userInfoResult.getId();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backId;
    }

    /**
     * 导入12306联系人信息
     * @param uid
     * @param appid
     */
    @Override
    public void importContacts(String uid) {
        UserInfo userInfoResult = userInfoService.selectByUid(uid);
        Preconditions.checkNotNull(userInfoResult,"登录信息异常");
        JSONObject userInfoJson = new JSONObject();
        userInfoJson.put("trainAccount",userInfoResult.getAccount());
        userInfoJson.put("pass",DESUtil.decrypt(userInfoResult.getPwd()));
        String tempData = null;
        try {
            tempData = DESUtil.encrypt(userInfoJson.toString());
            JSONObject jo = new JSONObject();
            jo.put("data",tempData);
            jo.put("accountversion","2");
            JSONObject responseJson = HttpClientUtil.sendPost(jo, HySdkConstans.ACCOUNT_CONTACT_QUERY);
            String resultData = DESUtil.decrypt(responseJson.getString("data"));
            JSONObject contactsArr = JSONObject.fromObject(resultData);
            if(!contactsArr.isEmpty()){
                for(int i=0;i<contactsArr.size();i++){
                    if("1".equals(contactsArr.getString("identyType"))){
                        TbContactInfo contactInfo = new TbContactInfo();

                        contactInfoService.findByUidAndIdenty(contactInfo);

//                        contactInfoService.add();
                    }

                }
            }
        } catch (Exception e) {
            logger.info("导入联系人加密错误",e.getMessage());
        }




    }
}
