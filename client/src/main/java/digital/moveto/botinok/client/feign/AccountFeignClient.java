package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.model.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "account", url = "http://localhost:8080/api/account")
public interface AccountFeignClient {
    @PostMapping("/save")
    AccountDto save(@RequestBody AccountDto accountDto);
}
