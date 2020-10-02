package top.weidaboy.service.Impl;

import org.springframework.stereotype.Service;
import top.weidaboy.dao.MessageDao;
import top.weidaboy.entity.Message;
import top.weidaboy.service.MessageService;

import javax.annotation.Resource;
import java.util.List;

@Service("MessageService")
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageDao messageDao;

    @Override
    public Message isHave(Message message) {
        return messageDao.isHave(message);
    }

    @Override
    public void insertMessage(Message message) {
        messageDao.insertMessage(message);
    }

    @Override
    public List<Message> getALLMessage() {
        return messageDao.getALLMessage();
    }
}
