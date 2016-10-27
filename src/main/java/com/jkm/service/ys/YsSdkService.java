package com.jkm.service.ys;


import com.jkm.service.ys.entity.YsRefundTicketRequest;
import com.jkm.service.ys.entity.YsRefundTicketResponse;
import com.jkm.service.ys.entity.YsTrainStationQueryRequest;
import com.jkm.service.ys.entity.YsTrainStationQueryResponse;

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
     * 请求ys退票
     *
     * @param request
     * @return
     */
    YsRefundTicketResponse refundTicket(final YsRefundTicketRequest request);

}
