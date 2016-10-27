import com.alibaba.fastjson.JSON;
import com.jkm.dao.CommonDao;
import com.jkm.dao.JkmPacketGetDao;
import com.jkm.entity.JkmPacketGet;
import com.jkm.entity.User;
import com.jkm.service.IUserService;
import com.jkm.service.JkmPacketGetService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)		//表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml",})

public class TestMyBatis {
    private static Logger logger = Logger.getLogger(TestMyBatis.class);
    @Resource
    private IUserService iUserService;
    @Resource
    private JkmPacketGetDao jkmPacketGetDao;
    @Test
    public void test1() {
        User user = iUserService.getUserById(1);
        JkmPacketGet jkmPacketGet = new JkmPacketGet();
        jkmPacketGet.setState("去");
        jkmPacketGetDao.insertSelective(jkmPacketGet);

    }
}
