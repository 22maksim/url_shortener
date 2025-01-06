package group_home.url_shortener.repository;

import group_home.url_shortener.model.Url;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface UrlRepository extends JpaRepository<Url, String> {
    String findByHash(@Size(max = 62) String hash);

    @Modifying
    @Query(value = """
        DELETE FROM Url u WHERE u.createdAt < :oldData
""")
    List<Url> findAllThatAreOlderThanYear(@Param("oneYearAgo") Timestamp oldData);
}
