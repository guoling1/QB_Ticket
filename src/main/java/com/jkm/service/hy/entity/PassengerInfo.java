package com.jkm.service.hy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by yuxiang on 2016-11-01.
 */
@Data
public class PassengerInfo {

        /**
         * 车票的乘车人信息，乘车人姓名、乘车人证件类型ID和乘车人证件号码
         */
        @JsonProperty("ticket_no")
        private String ticketNo;
        @JsonProperty("passengername")
        private String passengerName;
        @JsonProperty("passporttypeseid")
        private int passportTypeseId;
        @JsonProperty("passportseno")
        private String passportseno;
}
