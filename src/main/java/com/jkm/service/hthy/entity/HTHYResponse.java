package com.jkm.service.hthy.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/10/31.
 *
 * true	100	处理或操作成功
 * false	101	传入的 json 为空对象
 * false	102	传入的 json 格式错误
 * false	103	通用参数缺失
 * false	104	账户无效
 * false	105	错误的通用参数
 * false	106	接口不存在
 * false	107	业务参数缺失
 * false	108	错误的业务参数
 * false	112	订单状态不正确
 * false	113	当前时间不提供服务
 * false	402	订单不存在
 * false	403	账户余额不足
 * false	404	账户金额被冻结
 *
 * base response params
 */
@Data
public class HTHYResponse {

    /**
     * 返回标识（true:成功，false:失败）
     */
    private boolean success;

    /**
     * 状态编码
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;
}
