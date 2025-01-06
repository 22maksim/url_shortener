package group_home.url_shortener.encoder;

import group_home.url_shortener.model.properties.Base62Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Base62Encoder {
    private final Base62Properties base62Properties;

    public List<String> encode(List<Long> numbers) {
        int base = base62Properties.alphabet().length();
        List<String> hashes = new ArrayList<>();

        numbers.forEach(number -> {
            StringBuilder stringBuilder = new StringBuilder();
            while (number > 0) {
                int remainder = (int) (number % base);
                stringBuilder.append(base62Properties.alphabet().charAt(remainder));
                number /= base;
            }
            hashes.add(stringBuilder.toString());
        });
        return hashes;
    }
}
