package group_home.url_shortener.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cache")
public record CacheProperties(
        int maxSize, // максимальный размер
        double percentage // остаток в кэше не меньше %
) {
}
