package top.weidaboy.entity;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String sex;
    private String major;
    private int team;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", major='" + major + '\'' +
                ", team=" + team +
                '}';
    }

    public User(Integer id, String username, String password, String sex, String major, int team) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.major = major;
        this.team = team;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}
