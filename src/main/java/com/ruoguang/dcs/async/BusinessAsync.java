package com.ruoguang.dcs.async;


import com.alibaba.fastjson.JSONObject;
import com.ruoguang.dcs.service.IBusinessAsyncService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * BusinessAsync
 * 业务异步
 *
 * @author cc
 * @date 2020/06/04
 */
@Slf4j
@Component
public class BusinessAsync {

    @Autowired
    @Lazy
    private IBusinessAsyncService businessAsyncService;


    /**
     * 示例
     *
     * @return
     */
    @Async("myTaskExecutor")
    public CompletableFuture<String> example() {
        System.out.println(Thread.currentThread().getName());
        log.info("async example");
        return CompletableFuture.completedFuture("完成了async example");
    }

    @Async("myTaskExecutor")
    public CompletableFuture<String> autoPdfParseToAbbsAndHds(String allQueryId, byte[] bytes, int pages, boolean abbsAndHdsAsyn) throws IOException {
        log.info("Thread : {}-->,正在执行 autoPdfParseToAbbsAndHds", Thread.currentThread().getName());
        log.info("allQueryId : {}", allQueryId);
        boolean autoPdfParseToAbbsAndHdsFlag = businessAsyncService.autoPdfParseToAbbsAndHdProcess(allQueryId, bytes, pages, abbsAndHdsAsyn);
        log.info("autoPdfParseToAbbsAndHdsFlag-->{}", autoPdfParseToAbbsAndHdsFlag);
        return CompletableFuture.completedFuture("完成了async autoPdfParseToAbbsAndHds");
    }


    @Async("myTaskExecutor")
    public CompletableFuture<Boolean> autoPdfParseToAbbsAndHdProcessDetail(String redisKey, byte[] bytes, int pageCounter, Map<String, Object> detailsMap, boolean abbsAndHdsAsyn) throws IOException {
        log.info("thread : {}-->,正在执行 autoPdfParseToAbbsAndHdProcessDetail", Thread.currentThread().getName());
        log.info("redisKey->{}", redisKey);
        log.info("pageCounter->{}", pageCounter);
        businessAsyncService.autoPdfParseToAbbsAndHdProcessDetailAsyn(redisKey, bytes, pageCounter, detailsMap);
        return CompletableFuture.completedFuture(true);
    }

}
