package group_home.url_shortener.service.cache.hashe;

import group_home.url_shortener.model.properties.CacheProperties;
import group_home.url_shortener.repository.HashRepository;
import group_home.url_shortener.service.hash.generator.HashGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class HashCache implements CommandLineRunner {
    private final Queue<String> cache;
    private final HashRepository hashRepository;
    private final HashGenerator hashGenerator;
    private final ExecutorService myExecutor;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final CountDownLatch closeLatch = new CountDownLatch(1);
    private final CacheProperties cacheProperties;

    @Autowired
    public HashCache(ExecutorService myExecutor, CacheProperties cacheProperties,
                     HashRepository hashRepository, HashGenerator hashGenerator) {
        this.myExecutor = myExecutor;
        this.hashRepository = hashRepository;
        this.hashGenerator = hashGenerator;
        this.cacheProperties = cacheProperties;
        this.cache = new LinkedBlockingQueue<>(cacheProperties.maxSize());
    }

    public String getHash() throws InterruptedException {
        int sizeCache = cache.size();

        if (sizeCache > cacheProperties.maxSize() * cacheProperties.percentage()) {
            return cache.poll();
        } else if (closed.compareAndSet(false, true)) {
            myExecutor.submit(this::loadHashes);
            closeLatch.await();
            return cache.poll();
        }
        closeLatch.await();
        return cache.poll();
    }


    private void loadHashes() {
        try {
            List<String> newHashes = hashRepository.getRandomHashes();

            for (String hash : newHashes) {
                if (cache.size() < cacheProperties.maxSize()) {
                    cache.add(hash);
                }
            }
            hashGenerator.generateBatch();
        } finally {
            closed.set(false);
            closeLatch.countDown();
        }
    }

    public void addHashes(List<String> hashes) {
        try {
            cache.addAll(hashes);
        } catch (IllegalStateException e) {
            log.error("Failed to add items to cache, queue is full.");
        }
    }

    @Override
    public void run(String... args) {
        myExecutor.submit(this::loadHashes);
        log.info("Hashes loaded!!!");
    }
}
