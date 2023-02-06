package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.AccountDto;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountRestController {

    private final Logger log = LoggerFactory.getLogger(AccountRestController.class);

    private final AccountRepository accountRepository;

    @PostMapping("/save")
    AccountDto save(@RequestBody AccountDto accountDto) {
//        return accountRepository.save(new Account());
        return null;
    }

}
