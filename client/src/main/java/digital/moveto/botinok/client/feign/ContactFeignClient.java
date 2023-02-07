package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.model.dto.ContactDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "contact", url = "https://localhost:8080/api/contact")
public interface ContactFeignClient {
    @PostMapping("/save")
    ContactDto save(@RequestBody ContactDto contactDto);
}
