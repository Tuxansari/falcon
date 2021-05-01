package com.fameoflight.falcon.controller;

import com.fameoflight.falcon.model.StaffInfo;
import com.fameoflight.falcon.model.StudentInfo;
import com.fameoflight.falcon.service.IStaffService;
import com.fameoflight.falcon.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mysql")
public class MysqlController {

    @Autowired
    private IStaffService staffService;

    @GetMapping(value = "/staff/{id}")
    public StaffInfo getStudent(@PathVariable(value = "id") Long staffId) {
        return staffService.getStaff(staffId);
    }
}
