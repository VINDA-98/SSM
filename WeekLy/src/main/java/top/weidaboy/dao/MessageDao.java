package top.weidaboy.dao;

import top.weidaboy.entity.Message;

import java.util.List;

public interface MessageDao {
    /*判断是否存在同样留言*/
    Message isHave(Message message);
    /*添加留言*/
    void insertMessage(Message message);
    //获取所有留言
    List<Message> getALLMessage();
}
