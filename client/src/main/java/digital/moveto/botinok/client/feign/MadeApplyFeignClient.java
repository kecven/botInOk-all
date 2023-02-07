package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.model.dto.MadeApplyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "madeApply", url = "http://localhost:8080/api/madeApply")
public interface MadeApplyFeignClient {
    @PostMapping("/save")
    MadeApplyDto save(@RequestBody MadeApplyDto madeApplyDto);
}
