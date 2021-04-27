package com.fameoflight.falcon.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping(value = "/ping")
    public String ping() {
        return "pong";
    }

}
