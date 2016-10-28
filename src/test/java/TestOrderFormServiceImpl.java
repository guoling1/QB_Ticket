import com.google.common.base.Optional;
import com.jkm.entity.OrderForm;
import com.jkm.service.OrderFormService;
import com.jkm.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class TestOrderFormServiceImpl {

//    @Autowired
//    private OrderFormService orderFormService;


//    @Test
//    public void testAdd() {
//        final OrderForm orderForm = new OrderForm();
//
//        orderForm.setUid(1);
//        orderForm.setQueryKey("YS201610271659123456789");
//        orderForm.setTermTransId("HCP201610271659123456789");
////        orderForm.setTransId();
////        orderForm.setPickNo();
////        orderForm.setInsuranceFee();
////        orderForm.setTechnicalFee();
//        orderForm.setJourneyType(1);
//        orderForm.setTrainNo("G1001");
//        orderForm.setDepartStationCode("beijingbei");
//        orderForm.setArriveStationCode("nanjingnan");
//        try {
//            orderForm.setDepartDateTime(new SimpleDateFormat("yyy-MM-dd HH:mm").parse("2016-10-28 09:12"));
//            orderForm.setArriveDateTime(new SimpleDateFormat("yyy-MM-dd HH:mm").parse("2016-11-28 09:12"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        orderForm.setInsureId(0);
//        orderForm.setInsurePrice(new BigDecimal("0.00"));
//        orderForm.setIsMergeNotice(1);
//        orderForm.setIsAcceptNoSeat(0);
//        orderForm.setContactName("于翔");
//        orderForm.setContactMobile("18640426296");
//        orderForm.setStatus(1);
////        this.orderFormService.add(orderForm);
//    }
//
//    @Test
//    public void testUpdate() {
//        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectById(31);
//        final OrderForm orderForm1 = orderFormOptional.get();
//        orderForm1.setTransId("YS201610271709111111");
//        orderForm1.setPickNo("QP123456789");
//        orderForm1.setInsuranceFee(new BigDecimal("0.00"));
//        orderForm1.setTechnicalFee(new BigDecimal("0.00"));
//        this.orderFormService.update(orderForm1);
//    }
//
//    @Test
//    public void testSelect() {
//        final Optional<OrderForm> optional = this.orderFormService.selectById(31);
//        System.out.println("selectById = " + JsonUtil.toJsonString(optional.get()));
//
//        final Optional<OrderForm> orderFormOptional = this.orderFormService.selectByIdWithLock(31);
//        System.out.println("selectByIdWithLock = " + JsonUtil.toJsonString(orderFormOptional.get()));
//
//        final Optional<OrderForm> orderFormOptional1 = this.orderFormService.selectByTermTransId("HCP201610271659123456789");
//        System.out.println("selectByTermTransId = " + JsonUtil.toJsonString(orderFormOptional1.get()));
//
//        final List<OrderForm> orderForms = this.orderFormService.selectByUid(1);
//        System.out.println("selectByUid = " + JsonUtil.toJsonString(orderForms.get(0)));
//    }
}
