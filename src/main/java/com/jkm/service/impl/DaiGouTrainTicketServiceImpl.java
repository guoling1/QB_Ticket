package com.jkm.service.impl;

import com.jkm.service.DaiGouTrainTicketService;
import com.jkm.service.hy.HySdkService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/11/17.
 */
@Service
public class DaiGouTrainTicketServiceImpl implements DaiGouTrainTicketService {

    @Autowired
    private HySdkService hySdkService;

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param outOrderId
     * @return
     */
    @Override
    public JSONObject queryOrder(final String orderId, final String outOrderId) {
        return this.hySdkService.queryOrder(orderId, outOrderId);
    }
}
