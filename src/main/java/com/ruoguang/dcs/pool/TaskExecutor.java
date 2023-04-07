package com.ruoguang.dcs.pool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * TaskExecutor
 * 任务线程池
 *
 *  cc
 * @date 2020/06/04
 */
@Slf4j
@Component
public class TaskExecutor {

    /**
     * 装配自定义线城池
     *
     * @return
     */
    @Bean("myTaskExecutor")
    public static Executor myTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数量，线程池创建时候初始化的线程数
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // 最大线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize((int) (executor.getCorePoolSize() * 1.5));
        // 缓冲队列，用来缓冲执行任务的队列
        executor.setQueueCapacity(200);
        // 当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        // 设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("myTaskExecutorThread-");
        // 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setAwaitTerminationSeconds(60);
        // 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池初始化加载
        executor.initialize();
        log.info("MyTaskExecutor loaded and initialize");
        log.info("********** MyTaskExecutor info **********");
        log.info("********** CorePoolSize: {} **********", executor.getCorePoolSize());
        log.info("********** MaxPoolSize: {} **********", executor.getMaxPoolSize());
        log.info("********** KeepAliveSeconds: {} **********", executor.getKeepAliveSeconds());


        return executor;
    }

}
