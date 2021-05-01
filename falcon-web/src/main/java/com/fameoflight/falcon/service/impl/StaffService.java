package com.fameoflight.falcon.service.impl;

import com.fameoflight.falcon.domain.Staff;
import com.fameoflight.falcon.domain.Student;
import com.fameoflight.falcon.model.StaffInfo;
import com.fameoflight.falcon.model.StudentInfo;
import com.fameoflight.falcon.repository.StaffRepository;
import com.fameoflight.falcon.repository.StudentRepository;
import com.fameoflight.falcon.service.IStaffService;
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
public class StaffService implements IStaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    @HystrixCommand(fallbackMethod = "getStaffFallBack",
            commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000")},
            threadPoolKey= "StaffServiceThreadPool",
            commandKey="StaffServiceCommand")
    public StaffInfo getStaff(Long staffId) {
        log.info("Fetching staff data from mysql database for request-id : " + UserContextHolder.getContext().getRequestId());
        Optional<Staff> staff = staffRepository.findByStaffId(staffId);
        return StaffInfo.builder()
                .id(staff.get().getStaffId())
                .name(staff.get().getName())
                .email(staff.get().getEmail())
                .build();
    }

    private StaffInfo getStaffFallBack(Long staffId) {
        log.info("Calling fallback for getStaff for request-id : " + UserContextHolder.getContext().getRequestId());
        return StaffInfo.builder()
                .id(1L)
                .name("staff_fallback_name")
                .email("staff_fallback_email")
                .build();
    }
}
