package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-10-28.
 */
public enum EnumBusinessType {

    STATION_LIST_SERVICE(1, "站点查询" , "StationListService.cgi"),
    TRAIN_LIST_SERVICE(2, "车次查询", "TrainListService.cgi"),
    BOOK_TICKET_SERVICE(3, "订票请求", "BookTicketService.cgi"),
    BOOK_TICKET_CALL_BACK(4, "订票回调", ""),
    REFUND_TICKET_SERVICE(5, "退票请求", "RefundTicketService.cgi"),
    REFUND_TICKET_CALL_BACK(6, "退票回调", "");

    @Getter
    private int id;
    @Getter
    private String type;
    @Getter
    private String address;

    EnumBusinessType(final int id, final String type, final String address){
        this.id = id;
        this.type = type;
        this.address = address;
    }
}
