package com.jkm.service.hy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.MD5Util;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * Created by yuxiang on 2016-11-01.
 */
@Data
public class HyRefundCallbackResponse  {

    private int returntype;
    private String apiorderid;
    private String trainorderid;
    private String reqtoken;
    private List<RefundTicketInfo> returntickets;
    private String token;
    private String returnstate;
    private String returnmoney;
    private String returnmsg;
    private String timestamp;
    private String sign;

    @Data
    public class RefundTicketInfo{

        @JsonProperty("ticket_no")
        private String ticketNo;
        private String passengername;
        private String passporttypeseid;
        private String passportseno;
        private String returnsuccess;
        private String returnmoney;
        private String returntime;
        private String returnfailid;
        private String returnfailmsg;
    }

    /**
     * 签名是否正确
     *
     * @return
     */
    public boolean isSignCorrect() throws Exception {
        if(this.returntype == 0 || this.returntype == 2){
            final String expectSign = MD5Util.MD5(HySdkConstans.PARTNERID + this.returntype + this.timestamp
                    + this.apiorderid + this.trainorderid +this.returnmoney + this.returnstate + MD5Util.MD5(HySdkConstans.SIGN_KEY));
            return Objects.equals(expectSign, sign);
        }else{
            final String expectSign = MD5Util.MD5(HySdkConstans.PARTNERID + this.returntype + this.timestamp
                    + this.apiorderid + this.trainorderid + this.token +this.returnmoney + this.returnstate + MD5Util.MD5(HySdkConstans.SIGN_KEY));
            return Objects.equals(expectSign, sign);
        }
    }
}
