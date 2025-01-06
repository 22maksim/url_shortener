package group_home.url_shortener.configuration.async;

import group_home.url_shortener.model.properties.AsyncProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class ForkJoinPoolAsyncConfig implements AsyncConfigurer {
    private final AsyncProperties asyncProperties;

    @Bean("forkJoinPool")
    ForkJoinPool forkJoinPool() {
        return new ForkJoinPool();
    }

    @Bean(name = "taskExecutor")
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncProperties.size());
        executor.setMaxPoolSize(asyncProperties.maxSize());
        executor.setQueueCapacity(asyncProperties.maxQueueCapacity());
        executor.setThreadNamePrefix(asyncProperties.taskExecutorPoolPrefix());
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return taskExecutor();
    }
}
