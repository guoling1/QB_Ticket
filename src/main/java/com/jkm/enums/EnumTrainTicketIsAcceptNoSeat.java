package com.jkm.enums;

/**
 * Created by yulong.zhang on 2016/10/26.
 *
 * 火车票是否接受无座
 */
public enum EnumTrainTicketIsAcceptNoSeat {

    IS_ACCEPT_NO_SEAT_YES(0, "接受"),

    IS_ACCEPT_NO_SEAT_NO(1, "不接受");

    private int id;

    private String value;

    EnumTrainTicketIsAcceptNoSeat(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
