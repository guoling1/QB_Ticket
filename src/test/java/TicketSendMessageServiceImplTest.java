import com.jkm.helper.TicketMessageParams.*;
import com.jkm.service.TicketSendMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yulong.zhang on 2016/11/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class TicketSendMessageServiceImplTest {

    @Autowired
    private TicketSendMessageService ticketSendMessageService;

    @Test
    public void testPayment() {
        final SendPaymentParam sendPaymentParam = new SendPaymentParam();
        sendPaymentParam.setUid("wnl-123456");
        sendPaymentParam.setCode("123456");
        sendPaymentParam.setMobile("18640426296");
        sendPaymentParam.setAmount("100");
        this.ticketSendMessageService.sendPaymentMessage(sendPaymentParam);
    }

    @Test
    public void testBuyTicketSuccess() {
        final SendBuyTicketSuccessParam sendBuyTicketSuccessParam = new SendBuyTicketSuccessParam();
        sendBuyTicketSuccessParam.setUid("wnl-123456");
        sendBuyTicketSuccessParam.setMobile("18640426296");
        sendBuyTicketSuccessParam.setStartStation("北京");
        sendBuyTicketSuccessParam.setEndStation("南京南");
        sendBuyTicketSuccessParam.setStartDate("2016-12-11");
        sendBuyTicketSuccessParam.setStartTime("09:12");
        sendBuyTicketSuccessParam.setTicketNo("E545dfdsf5545454");
        sendBuyTicketSuccessParam.setTrainNo("G22");
        this.ticketSendMessageService.sendBuyTicketSuccessMessage(sendBuyTicketSuccessParam);
    }

    @Test
    public void testBuyTicketFail() {
        final SendBuyTicketFailParam sendBuyTicketFailParam = new SendBuyTicketFailParam();
        sendBuyTicketFailParam.setUid("wnl-123456");
        sendBuyTicketFailParam.setMobile("18640426296");
        sendBuyTicketFailParam.setStartStation("北京");
        sendBuyTicketFailParam.setEndStation("南京南");
        sendBuyTicketFailParam.setTrainNo("G22");
        this.ticketSendMessageService.sendBuyTicketFailMessage(sendBuyTicketFailParam);
    }

    @Test
    public void testGrabTicketSuccessHaveResidueYes() {
        final SendGrabTicketSuccessParam sendGrabTicketSuccessParam = new SendGrabTicketSuccessParam();
        sendGrabTicketSuccessParam.setUid("wnl-123456");
        sendGrabTicketSuccessParam.setMobile("18640426296");
        sendGrabTicketSuccessParam.setStartStation("北京");
        sendGrabTicketSuccessParam.setEndStation("南京南");
        sendGrabTicketSuccessParam.setStartDate("2016-12-11");
        sendGrabTicketSuccessParam.setStartTime("09:12");
        sendGrabTicketSuccessParam.setTicketNo("E545dfdsf5545454");
        sendGrabTicketSuccessParam.setTrainNo("G22");
        sendGrabTicketSuccessParam.setResidueAmount("12.5");
        this.ticketSendMessageService.sendGrabTicketSuccessHaveResidueMessage(sendGrabTicketSuccessParam);
    }

    @Test
    public void testGrabTicketSuccessHaveResidueNo() {
        final SendGrabTicketSuccessParam sendGrabTicketSuccessParam = new SendGrabTicketSuccessParam();
        sendGrabTicketSuccessParam.setUid("wnl-123456");
        sendGrabTicketSuccessParam.setMobile("18640426296");
        sendGrabTicketSuccessParam.setStartStation("北京");
        sendGrabTicketSuccessParam.setEndStation("南京南");
        sendGrabTicketSuccessParam.setStartDate("2016-12-11");
        sendGrabTicketSuccessParam.setStartTime("09:12");
        sendGrabTicketSuccessParam.setTicketNo("E545dfdsf5545454");
        sendGrabTicketSuccessParam.setTrainNo("G22");
        this.ticketSendMessageService.sendGrabTicketSuccessHaveNotResidueMessage(sendGrabTicketSuccessParam);
    }

    @Test
    public void testGrabTicketFail() {
        final SendGrabTicketFailParam sendGrabTicketFailParam = new SendGrabTicketFailParam();
        sendGrabTicketFailParam.setUid("wnl-123456");
        sendGrabTicketFailParam.setMobile("18640426296");
        sendGrabTicketFailParam.setStartStation("北京");
        sendGrabTicketFailParam.setEndStation("南京南");
        sendGrabTicketFailParam.setStartDate("2016-12-11");
        this.ticketSendMessageService.sendGrabTicketFailMessage(sendGrabTicketFailParam);
    }

}
