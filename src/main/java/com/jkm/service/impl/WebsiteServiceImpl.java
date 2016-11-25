package com.jkm.service.impl;

import com.google.common.base.Preconditions;
import com.jkm.controller.helper.request.RequestWeb;
import com.jkm.dao.ContactInfoDao;
import com.jkm.entity.UserInfo;
import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.enums.EnumAddUserStatus;
import com.jkm.enums.EnumTrainTicketType;
import com.jkm.service.ContactInfoService;
import com.jkm.service.UserInfoService;
import com.jkm.service.WebsiteService;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.DESUtil;
import com.jkm.util.HttpClientUtil;
import com.jkm.entity.TbContactInfo;
import net.sf.json.JSONArray;
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
    @Autowired
    private ContactInfoDao contactInfoDao;


    /**
     * 1.登录12306
     * 2.如果有用户信息直接登录，如果没有保存用户信息
     * @param data
     * @param uid
     * @param appid
     * @return
     */
    @Override
    public JSONObject addWebSite(RequestWeb data, String uid, String appid) throws Exception {
        long backId = 0;

        JSONObject webSiteInfo = JSONObject.fromObject(data);
        String tempData = DESUtil.encrypt(webSiteInfo.toString());
        JSONObject jo = new JSONObject();
        jo.put("data",tempData);
        jo.put("accountversion","2");
        //①
        JSONObject responseJson = HttpClientUtil.sendPost(jo, HySdkConstans.ACCOUNT_VALIDATE_URL);

        JSONObject jb = new JSONObject();
        if(responseJson!=null){
            if(responseJson.getBoolean("success")){
                UserInfo userInfo = new UserInfo();
                userInfo.setStatus(0);
                userInfo.setUid(uid);
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
                jb.put("result",true);
                jb.put("data",backId);
                jb.put("message","登陆成功");
            }else{
                jb.put("result",false);
                jb.put("message",responseJson.getString("errorMsg"));
            }
        }else{
            jb.put("result",false);
            jb.put("message","通道返回信息为空");
        }
        return jb;
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
        Preconditions.checkNotNull(userInfoResult.getAccount(),"未添加12306账号或账户异常，不能使用此功能");
        JSONObject userInfoJson = new JSONObject();
        userInfoJson.put("trainAccount",userInfoResult.getAccount());
        userInfoJson.put("pass",UserBankCardSupporter.decryptPwd(userInfoResult.getPwd()));
        String tempData = null;
        try {
            tempData = DESUtil.encrypt(userInfoJson.toString());
            JSONObject jo = new JSONObject();
            jo.put("data",tempData);
            jo.put("accountversion","2");
            JSONObject responseJson = HttpClientUtil.sendPost(jo, HySdkConstans.ACCOUNT_CONTACT_QUERY);
            String resultData = DESUtil.decrypt(responseJson.getString("data"));
            JSONArray contactsArr = JSONArray.fromObject(resultData);
            if(!contactsArr.isEmpty()){
                for(int i=0;i<contactsArr.size();i++){
                    if("1".equals(contactsArr.getJSONObject(i).getString("identyType"))&&
                            contactsArr.getJSONObject(i).getInt("checkStatus")==0){
                        TbContactInfo contactInfo = new TbContactInfo();
                        contactInfo.setUid(uid);
                        contactInfo.setBirthday(contactsArr.getJSONObject(i).getString("birthday"));
                        contactInfo.setSex(contactsArr.getJSONObject(i).getInt("sex"));
                        contactInfo.setTel(contactsArr.getJSONObject(i).getString("phone"));
                        contactInfo.setIdenty(UserBankCardSupporter.encryptCardNo(contactsArr.getJSONObject(i).getString("identy")));
                        contactInfo.setIdentyType("1");//只接受身份证
                        contactInfo.setCountry(contactsArr.getJSONObject(i).getString("country"));
                        contactInfo.setCheckStatus(0);
                        contactInfo.setEmail(contactsArr.getJSONObject(i).getString("email"));
                        contactInfo.setAddress(contactsArr.getJSONObject(i).getString("address"));
                        contactInfo.setName(contactsArr.getJSONObject(i).getString("name"));
                        contactInfo.setIsUserSelf(contactsArr.getJSONObject(i).getInt("isUserSelf"));
                        if(contactsArr.getJSONObject(i).getInt("personType")==0){
                            contactInfo.setPersonType(1);
                        }
                        if(contactsArr.getJSONObject(i).getInt("personType")==1){
                            contactInfo.setPersonType(2);
                        }
                        if(contactsArr.getJSONObject(i).getInt("personType")==2){
                            contactInfo.setPersonType(3);
                        }
                        int count=0;
                        if(!(EnumTrainTicketType.CHILDREN.getId()).equals(contactInfo.getPersonType()+"")){
                            count = contactInfoDao.selectCountByIdenty(contactInfo.getIdenty(),contactInfo.getUid());
                        }
                        if(count==0){
                            contactInfoService.add(contactInfo);
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.info("导入联系人加密错误",e.getMessage());
        }
    }
}
