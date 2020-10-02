package top.weidaboy.controller;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.weidaboy.entity.Message;
import top.weidaboy.service.MessageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class MessageController {
    @Resource
    private  MessageService messageService;

    private static boolean flag = false;

    @RequestMapping("sumbitLiuYan")
    public String sumbitLiuYaN(
            @Param("week")Integer week,
            @Param("content")String content,
            HttpServletRequest request, Model map){
        Message message = new Message();
        message.setWeek(week);
        message.setContent(content);
        //判断是否存在相同内容
        Message have = messageService.isHave(message);
        if(have != null) return "forward:/flushLiuYan";
        else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            message.setTime(df.format(new Date()));
            messageService.insertMessage(message);
        }
        return "forward:/flushLiuYan";
    }

    @RequestMapping("flushLiuYan")
    public String flushLiuYan( HttpServletRequest request){
        //获取所有留言信息
        List<Message> Messages = messageService.getALLMessage();
        request.setAttribute("Messages",Messages);
        return "liuyan";
    }

}
