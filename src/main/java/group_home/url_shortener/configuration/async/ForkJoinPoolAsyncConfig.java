package group_home.url_shortener.configuration.async;

import group_home.url_shortener.model.properties.AsyncProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class ForkJoinPoolAsyncConfig implements AsyncConfigurer {
    private final AsyncProperties asyncProperties;

    @Bean("forkJoinPool")
    ForkJoinPool forkJoinPool() {
        return new ForkJoinPool();
    }

    @Bean("myExecutor")
    ExecutorService myExecutor() {
        return Executors.newFixedThreadPool(5);
    }

    @Bean(name = "taskExecutor")
    TaskExecutor taskExecutorPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncProperties.size());
        executor.setMaxPoolSize(asyncProperties.maxSize());
        executor.setQueueCapacity(asyncProperties.maxQueueCapacity());
        executor.setThreadNamePrefix(asyncProperties.taskExecutorPoolPrefix());
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return taskExecutorPool();
    }
}
