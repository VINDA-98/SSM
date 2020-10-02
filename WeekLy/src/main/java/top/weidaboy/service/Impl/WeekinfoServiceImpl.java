package top.weidaboy.service.Impl;

import org.springframework.stereotype.Service;
import top.weidaboy.dao.WeekinfoDao;
import top.weidaboy.entity.Weekinfo;
import top.weidaboy.service.WeekinfoService;

import javax.annotation.Resource;
import java.util.List;

@Service("WeekInfoService")
public class WeekinfoServiceImpl implements WeekinfoService {

    @Resource
    private WeekinfoDao weekinfoDao;

    @Override
    /**
     * 通过id和周数获得周报内容
     */
    public Weekinfo findWeeklyByIdAndWeek(Integer id, Integer week)  {
        return weekinfoDao.findWeeklyByIdAndWeek(id,week);
    }

    @Override
    public void updateWeekly(Weekinfo weekinfo) {
        weekinfoDao.updateWeekly(weekinfo);
    }

    /**
     * 获取所有周报周数
     * @return
     */
    @Override
    public List<Integer> allWeeks(Integer id){
        return weekinfoDao.allWeeks(id);
    }

    /**
     * 通过id得到用户的所有周报内容
     */
    @Override
    public List<Weekinfo> byIdAndWeek(Integer id){
        return weekinfoDao.byIdAndWeek(id);
    }

    /**
     * 通过id week查询是否能新建周报数据
     * @param id
     * @param week
     * @return
     */
    @Override
    public boolean isExiste(Integer id, Integer week) {
        return weekinfoDao.isExiste(id,week);
    }

    /**
     * 添加新的周报数据
     * @param weekinfo
     */
    @Override
    public void addWeekly(Weekinfo weekinfo) {
        weekinfoDao.addWeekly(weekinfo);
    }

    /**
     * 删除某周周报内容
     * @param id
     * @param week
     */
    @Override
    public void deleteWeekly(Integer id, Integer week) {
        weekinfoDao.deleteWeekly(id,week);
    }

    @Override
    public void updateTeamWeekly(Weekinfo weekinfo) {
        weekinfoDao.updateTeamWeekly(weekinfo);
    }

    /**
     * 返回所有的周报数据库信息
     * @return
     */
    @Override
    public List<Weekinfo> allWeekly() {
        return  weekinfoDao.allWeekly();
    }

    @Override
    public List<Integer> queryWeeks() {
        return weekinfoDao.queryWeeks();
    }

    @Override
    public List<Weekinfo> queryWeekInfoAll(String week) {
        return weekinfoDao.queryWeekInfoAll(week);
    }

    @Override
    public int MaxWeek() {
        return weekinfoDao.MaxWeek();
    }


}
