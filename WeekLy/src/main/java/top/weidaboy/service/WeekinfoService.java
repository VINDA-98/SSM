package top.weidaboy.service;

import top.weidaboy.entity.Weekinfo;

import java.util.List;

public interface WeekinfoService {
    /**
     * 通过ID和周数确定某一周周报内容
     * @param
     * @param
     * @return
     */
    public Weekinfo findWeeklyByIdAndWeek(Integer id, Integer week);

    /**
     * 修改某周周报内容
     * @param
     */
    public void updateWeekly(Weekinfo weekinfo);

    /**
     * 获取所有周报周数
     * @return
     */
    public List<Integer> allWeeks(Integer id);

    /**
     * 通过ID得到周报详细信息
     * @param
     * @return
     */
    public List<Weekinfo> byIdAndWeek(Integer id);

    /**
     * 通过id week查询是否能新建周报数据
     * @param id
     * @param week
     * @return
     */
    public boolean isExiste(Integer id, Integer week);

    /**
     * 添加新的周报数据
     * @param weekinfo
     */
    public void addWeekly(Weekinfo weekinfo);

    /**
     * 删除某周周报内容
     * @param id
     * @param week
     */
    public void deleteWeekly(Integer id, Integer week);


    /**
     * 修改组长周报
     * @param weekinfo
     */
    public void updateTeamWeekly(Weekinfo weekinfo);

    /**
     * 返回所有周报内容
     * @return
     */
    public List<Weekinfo> allWeekly();

    /**
     * 查询所有周数
     * @return List<Integer>
     */
    public List<Integer> queryWeeks();

    /**
     * 获取指定周数或不指定的所有信息
     * @param week
     * @return
     */
    public List<Weekinfo> queryWeekInfoAll(String week);


    /**
     * 获取最新周数
     * @return
     */
    public int MaxWeek();
}
