import com.google.common.collect.Lists;
import com.jkm.enums.EnumHTHYMethodCode;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.entity.PassengerInfo;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.SnGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yuxiang on 2016-10-31.
 */

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class Test3Des {

  /* @Autowired
    private HySdkService hySdkService;

    @Test
    public void test(){
        PassengerInfo passengerInfo = new PassengerInfo();
        passengerInfo.setTicketNo("E2610890401070051");
        passengerInfo.setPassengerName("王二");
        passengerInfo.setPassportTypeseId(1);
        passengerInfo.setPassportseno("421116198907143795");
        final ArrayList<PassengerInfo> list = Lists.newArrayList();
        list.add(passengerInfo);

        HyReturnTicketRequest request = new HyReturnTicketRequest();
        request.setPartnerId(HySdkConstans.PARTNERID);
        request.setMethod(EnumHTHYMethodCode.RETURN_TICKET.getCode());
        request.setReqTime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
        request.setOrderId(SnGenerator.generate());
        request.setTransactionId(SnGenerator.generate());
        request.setOrderNumber("EC18532964");
        request.setReqToken(SnGenerator.generate());
        request.setCallBackUrl(HySdkConstans.REFUND_TICKET_NOTIFY_URL);
        request.setTickets(list);
        request.setLoginUserName("yudandanweiwu");
        request.setLoginUserPassword("yu609685881");


        final HyReturnTicketResponse hyReturnTicketResponse = this.hySdkService.returnTicket(request);

    }*/
}

