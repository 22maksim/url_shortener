package group_home.url_shortener.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "url")
public class Url {
    @Id
    @Size(max = 62)
    @Column(name = "hash", nullable = false, length = 62)
    private String hash;

    @Size(max = 500)
    @NotNull
    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "created_at")
    private Instant createdAt;

}