package group_home.url_shortener.scheduler;

import group_home.url_shortener.service.url.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClearOldUrl {
    private final UrlService urlService;

    @Scheduled(cron = "${cron}")
    public void clearOldUrl() {
        urlService.checkAndCleanOldLinks();
    }
}
