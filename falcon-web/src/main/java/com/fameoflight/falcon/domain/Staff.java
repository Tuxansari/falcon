package com.fameoflight.falcon.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "staff")
public class Staff {

    @Id
    private Integer id;
    @Column(name = "staff_id")
    private Long staffId;
    private String name;
    private String email;
}
