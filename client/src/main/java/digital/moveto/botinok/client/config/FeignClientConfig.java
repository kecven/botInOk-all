package digital.moveto.botinok.client.config;

import feign.Feign;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfig {

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .retryer(new Retryer.Default(100, TimeUnit.SECONDS.toMillis(120), 20));
    }
}
