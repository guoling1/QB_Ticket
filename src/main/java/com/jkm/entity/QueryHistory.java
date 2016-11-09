package com.jkm.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2016/11/8.
 */
@Data
public class QueryHistory extends BaseEntity {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 出发站简码
     */
    private String fromStation;

    /**
     * 到达站简码
     */
    private String toStation;

    /**
     * 出发车站名
     */
    private String fromStationName;

    /**
     * 到达车站名
     */
    private String toStationName;
}
