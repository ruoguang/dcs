package com.ruoguang.dcs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = "com.ruoguang.dcs")
@EnableAsync
@EnableScheduling
public class DCSApplication {

    public static void main(String[] args) {
        SpringApplication.run(DCSApplication.class, args);
    }

}
