package top.weidaboy.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.weidaboy.entity.Weekinfo;
import top.weidaboy.service.WeekinfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("")
public class WeekInfoController {
    @Resource
    private WeekinfoService weekinfoService;

    /*修改小组、个人周报*/
    @RequestMapping("/updateWeekly")
    public String Weekly(@Param("id") Integer id, @Param("week") Integer week, @Param("flag") String flag,
                         @Param("content") String content,@Param("tcontent") String tcontent,
                         @Param("limits") String limits,
                         Model map,
                         HttpSession session, Map<String, Object> error){
        Weekinfo weekinfo = new Weekinfo();
        weekinfo.setId(id);
        weekinfo.setWeek(week);
        //获取修改时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date()).toString();
        weekinfo.setTime(time);
        //weekinfo.setLimits(""); //设置limits
        if(flag.equals("team")){ //如果是小组周报
            weekinfo.setTcontent(tcontent);
            weekinfoService.updateTeamWeekly(weekinfo);
        }else{
            weekinfo.setContent(content);
            weekinfoService.updateWeekly(weekinfo);
        }
        weekinfo = weekinfoService.findWeeklyByIdAndWeek(id, week);
        map.addAttribute("weekinfo",weekinfo);
        map.addAttribute("msg","ok");
        return "forward:/showWeekly";
    }

    @RequestMapping("/refreshWeekly")
    public String refreshWeekly(){
        return "showWeekly";
    }
}
