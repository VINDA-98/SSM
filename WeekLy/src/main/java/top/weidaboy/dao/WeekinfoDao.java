package top.weidaboy.dao;

import org.apache.ibatis.annotations.Param;
import top.weidaboy.entity.Weekinfo;

import java.util.List;

public interface WeekinfoDao {
    /**
     * 通过ID查询到用户信息周报信息
     * @param id
     */
    public Weekinfo findWeeklyById(Integer id);

    /**
     * 通过ID和周数确定某一周周报内容
     * @param id
     * @param week
     * @return
     */
    public Weekinfo findWeeklyByIdAndWeek(@Param("id") Integer id, @Param("week")Integer week);

    /**
     * 通过ID和周数确定这一周是否有周报内容
     * @param id
     * @param week
     * @return
     */
    public boolean isExiste(@Param("id")Integer id,@Param("week")Integer week);

    /**
     * 修改某周周报内容
     * @param weekinfo
     */
    public void updateWeekly(Weekinfo weekinfo);

    /**
     * 获取所有周报周数
     * @return
     */
    public List<Integer> allWeeks(Integer id);


    /**
     * 通过ID查询到用户周报信息
     * @param
     */
    public List<Weekinfo> byIdAndWeek(Integer id);


    /**
     * 添加新的一周周报内容
     * @param weekinfo
     */
    public void addWeekly(Weekinfo weekinfo);

    /**
     * 删除某周周报内容
     * @param id
     * @param week
     */
    public void deleteWeekly(@Param("id")Integer id,@Param("week")Integer week);

    /**
     * 组长更新周报
     * @param weekinfo
     */
    public void updateTeamWeekly(Weekinfo weekinfo);


    /**
     * 获取所有用户周报内容
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


    /**
     * 测试插入使用
     */
    public void insertWeeklyTest(Weekinfo weekinfo);


    /**
     * 测试更新使用
     */
    public void updateTeamWeeklyTest(Weekinfo weekinfo);
    public void updateWeeklyTest(Weekinfo weekinfo);

}
