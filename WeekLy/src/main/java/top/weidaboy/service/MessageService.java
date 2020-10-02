package top.weidaboy.service;

import top.weidaboy.entity.Message;

import java.util.List;

public interface MessageService {
    /*判断是否存在同样留言*/
    Message isHave(Message message);
    /*添加留言*/
    public void insertMessage(Message message);
    //获取所有留言
    List<Message> getALLMessage();
}
