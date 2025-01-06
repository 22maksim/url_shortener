package group_home.url_shortener.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequest {
    @URL(message = "Invalid URL format")
    @NotEmpty(message = "Url must not be empty")
    private String url;
}
