package group_home.url_shortener.repository;

import group_home.url_shortener.model.Url;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, String> {
    String findByHash(@Size(max = 62) String hash);
}
