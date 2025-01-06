package group_home.url_shortener.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "url")
public class Url {
    @Id
    @Size(max = 6)
    @Column(name = "hash", nullable = false, length = 6)
    private String hash;

    @Size(max = 500)
    @NotNull
    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;
}