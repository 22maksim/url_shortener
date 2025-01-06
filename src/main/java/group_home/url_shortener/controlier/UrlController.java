package group_home.url_shortener.controlier;

import group_home.url_shortener.model.dto.HashResponse;
import group_home.url_shortener.model.dto.UrlRequest;
import group_home.url_shortener.service.url.UrlService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/v1/url-shortener/")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @GetMapping("{hash}")
    public ResponseEntity<Void> getUrlByHash(@PathVariable String hash) {
        String url = urlService.getUrlByHash(hash);
        if (url.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json")
                    .body(null);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .body(null);
    }

    @PostMapping("url")
    public HashResponse getHashByUrl(@RequestBody @NotNull @Valid UrlRequest urlRequest) {
        return urlService.getHashByUrl(urlRequest);
    }
}
