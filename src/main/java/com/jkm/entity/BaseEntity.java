package com.jkm.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
@Data
public class BaseEntity {

    private long id;

    private int status;

    private Date createTime;

    private Date updateTime;
}
