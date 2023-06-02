package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.client.config.FeignClientConfig;
import digital.moveto.botinok.model.dto.MadeApplyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static digital.moveto.botinok.client.config.ClientConst.MOVE_TO_DIGITAL;

@FeignClient(value = "madeApply", url = MOVE_TO_DIGITAL + "/api/madeApply", configuration = FeignClientConfig.class)
public interface MadeApplyFeignClient {
    @PostMapping("/save")
    void save(@RequestBody MadeApplyDto madeApplyDto);
}
