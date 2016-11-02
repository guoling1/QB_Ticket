package com.jkm.service.hy.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jkm.service.hy.helper.serialize.HySdkSerializeUtil;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeAlias;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeListNeedConverter;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeNoInclude;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeNoNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/1.
 *
 * 订单提交回调  -- 返回参数
 */
@Data
public class HySubmitOrderCallbackResponse {

    /**
     * api用户请求时传入的特征值
     */
    @HySdkSerializeNoNull
    @HySdkSerializeAlias(name = "reqtoken")
    private String reqToken;

    /**
     * 出发站名称
     */
    @HySdkSerializeNoInclude
    @HySdkSerializeAlias(name = "from_station_name")
    private String fromStationName;

    /**
     * 取票号
     */
    @HySdkSerializeAlias(name = "ordernumber")
    private String orderNumber;

    /**
     * 历时
     */
    @HySdkSerializeNoInclude
    @HySdkSerializeAlias(name = "runtime")
    private String runTime;

    /**
     * 车次
     */
    @HySdkSerializeNoInclude
    private String checi;

    /**
     * 简码
     */
    @HySdkSerializeNoNull
    private int code;

    /**
     * 出发站简码
     */
    @HySdkSerializeNoInclude
    @HySdkSerializeAlias(name = "from_station_code")
    private String fromStationCode;

    /**
     * 订单金额
     */
    @HySdkSerializeNoNull
    @HySdkSerializeAlias(name = "orderamount")
    private String orderAmount;

    /**
     * 到达站名称
     */
    @HySdkSerializeNoInclude
    @HySdkSerializeAlias(name = "to_station_code")
    private String toStationName;


    /**
     * 仅refund_online=1，表示该订单无法在线退票或改签（12306官网提示)
     */
    @HySdkSerializeAlias(name = "refundonline")
    private String refundOnline;

    /**
     * 空铁订单号
     */
    @HySdkSerializeNoNull
    @HySdkSerializeAlias(name = "transactionid")
    private String transactionId;

    /**
     * 发车时间
     */
    @HySdkSerializeNoInclude
    @HySdkSerializeAlias(name = "starttime")
    private String startTime;

    /**
     * 商户订单号
     */
    @HySdkSerializeNoNull
    @HySdkSerializeAlias(name = "orderid")
    private String orderId;

    /**
     * 乘车日期
     */
    @HySdkSerializeNoInclude
    @HySdkSerializeAlias(name = "train_date")
    private String trainDate;

    /**
     * 是否占座成功
     */
    @HySdkSerializeNoNull
    @HySdkSerializeAlias(name = "ordersuccess")
    private String orderSuccess;

    /**
     * 操作是否成功
     */
    private boolean success;

    /**
     * 描述信息
     */
    @HySdkSerializeNoNull
    private String msg;

    /**
     * 乘客列表
     */
    @HySdkSerializeListNeedConverter
    private List<Passenger> passengers;

    @Data
    public class Passenger{
        /**
         * 0：正常 1：待审核 2：未通过（占座结果回调才有）
         */
        private int reason;

        /**
         * 乘客编号
         */
        @HySdkSerializeAlias(name = "passengerid")
        private int passengerId;

        /**
         * 坐席编号
         */
        private String cxin;

        /**
         * 票号
         */
        @HySdkSerializeAlias(name = "ticket_no")
        private String ticketNo;
    }

    public HySubmitOrderCallbackResponse converterJsonObjectToResponse(final JSONObject jsonObject) {
        final HySubmitOrderCallbackResponse hySubmitOrderCallbackResponse = new HySubmitOrderCallbackResponse();
        HySdkSerializeUtil.convertJsonObjectToResponse(jsonObject, hySubmitOrderCallbackResponse);
        final JSONArray passengers = jsonObject.getJSONArray("passengers");
        final List<Passenger> passengerList = new ArrayList<>(passengers.size());
        for (int i = 0; i < passengers.size(); i++) {
            final JSONObject jo = passengers.getJSONObject(i);
            final Passenger passenger = new Passenger();
            passenger.setPassengerId(jo.getIntValue("passengerid"));
            passenger.setReason(jo.getIntValue("reason"));
            passenger.setCxin(jo.getString("cxin"));
            passenger.setTicketNo(jo.getString("ticket_no"));
            passengerList.add(passenger);
        }
        hySubmitOrderCallbackResponse.setPassengers(passengerList);
        return hySubmitOrderCallbackResponse;
    }
}
