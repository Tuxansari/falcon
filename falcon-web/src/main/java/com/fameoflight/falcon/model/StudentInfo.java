package com.fameoflight.falcon.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentInfo {
    private Long id;
    private String name;
    private String email;
}
