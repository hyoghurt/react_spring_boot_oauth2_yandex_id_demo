package com.example.oauth2.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private")
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "pong (GET method)";
    }

    @PostMapping("/ping")
    public String post(@RequestBody String body) {
        return "pong (POST method): ".concat(body);
    }
}
