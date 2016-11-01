package com.jkm.service.hy.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2016/11/1.
 */
@Data
public class HyTicketQueryRequest extends HySdkRequest {

    /**
     * 乘车日期（yyyy-MM-dd）
     */
    private String trainDate;

    /**
     * 出发站三字码
     */
    private String fromStation;

    /**
     * 到达站三字码
     */
    private String toStation;

    /**
     * ADULT，固定值表示普通票（0X00 代表学生票）
     */
    private String purposeCodes;

}
