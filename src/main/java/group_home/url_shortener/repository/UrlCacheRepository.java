package group_home.url_shortener.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlCacheRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public String findUrlById(String id) {
        return redisTemplate.opsForValue().get(id);
    }

    public void saveUrl(String hash, String url) {
        redisTemplate.opsForValue().set(hash, url);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
