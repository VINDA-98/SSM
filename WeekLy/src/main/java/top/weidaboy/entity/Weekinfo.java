package top.weidaboy.entity;

public class Weekinfo {
    private Integer id; //工号
    private Integer week; //周数
    private String time; //时间
    private String content; //周报内容
    private String tcontent;//小组内容
    private String limits;  //时间范围

    public String getLimits() {
        return limits;
    }

    public void setLimits(String limits) {
        this.limits = limits;
    }

    @Override
    public String toString() {
        return "Weekinfo{" +
                "id=" + id +
                ", week=" + week +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", tcontent='" + tcontent + '\'' +
                ", limits='" + limits + '\'' +
                '}';
    }

    public Weekinfo() {
    }

    public Weekinfo(Integer id, Integer week, String time, String content, String tcontent) {
        this.id = id;
        this.week = week;
        this.time = time;
        this.content = content;
        this.tcontent = tcontent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTcontent() {
        return tcontent;
    }

    public void setTcontent(String tcontent) {
        this.tcontent = tcontent;
    }
}
