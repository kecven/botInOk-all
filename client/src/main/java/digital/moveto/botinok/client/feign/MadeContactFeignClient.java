package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.model.dto.MadeApplyDto;
import digital.moveto.botinok.model.dto.MadeContactDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "madeContact", url = "http://localhost:8080/api/madeContact")
public interface MadeContactFeignClient {
    @PostMapping("/save")
    MadeApplyDto save(@RequestBody MadeContactDto madeContactDto);
}
