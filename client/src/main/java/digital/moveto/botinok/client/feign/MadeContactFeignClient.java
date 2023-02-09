package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.client.config.FeignClientConfig;
import digital.moveto.botinok.model.dto.MadeApplyDto;
import digital.moveto.botinok.model.dto.MadeContactDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static digital.moveto.botinok.client.config.ClientConst.MOVE_TO_DIGITAL;

@FeignClient(value = "madeContact", url = MOVE_TO_DIGITAL + "/api/madeContact", configuration = FeignClientConfig.class)
public interface MadeContactFeignClient {
    @PostMapping("/save")
    MadeApplyDto save(@RequestBody MadeContactDto madeContactDto);
}
