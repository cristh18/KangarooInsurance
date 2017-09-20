package com.kangarooinsurance.kiapp.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@ComponentScan({"com.kangarooinsurance.kiapp.service"})
public class HomeController {

    @RequestMapping("/kiapp")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/kiapp/list")
    public String homeList() {
        return "list";
    }
}
