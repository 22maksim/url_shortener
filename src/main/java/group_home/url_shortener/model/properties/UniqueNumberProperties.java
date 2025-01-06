package group_home.url_shortener.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("unique-number")
public record UniqueNumberProperties(
        int batch, // размер кол-ва новых чисел
        Long start, // начинается отсчет с этого числа, не меньше
        int increment
) {
}
