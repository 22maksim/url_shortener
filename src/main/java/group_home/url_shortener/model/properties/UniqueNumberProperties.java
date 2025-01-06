package group_home.url_shortener.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("unique-number")
public record UniqueNumberProperties(
        int batch, // размер запроса в бд
        Long start, // начинается отсчет с этого числа, не меньше, чтобы хэш был 6 символов
        int increment // На сколько увеличиваем счетчик. Равен меньше 80% от maxSize cache
) {
}
