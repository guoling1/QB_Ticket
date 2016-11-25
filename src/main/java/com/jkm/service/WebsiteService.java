package com.jkm.service;

import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2016/11/2.
 */
public interface WebsiteService {
    /**
     * 添加12306账号
     * @param data
     * @param uid
     * @param appid
     * @return
     */
    JSONObject addWebSite(String data,String uid, String appid) throws Exception;

    /**
     * 一键导入联系人
     * @param uid
     */
    void importContacts(String uid);
}
