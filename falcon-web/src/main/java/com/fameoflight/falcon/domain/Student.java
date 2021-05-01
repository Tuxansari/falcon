package com.fameoflight.falcon.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Data
@Document(collection = "student")
public class Student {

    @Id
    private String id;
    @Indexed(unique = true)
    private Long studentId;
    private String name;
    private String email;
}
