package com.fameoflight.falcon.controller;

import com.fameoflight.falcon.model.StudentInfo;
import com.fameoflight.falcon.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    private IStudentService studentService;

    @GetMapping(value = "/student/{id}")
    public StudentInfo getStudent(@PathVariable(value = "id") Integer studentId) {
        return studentService.getStudent(studentId);
    }
}
