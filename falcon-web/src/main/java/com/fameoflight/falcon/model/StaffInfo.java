package com.fameoflight.falcon.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffInfo {
    private Long id;
    private String name;
    private String email;
}
