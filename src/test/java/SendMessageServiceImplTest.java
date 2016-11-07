
import com.jkm.enums.notifier.EnumNoticeType;
import com.jkm.enums.notifier.EnumUserType;
import com.jkm.helper.notifier.SendMessageParams;
import com.jkm.service.notifier.NoticeTemplateInitService;
import com.jkm.service.notifier.SendMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konglingxin on 15/11/4.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class SendMessageServiceImplTest {
//    @Autowired
//    private SendMessageService sendMessageService;
//    @Autowired
//    private NoticeTemplateInitService noticeTemplateInitService;
//
//    @Test
//    public void testSendMessage() throws Exception {
//        this.noticeTemplateInitService.initTemplate();
//        final Map<String, String> data = new HashMap<>();
//        data.put("code", "323233");
//        final SendMessageParams sendMessageParams = SendMessageParams.builder()
//                .uid(1)
//                .noticeType(EnumNoticeType.BIND_MOBILE)
//                .userType(EnumUserType.BACKGROUND_USER)
//                .mobile("18640426296")
//                .addMapData("code", "323233")
//                .build();
//        this.sendMessageService.sendMessage(sendMessageParams);
//        this.sendMessageService.asyncSendInstantMessage(sendMessageParams);
//    }
//
//    @Test
//    public void name() throws Exception {
//
//    }
}