package group_home.url_shortener.service.hash.generator;

import group_home.url_shortener.encoder.Base62Encoder;
import group_home.url_shortener.exception.DataNumberException;
import group_home.url_shortener.repository.HashRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashGenerator {
    private final HashRepository hashRepository;
    private final Base62Encoder base62Encoder;

    @Async
    @Transactional
    public void generateBatch() {
        List<Long> numbers = hashRepository.getUniqueNumbers();
        if (numbers.isEmpty()) {
            log.info("No unique numbers found");
            throw new DataNumberException("No unique numbers found");
        }

        List<String> hashes = base62Encoder.encode(numbers);
        if (hashes.isEmpty()) {
            log.info("Failed to create unique hashes");
            throw new DataNumberException("Failed to create unique hashes");
        }
        log.info("Generated {} unique hashes", hashes.size());

        hashRepository.saveAllHash(hashes);
        log.info("Saved {} unique hashes", hashes.size());
    }
}
