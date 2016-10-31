package com.jkm.service.ys;


import com.jkm.service.ys.entity.*;

/**
 * Created by yulong.zhang on 2016/9/28.
 */
public interface YsSdkService {

    /**
     * 查询站点信息
     * @param request
     * @return
     */
    YsTrainStationQueryResponse queryTrainStation(final YsTrainStationQueryRequest request);

    /**
     * 查询车次
     * @param request
     * @return
     */
    YsTrainTripsQueryResponse queryTrainStation(final YsTrainTripsQueryRequest request);

    /**
     * 请求ys退票
     *
     * @param request
     * @return
     */
    YsRefundTicketResponse refundTicket(final YsRefundTicketRequest request);

    /**
     * 记录订票回调
     *
     * @param callbackResponse
     */
    void recordBookTicketCallbackParams(YsTrainTicketBookingCallbackResponse callbackResponse);
}
