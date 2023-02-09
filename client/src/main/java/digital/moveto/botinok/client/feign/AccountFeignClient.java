package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.client.config.FeignClientConfig;
import digital.moveto.botinok.model.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static digital.moveto.botinok.client.config.ClientConst.MOVE_TO_DIGITAL;

@FeignClient(value = "account", url = MOVE_TO_DIGITAL + "/api/account", configuration = FeignClientConfig.class)
public interface AccountFeignClient {
    @PostMapping("/save")
    AccountDto save(@RequestBody AccountDto accountDto);
}
