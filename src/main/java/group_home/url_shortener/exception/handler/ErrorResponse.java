package group_home.url_shortener.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    // Геттеры и сеттеры
    private String errorCode;
    private String errorMessage;
}
