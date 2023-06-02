package digital.moveto.botinok.client.service;

import digital.moveto.botinok.client.feign.FeignClientService;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientAccountService extends AccountService {

    @Autowired
    private FeignClientService feignClientService;

    @Transactional
    public Account save(Account account) {
        account = super.save(account);
        feignClientService.saveAccount(account);
        return account;
    }

}
