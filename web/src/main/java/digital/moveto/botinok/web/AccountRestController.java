package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.AccountDto;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static digital.moveto.botinok.WebConst.API_ADDRESS;

@RestController
@RequestMapping(API_ADDRESS + "account")
@RequiredArgsConstructor
public class AccountRestController {

    private final Logger log = LoggerFactory.getLogger(AccountRestController.class);

    private final AccountService accountService;

    @PostMapping("/save")
    public void save(@RequestBody AccountDto accountDto) {
        Account account = accountDto.toEntity();
        log.trace("save({})", account);

        Account saveAccount = accountService.save(account);
    }

}
