package com.jkm.controller.helper.request;

import lombok.Data;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-24 21:21
 */
@Data
public class RequestUserInfo {
    /**
     * appId
     */
    private String appid;

    /**
     * 用户id
     */
    private String uid;
}
