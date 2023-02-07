package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.model.dto.AccountDto;
import digital.moveto.botinok.model.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "company", url = "http://localhost:8080/api/company")
public interface CompanyFeignClient {
    @PostMapping("/save")
    CompanyDto save(@RequestBody CompanyDto companyDto);
}
