package com.withins.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VueController implements ErrorController {

    @GetMapping("/error")
    public String index() {
        return "index.html";
    }

}
