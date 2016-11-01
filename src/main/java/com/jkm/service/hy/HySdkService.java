package com.jkm.service.hy;

import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;

/**
 * Created by yuxiang on 2016-11-01.
 */
public interface HySdkService {


    /**
     * 线上退票
     */
    HyReturnTicketResponse returnTicket(final HyReturnTicketRequest request);

}
