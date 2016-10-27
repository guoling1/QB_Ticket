package com.jkm.common.third.channel.ys.service;


import com.jkm.common.third.channel.ys.service.entity.YsTrainStationQueryRequest;
import com.jkm.common.third.channel.ys.service.entity.YsTrainStationQueryResponse;

/**
 * Created by yulong.zhang on 2016/9/28.
 */
public interface YsSdkService {

    /**
     * 校验用户银行卡
     * @param request
     * @return
     */
    YsTrainStationQueryResponse queryTrainStation(final YsTrainStationQueryRequest request);


}
