package group_home.url_shortener.encoder;

import group_home.url_shortener.model.properties.Base62Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Base62Encoder {
    private final Base62Properties base62Properties;

    // принимает список чисел и возвращает список уникальных хешей
    @Async("forkJoinPool")
    public List<String> encode(List<Long> numbers) {
        int base = base62Properties.alphabet().length();
        List<String> result = new ArrayList<>();

        numbers.forEach(number->{
            StringBuilder stringBuilder = new StringBuilder();
            while (number > 0) {
                int remainder = (int) (number % base);
                stringBuilder.append(base62Properties.alphabet().charAt(remainder));
                number /= base;
            }
            result.add(stringBuilder.toString());
        });
        return result;
    }
}