package com.ruoguang.dcs.listener;


import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 应用启动监听器
 */
@Component
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.profiles.active}")
    private String profiles;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("--- DCS Started at {} ---", DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss"));
        log.info("--- DCS profiles is {} ---", profiles);
        Properties properties = System.getProperties();
        log.info("--- DCS os is {} ---", properties.getProperty("os.name"));
        log.info("--- DCS java-version is {} ---", properties.getProperty("java.version"));
        log.info("--- DCS PID is {} ---", properties.getProperty("PID"));
        log.info("--- DCS timezone is {} ---", properties.getProperty("user.timezone"));

    }

}