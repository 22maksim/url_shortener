package group_home.url_shortener.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashResponse {
    @NotEmpty
    private String hash;
}
