package group_home.url_shortener.service.url;

import group_home.url_shortener.exception.DataUrlException;
import group_home.url_shortener.model.Url;
import group_home.url_shortener.model.dto.HashResponse;
import group_home.url_shortener.model.dto.UrlRequest;
import group_home.url_shortener.repository.UrlRepository;
import group_home.url_shortener.service.cache.hashe.HashCache;
import group_home.url_shortener.service.cache.redis.UrlCacheRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final UrlCacheRedisService cache;
    private final HashCache hashCache;

    public String getUrlByHash(String hash) {
        String url = cache.findUrlById(hash);

        if (url.isEmpty()) {
            url = urlRepository.findByHash(hash);
        }

        if (url.isEmpty()) {
            log.error("No url found for hash {}", hash);
        } else {
            log.info("get Url By Hash : {}", url);
        }
        return url;
    }

    public HashResponse getHashByUrl(UrlRequest request) {
        String hash = "";
        try {
            hash = hashCache.getHash();
        } catch (InterruptedException ex) {
            log.error("Hash is not found. Exception: {}", ex.getMessage());
        }
        if (hash.isEmpty()) {
            log.info("No hash found for url {}", request.getUrl());
            throw new DataUrlException("No hash found for url " + request.getUrl());
        }
        Url url = new Url();
        url.setHash(hash);
        url.setUrl(request.getUrl());

        Url result = urlRepository.save(url);
        cache.saveUrl(result.getHash(), request.getUrl());

        log.info("Saved Url : {}", result.getHash());
        return new HashResponse(result.getHash());
    }

    @Async("forkJoinPool")
    public void checkAndCleanOldLinks() {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        List<Url> oldLinks = urlRepository.findAllThatAreOlderThanYear(date);
        List<String> oldHashes = oldLinks.stream()
                .map(Url::getHash)
                .toList();
        cache.delete(oldHashes);
        hashCache.addHashes(oldHashes);
        log.info("Deleted old Urls for links : {}", oldHashes);
    }
}
