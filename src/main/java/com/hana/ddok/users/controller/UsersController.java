package com.hana.ddok.users.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class UsersController {

    @PostMapping("/")
    public String test() {
        return "hi";
    }

    @PostMapping("/q")
    public String test1() {
        return "hi";
    }
}
