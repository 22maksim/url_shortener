package group_home.url_shortener.repository;

import group_home.url_shortener.exception.DataNumberException;
import group_home.url_shortener.model.properties.UniqueNumberProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HashRepository  {
    private final JdbcTemplate jdbcTemplate;
    private final UniqueNumberProperties uniqueNumberProperties;


    public List<Long> getUniqueNumbers() {
        long increment = uniqueNumberProperties.increment();
        String sql = "UPDATE unique_number_seq SET number = number + ? RETURNING number";

        Long numberFinish = jdbcTemplate.queryForObject(sql, Long.class, increment);
        if (numberFinish == null) {
            log.error("No unique numbers found in database");
            throw new DataNumberException("No unique numbers found in database");
        }

        Long startNumber = numberFinish - increment + 1;
        if (uniqueNumberProperties.start() < startNumber) {
            log.info("start numberFinish is less than start numberFinish");
            throw new DataNumberException("start numberFinish is less than start numberFinish");
        }

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

    public List<String> getRandomHashes() {
        int size = uniqueNumberProperties.batch();
        String sql = "DELETE FROM hash where hash IN (SELECT hash FROM hash ORDER BY RANDOM() limit ?) RETURNING hash";
        return jdbcTemplate.queryForList(sql, String.class, size);
    }
}
