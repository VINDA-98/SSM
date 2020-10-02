package top.weidaboy.entity;

public class Message {
    private Integer id;
    private Integer week;
    private String content;
    private String reply;
    private String time;


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", week=" + week +
                ", content='" + content + '\'' +
                ", reply='" + reply + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public Message() {
    }

    public Message(Integer id, Integer week, String content, String reply, String time) {
        this.id = id;
        this.week = week;
        this.content = content;
        this.reply = reply;
        this.time = time;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
