package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.model.dto.ContactDto;
import digital.moveto.botinok.model.entities.*;
import digital.moveto.botinok.model.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class FeignClientService {
    private Logger log = LoggerFactory.getLogger(FeignClientService.class);

    private final AccountFeignClient accountFeignClient;
    private final CompanyFeignClient companyFeignClient;
    private final ContactFeignClient contactFeignClient;
    private final MadeApplyFeignClient madeApplyFeignClient;
    private final MadeContactFeignClient madeContactFeignClient;

    private final ContactService contactService;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    public void saveAccount(Account account) {
        executorService.submit(() -> {
            log.trace("saveAccount: {}", account);
            accountFeignClient.save(account.toDto());
        });
    }

    public void saveMadeContact(MadeContact madeContact) {
        executorService.submit(() -> {
            log.trace("saveMadeContact: {}", madeContact);
            madeContactFeignClient.save(madeContact.toDto());
        });
    }

    public void saveMadeApply(MadeApply madeApply) {
        executorService.submit(() -> {
            log.trace("saveMadeApply: {}", madeApply);
            madeApplyFeignClient.save(madeApply.toDto());
        });
    }

    public void saveContact(Contact contact) {
        executorService.submit(() -> {
            log.trace("saveContact: {}", contact);
            contactFeignClient.save(contact.toDto());
        });
    }

    public void saveCompany(Company company) {
        executorService.submit(() -> {
            log.trace("saveCompany: {}", company);
            companyFeignClient.save(company.toDto());
        });
    }
}
