package group_home.url_shortener.service.url;

import group_home.url_shortener.repository.UrlCacheRepository;
import group_home.url_shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final UrlCacheRepository cache;

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
}
