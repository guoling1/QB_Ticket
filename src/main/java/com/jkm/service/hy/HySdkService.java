package com.jkm.service.hy;

import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.entity.HySubmitOrderRequest;
import com.jkm.service.hy.entity.HySubmitOrderResponse;

/**
 * Created by yuxiang on 2016-11-01.
 */
public interface HySdkService {

    /**
     * 提交订单
     *
     * @param request
     * @return
     */
    HySubmitOrderResponse submitOrder(HySubmitOrderRequest request);

    /**
     * 线上退票
     */
    HyReturnTicketResponse returnTicket(final HyReturnTicketRequest request);

}
