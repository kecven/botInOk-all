package digital.moveto.botinok;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("digital.moveto.botinok.client.feign")
@SpringBootApplication
public class BotinokClientSpringApplication {
}
