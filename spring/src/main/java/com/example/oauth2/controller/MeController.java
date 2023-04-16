package com.example.oauth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class MeController {

    @GetMapping("/me")
    public Map<String, Object> getInfo(Principal principal,
                                       @AuthenticationPrincipal OAuth2User user) {
        if (user != null) {
            return user.getAttributes();
        }
        return Map.of("username", principal.getName());
    }

//    @GetMapping("/me")
//    public Map<String, Object> get(Principal principal,
//                                   @RequestHeader Map<String, String> headers,
//                                   @RequestParam Map<String,String> allParams,
//                                   @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client,
//                                   @AuthenticationPrincipal OAuth2User user) {
//    }
}
