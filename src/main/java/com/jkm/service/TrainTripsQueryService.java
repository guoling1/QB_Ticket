package com.jkm.service;

import com.jkm.controller.helper.request.RequestTrainTripsQuery;

import java.util.List;

/**
 * Created by zhangbin on 2016/10/28.
 */
public interface TrainTripsQueryService {
    /**
     *
     * @param requestTrainTripsQueries
     * @return
     */
    List<RequestTrainTripsQuery> select(List<RequestTrainTripsQuery> requestTrainTripsQueries);


}
