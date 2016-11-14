/*
import com.google.common.collect.Lists;
import com.jkm.entity.PolicyOrder;
import com.jkm.enums.EnumHTHYMethodCode;
import com.jkm.service.PolicyOrderService;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.*;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.SnGenerator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

*/
/**
 * Created by yuxiang on 2016-10-31.
 *//*


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class Test3Des {

//   @Autowired
//    private HySdkService hySdkService;
   @Autowired
   private PolicyOrderService policyOrderService;
//
   @Test
   public void test(){
       this.policyOrderService.batchBuyGrabPolicy(14);
//
//        this.policyOrderService.batchBuyPolicy(127);
   */
/*     HyPostPolicyOrderRequest request = new HyPostPolicyOrderRequest();
        request.setUsername(HySdkConstans.USERNAME);
        request.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
        request.setMethod(EnumHTHYMethodCode.POST_POLICY_ORDER.getCode());
        request.setInsProductNo(HySdkConstans.MAN);
        request.setFlightDate("2016-12-12");
        request.setFlightNumber("C23A1325");
        request.setSerialNo(SnGenerator.generate());
        request.setContractName("任少楠");
        request.setCardType(1);
        request.setCardNo("411224199212155611");
        request.setBirthday("1992-12-15");
        request.setPhone("18813076467");
        request.setGender("M");
        request.setContractType("1");

        HyPostPolicyOrderRequest request1 = new HyPostPolicyOrderRequest();
        request1.setUsername(HySdkConstans.USERNAME);
        request1.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
        request1.setMethod(EnumHTHYMethodCode.POST_POLICY_ORDER.getCode());
        request1.setInsProductNo(HySdkConstans.MAN);
        request1.setFlightDate("2016-12-15");
        request1.setFlightNumber("1C2A13251");
        request1.setSerialNo(SnGenerator.generate());
        request1.setContractName("任少楠");
        request1.setCardType(1);
        request1.setCardNo("411224199212155611");
        request1.setBirthday("1992-12-15");
        request1.setPhone("18813076467");
        request1.setGender("M");
        request1.setContractType("1");

        List<HyPostPolicyOrderRequest> requestList = new ArrayList<>();
        requestList.add(request);
        requestList.add(request1);
        this.hySdkService.postPolicyOrder(requestList);
*//*

       */
/* HyCancelPolicyOrderRequest request = new HyCancelPolicyOrderRequest();
        request.setUsername(HySdkConstans.USERNAME);
        request.setMethod(EnumHTHYMethodCode.CANCEL_POLICY_ORDER.getCode());
        request.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
        request.setPolicyNo("5044809552807120710");

        this.hySdkService.cancelPolicyOrder(request);*//*


       */
/* HyReturnTicketRequest request = new HyReturnTicketRequest();
        request.setPartnerId(HySdkConstans.PARTNERID);
        request.setMethod(EnumHTHYMethodCode.RETURN_TICKET.getCode());
        request.setReqTime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
        request.setOrderId(SnGenerator.generate());
        request.setTransactionId(SnGenerator.generate());
        request.setOrderNumber("EC18532964");
        request.setReqToken(SnGenerator.generate());
        request.setCallBackUrl(HySdkConstans.REFUND_TICKET_NOTIFY_URL);
        request.setLoginUserName("yudandanweiwu");
        request.setLoginUserPassword("yu609685881");
        JSONObject obj = new JSONObject();
        obj.put("passengername" , "王大龙");
        obj.put("passporttypeseid" , "1");
        obj.put("passportseno" , "152201198505080519");
        obj.put("ticket_no" , "E5743415381080001");


        JSONArray jsonArray = new JSONArray();
        jsonArray.add(obj);
        final HyReturnTicketResponse hyReturnTicketResponse = this.hySdkService.returnTicket(request ,jsonArray );*//*


    }
}

*/
