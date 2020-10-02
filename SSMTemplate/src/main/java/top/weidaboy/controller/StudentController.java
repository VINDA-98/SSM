package top.weidaboy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.weidaboy.entity.Student;
import top.weidaboy.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/login")
    @GetMapping
    public String login(Student student){
        return studentService.login(student).toString();
    }

    @RequestMapping("/")
    public String index(){
        return "test";
    }
}
