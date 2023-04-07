package com.ruoguang.dcs.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Component
public class ScheduTask {


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 举例/示例
     * 每30s执行一次日志打印
     * 打开注解即开启
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 30000)
    public void example() throws ExecutionException, InterruptedException {
        log.info("example now time start-->{}", sdf.format(new Date()));
        log.info("example now time end-->{}", sdf.format(new Date()));
    }


}