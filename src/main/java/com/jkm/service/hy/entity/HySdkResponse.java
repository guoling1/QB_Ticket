package com.jkm.service.hy.entity;

import lombok.Data;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Data
public class HySdkResponse {

    /**
     *true:成功，false:失败
     */
    private boolean success;

    /**
     *状态编码
     */
    private int code;

    /**
     *提示信息
     */
    private String msg;

}
