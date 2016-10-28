import com.google.common.base.Optional;
import com.jkm.entity.OrderFormDetail;
import com.jkm.enums.EnumTrainTicketSeatType;
import com.jkm.service.OrderFormDetailService;
import com.jkm.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml",})
public class TestOrderFormDetailServiceImpl {

    @Autowired
    private OrderFormDetailService orderFormDetailService;

    @Test
    public void testAdd() {
        final OrderFormDetail orderFormDetail = new OrderFormDetail();
        orderFormDetail.setOrderFormId(31);
//        orderFormDetail.setPassengerId("Ys123");
        orderFormDetail.setPassengerName("于翔");
        orderFormDetail.setPassengerType(1);
        orderFormDetail.setCardNo("123456789123456789");
        orderFormDetail.setSeatType(EnumTrainTicketSeatType.BUSINESS_SEAT.getId());
//        orderFormDetail.setSeatName(EnumTrainTicketSeatType.BUSINESS_SEAT.getValue());
        orderFormDetail.setPrice(new BigDecimal("23.2"));
        orderFormDetail.setPickNo("EA12345678");
        orderFormDetail.setJourneyType(1);
        orderFormDetail.setTrainNo("G1001");
        orderFormDetail.setDepartStationCode("beijingbei");
        orderFormDetail.setArriveStationCode("nanjingnan");
        try {
            orderFormDetail.setDepartDateTime(new SimpleDateFormat("yyy-MM-dd HH:mm").parse("2016-10-28 09:12"));
            orderFormDetail.setArriveDateTime(new SimpleDateFormat("yyy-MM-dd HH:mm").parse("2016-11-28 09:12"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        orderFormDetail.setStatus(1);
//        this.orderFormDetailService.add(orderFormDetail);
    }

    @Test
    public void testUpdate() {
        final Optional<OrderFormDetail> orderFormDetailOptional = this.orderFormDetailService.selectById(1);
        final OrderFormDetail orderFormDetail = orderFormDetailOptional.get();
        orderFormDetail.setPassengerId("Ys123");
        orderFormDetail.setSeatName("14 车厢， 084 座");
        this.orderFormDetailService.update(orderFormDetail);
    }

    @Test
    public void testSelect() {
        final Optional<OrderFormDetail> orderFormDetailOptional = this.orderFormDetailService.selectById(1);
        System.out.println(JsonUtil.toJsonString(orderFormDetailOptional.get()));
    }
}
