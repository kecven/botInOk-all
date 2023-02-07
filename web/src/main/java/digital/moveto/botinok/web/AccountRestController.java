package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.AccountDto;
import digital.moveto.botinok.model.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static digital.moveto.botinok.WebConst.API_ADDRESS;

@RestController
@RequestMapping(API_ADDRESS + "account")
@RequiredArgsConstructor
public class AccountRestController {

    private final Logger log = LoggerFactory.getLogger(AccountRestController.class);

    private final AccountRepository accountRepository;

    @PostMapping("/save")
    public AccountDto save(@RequestBody AccountDto accountDto) {
        return accountRepository.save(accountDto.toEntity()).toDto();
    }

}
