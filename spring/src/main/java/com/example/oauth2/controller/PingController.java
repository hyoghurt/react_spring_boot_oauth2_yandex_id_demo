package com.example.oauth2.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private")
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "GET - pong";
    }

    @PostMapping("/ping")
    public String post(@RequestBody String body) {
        return "POST - pong - ".concat(body);
    }
}
