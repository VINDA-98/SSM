package top.weidaboy.service.Impl;

import org.springframework.stereotype.Service;
import top.weidaboy.dao.UserDao;
import top.weidaboy.entity.User;
import top.weidaboy.service.UserService;

import javax.annotation.Resource;
import java.util.List;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;


    //通过ID查询用户信息
    @Override
    public User queryUserByID(int id) {
        User user = userDao.queryUserByID(id);
        return user;
    }

    //通过验证账号密码达到登录效果
    @Override
    public User queryUserByIDandPassword(int id, String password) {
         User user = userDao.queryUserByIDandPassword(id,password);
        return user;
    }

    //获取所有用户信息
    @Override
    public  List<User> allUser(){
        List<User> users = userDao.allUser();
        return  users;
    }

    @Override
    public void changePassword(Integer id, String password) {
        userDao.changePassword(id,password);
    }

    @Override
    public List<User> queryUserAll(String team) {
        return userDao.queryUserAll(team);
    }


}
