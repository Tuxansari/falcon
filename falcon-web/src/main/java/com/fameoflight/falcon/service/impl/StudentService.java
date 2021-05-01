package com.fameoflight.falcon.service.impl;

import com.fameoflight.falcon.domain.Student;
import com.fameoflight.falcon.model.StudentInfo;
import com.fameoflight.falcon.repository.StudentRepository;
import com.fameoflight.falcon.service.IStudentService;
import com.fameoflight.falcon.utils.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@DefaultProperties(commandProperties = {
                @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="3000")})
public class StudentService implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @HystrixCommand(fallbackMethod = "getStudentFallBack",
            commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000")},
            threadPoolKey= "StudentServiceThreadPool",
            commandKey="StudentServiceCommand")
    public StudentInfo getStudent(Integer studentId) {
        log.info("Fetching student data from mongo database for request-id : " + UserContextHolder.getContext().getRequestId());
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        return StudentInfo.builder()
                .id(student.get().getStudentId())
                .name(student.get().getName())
                .email(student.get().getEmail())
                .build();
    }

    private StudentInfo getStudentFallBack(Integer studentId) {
        log.info("Calling fallback for getStudent for request-id : " + UserContextHolder.getContext().getRequestId());
        return StudentInfo.builder()
                .id(1L)
                .name("student_fallback_name")
                .email("student_fallback_email")
                .build();
    }
}
