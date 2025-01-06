package group_home.url_shortener.service.cache.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlCacheRedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public String findUrlById(String id) {
        return redisTemplate.opsForValue().get(id);
    }

    public void saveUrl(String hash, String url) {
        redisTemplate.opsForValue().set(hash, url);
    }

    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }
}
