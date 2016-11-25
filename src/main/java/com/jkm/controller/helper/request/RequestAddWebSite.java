package com.jkm.controller.helper.request;

import lombok.Data;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-25 17:41
 */
@Data
public class RequestAddWebSite {
    private String uid;
    private String appid;
    private RequestWeb data;
}
