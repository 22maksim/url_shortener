package group_home.url_shortener.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("base62")
public record Base62Properties(
        String alphabet
) {
}
