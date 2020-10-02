import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.weidaboy.dao.MessageDao;
import top.weidaboy.entity.Message;
import top.weidaboy.service.MessageService;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springmybatis.xml"})
public class MessageTest {
    @Resource
    private MessageService messageService;

    @Resource
    MessageDao messageDao;
    @Test
    public void test01(){
        Message message = new Message();
        message.setWeek(1);
        message.setContent("加油");
        message.setTime("1.06");
        Message have = messageDao.isHave(message);
        if(have != null)System.out.println("已经存在相同内容了，达哥");
        else messageService.insertMessage(message);
    }

    @Test
    public void test02(){
        List<Message> allMessage = messageService.getALLMessage();
        for (Message message : allMessage) {
            System.out.println(message);
        }
    }


}
