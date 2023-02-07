package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.model.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class FeignClientService {
    private final AccountFeignClient accountFeignClient;
    private final CompanyFeignClient companyFeignClient;
    private final ContactFeignClient contactFeignClient;
    private final MadeApplyFeignClient madeApplyFeignClient;
    private final MadeContactFeignClient madeContactFeignClient;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void saveAccount(Account account) {
        executorService.submit(() -> {
            accountFeignClient.save(account.toDto());
        });
    }

    public void saveMadeContact(MadeContact madeContact) {
        executorService.submit(() -> {
            madeContactFeignClient.save(madeContact.toDto());
        });
    }

    public void saveMadeApply(MadeApply madeApply) {
        executorService.submit(() -> {
            madeApplyFeignClient.save(madeApply.toDto());
        });
    }

    public void saveContact(Contact contact) {
        executorService.submit(() -> {
            contactFeignClient.save(contact.toDto());
        });
    }

    public void saveCompany(Company company) {
        executorService.submit(() -> {
            companyFeignClient.save(company.toDto());
        });
    }
}
