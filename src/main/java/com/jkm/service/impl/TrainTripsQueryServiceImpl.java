package com.jkm.service.impl;

import com.jkm.controller.helper.request.RequestTrainTripsQuery;
import com.jkm.service.TrainTripsQueryService;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.YsTrainTripsQueryRequest;
import com.jkm.service.ys.entity.YsTrainTripsQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbin on 2016/10/28.
 */
@Service
public class TrainTripsQueryServiceImpl implements TrainTripsQueryService {
    @Autowired
    private YsSdkService ysSdkService;


    @Override
    public List<RequestTrainTripsQuery> select(List<RequestTrainTripsQuery> requestTrainTripsQueries) {
        final YsTrainTripsQueryRequest request = YsTrainTripsQueryRequest.builder().departStationCode("").
                arriveStationCode("").departDate("").build();
        final YsTrainTripsQueryResponse response = this.ysSdkService.queryTrainStation(request);
        if(response == null){
            return null;
        }
        return (List<RequestTrainTripsQuery>) response;
    }
}
