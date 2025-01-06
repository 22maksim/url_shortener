package group_home.url_shortener.repository;

import group_home.url_shortener.model.properties.UniqueNumberProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HashRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UniqueNumberProperties uniqueNumberProperties;


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Long> getUniqueNumbers() {
        long increment = uniqueNumberProperties.increment();
        String checkExistenceSql = "SELECT COUNT(*) FROM unique_number_seq";
        Optional<Long> count = Optional.ofNullable(jdbcTemplate.queryForObject(checkExistenceSql, Long.class));
        Long number = count.orElse(0L);
        Long numberFinish;
        if (number == 0) {
            // Если строк нет, вставляем
            String insertSql = "INSERT INTO unique_number_seq (number) VALUES (?) RETURNING number";
            numberFinish = jdbcTemplate.queryForObject(insertSql, Long.class, uniqueNumberProperties.start());
        } else {
            // Если строка есть, обновляем
            String updateSql = "UPDATE unique_number_seq SET number = number + ? RETURNING number";
            numberFinish = jdbcTemplate.queryForObject(updateSql, Long.class, increment);
        }
        assert numberFinish != null;

        long startNumber = numberFinish - increment + 1;

        List<Long> numbersList = new ArrayList<>();
        for (long i = startNumber; i <= numberFinish; i++) {
            numbersList.add(i);
        }
        return numbersList;
    }

    @Async
    public void saveAllHash(List<String> hashes) {
        int sizeTotal = hashes.size();
        int sizeBatch = uniqueNumberProperties.batch();
        String sql = "INSERT INTO hash (hash) VALUES (?)";

        for (int i = 0; i < sizeTotal; i += sizeBatch) {
            List<String> batchHashes = hashes.subList(i, Math.min(i + sizeBatch, sizeTotal));
            jdbcTemplate.batchUpdate(sql, batchHashes, sizeBatch, (ps, hash) -> ps.setString(1, hash));
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<String> getRandomHashes() {
        int size = uniqueNumberProperties.increment();
        String sql = "DELETE FROM hash where hash IN (SELECT hash FROM hash ORDER BY RANDOM() limit ?) RETURNING hash";
        return jdbcTemplate.queryForList(sql, String.class, size);
    }
}
