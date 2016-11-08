
import com.jkm.entity.notifier.SmsTemplate;
import com.jkm.enums.notifier.EnumNoticeType;
import com.jkm.helper.notifier.NotifierConstants;
import com.jkm.service.notifier.MessageTemplateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by konglingxin on 15/11/4.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class MessageTemplateServiceImplTest {
//    @Autowired
//    private MessageTemplateService messageTemplateService;
//
//    @Test
//    public void testAddTemplate() throws Exception {
//        final SmsTemplate messageTemplate = new SmsTemplate();
//        final String platformName = NotifierConstants.getNotifierConfig().platformName();
//        messageTemplate.setMessageTemplate("您的验证码为${code}，请勿泄漏。【" + platformName + "】");
//        messageTemplate.setNoticeType(102);
//        this.messageTemplateService.addTemplate(messageTemplate);
//        assertThat(this.messageTemplateService.getTemplateByType(EnumNoticeType.MODIFY_PASSWORD).getId() > 0, is(true));
//
//    }
//
//    @Test
//    public void testGetMessageTemplate() throws Exception {
//
//
//    }
//
//    @Test
//    public void testModifyMessageTemplate() throws Exception {
//
//    }
//
//    @Test
//    public void testGetTemplateByType() throws Exception {
//
//    }
//
//    @Test
//    public void testGetMessageTemplateById() throws Exception {
//
//    }
}