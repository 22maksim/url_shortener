package group_home.url_shortener.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("async.pools")
public record AsyncProperties(
        int size,
        int maxSize,
        int maxQueueCapacity,
        String taskExecutorPoolPrefix
        ) {
}
