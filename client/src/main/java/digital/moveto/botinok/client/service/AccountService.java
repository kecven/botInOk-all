package digital.moveto.botinok.client.service;

import digital.moveto.botinok.client.feign.FeignClientService;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.enums.Location;
import digital.moveto.botinok.model.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FeignClientService feignClientService;

    @Transactional
    public Account save(Account account) {
        account = accountRepository.save(account);
        feignClientService.saveAccount(account);
        return account;
    }

    @Transactional
    public Account findOrCreateAccountWithContacts(String folder) {
        Account account = accountRepository.findByFolder(folder).orElse(null);
        if (account == null) {
            account = new Account();
            account.setFolder(folder);
            account = save(account);
        }
        if (account.getContacts() == null) {
            account.setContacts(new ArrayList<>());
        }
        account.getContacts().size();
        return account;
    }

    @Transactional
    public Account findById(UUID id) {
        Account account = accountRepository.findById(id).orElse(null);
        account.getContacts().size();
        return account;
    }

    @Transactional
    public Account addNewAccount() {
        Account account = new Account();
        account.setWorkInShabat(true);
        account.setActive(true);
        account.setActiveSearch(true);
        account.setEndDateLicense(LocalDate.now().plusYears(20));
        account.setLocation(Location.ISRAEL);

        account = save(account);
        account.setFolder(account.getId().toString());
        account = save(account);

        return account;
    }



    @Transactional
    public void deleteByFolder(String folder) {
        accountRepository.deleteByFolder(folder);
    }

    @Transactional
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
    @Transactional
    public List<Account> findAllActive() {
        List<Account> all = accountRepository.findAllByActive(true);
        List<Account> result = new ArrayList<>(all.size());

        for (Account account : all) {
            if (account.getEndDateLicense() == null
                    || !account.getEndDateLicense().isBefore(LocalDate.now())) {
                result.add(account);
            }
        }
        return result;
    }
}
