package top.weidaboy.service;

import org.apache.ibatis.annotations.Param;
import top.weidaboy.entity.User;

import java.util.List;

public interface UserService {
    public abstract User queryUserByID(int id);

    //检查用户账号、密码是否一致
    public abstract User queryUserByIDandPassword(int id,String password);

    //获取所有用户信息
    List<User> allUser();

    //修改用户密码
    void changePassword(@Param("id") Integer id, @Param("password")String password);

    //查询指定组员的所有信息或不指定
    List<User> queryUserAll(String team);
}
