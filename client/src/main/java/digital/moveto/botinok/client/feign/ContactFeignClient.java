package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.model.dto.ContactDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static digital.moveto.botinok.client.config.ClientConst.MOVE_TO_DIGITAL;

@FeignClient(value = "contact", url = MOVE_TO_DIGITAL + "/api/contact")
public interface ContactFeignClient {
    @PostMapping("/save")
    ContactDto save(@RequestBody ContactDto contactDto);
}
