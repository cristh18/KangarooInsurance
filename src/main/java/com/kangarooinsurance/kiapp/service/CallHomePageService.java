package com.kangarooinsurance.kiapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CallHomePageService implements CommandLineRunner {

    @Autowired
    AppService appService;

    @Override
    public void run(String... args) throws Exception {
        appService.getHomeService();
    }
}
