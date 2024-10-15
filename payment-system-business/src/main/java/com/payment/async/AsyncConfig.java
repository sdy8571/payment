package com.payment.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author sdy
 * @description
 * @date 2024/6/12
 */
@Slf4j
@EnableAsync()
@Configuration
public class AsyncConfig {

    public AsyncConfig() {
        log.info("线程池启动");
    }

    public static final String NOTIFY_THREAD_POOL_TASK_EXECUTOR = "NOTIFY_THREAD_POOL_TASK_EXECUTOR";

    public static final List<Integer> NOTIFY_FREQUENCY;

    /**
     * 通知频率，单位为秒。最长通知 2 小时，如果全部失败，则放弃通知
     * 算上首次的通知，实际是一共 1 + 8 = 9 次。
     * ToDo 后续可将通知间隔添加到数据库配置中，以防修改需要重新发布问题
     */
    static {
        Integer[] frequency = { 3, 15, 30, 60, 300, 900, 1800, 3600, 7200 };
        NOTIFY_FREQUENCY = Arrays.asList(frequency);
    }

    @Bean(NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    public ThreadPoolTaskExecutor notifyThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8); // 设置核心线程数
        executor.setMaxPoolSize(16); // 设置最大线程数
        executor.setKeepAliveSeconds(60); // 设置空闲时间
        executor.setQueueCapacity(100); // 设置队列大小
        executor.setThreadNamePrefix("notify-task-"); // 配置线程池的前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 进行加载
        executor.initialize();
        return executor;
    }

}
