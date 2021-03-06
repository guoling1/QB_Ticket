package com.jkm.service.ys.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * Created by zhangbin on 2016/10/25.
 */
@Data
public class YsTrainStationQueryResponse extends YsSdkResponse{
    /**
     * 响应时间
     *
     */
    private String srvDateTime;

    private Map<String, List<Object>> data;


}
