import com.google.common.collect.Lists;
import com.jkm.enums.EnumHTHYMethodCode;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.entity.PassengerInfo;
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

/**
 * Created by yuxiang on 2016-10-31.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class Test3Des {

   @Autowired
    private HySdkService hySdkService;

    @Test
    public void test(){


        HyReturnTicketRequest request = new HyReturnTicketRequest();
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
        final HyReturnTicketResponse hyReturnTicketResponse = this.hySdkService.returnTicket(request ,jsonArray );

    }
}

