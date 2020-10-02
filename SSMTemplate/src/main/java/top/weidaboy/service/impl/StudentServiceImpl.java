package top.weidaboy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.weidaboy.dao.StudentDao;
import top.weidaboy.entity.Student;
import top.weidaboy.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    public Student login(Student student) {
        return studentDao.login(student);
    }
}
