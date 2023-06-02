package digital.moveto.botinok.client.feign;

import digital.moveto.botinok.client.config.GlobalConfig;
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

    private final GlobalConfig globalConfig;

    private final ContactService contactService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void saveAccount(Account account) {
        if (globalConfig.sendDataToServer) {
            executorService.submit(() -> {
                try {
                    log.trace("saveAccount: {}", account);
                    accountFeignClient.save(account.toDto());
                } catch (Exception e) {
                    log.error("saveAccount: {}", account, e);
                }
            });
        }
    }

    public void saveMadeContact(MadeContact madeContact) {
        if (globalConfig.sendDataToServer) {
            executorService.submit(() -> {
                try {
                    log.trace("saveMadeContact: {}", madeContact);
                    madeContactFeignClient.save(madeContact.toDto());
                } catch (Exception e) {
                    log.error("saveMadeContact: {}", madeContact, e);
                }
            });
        }
    }

    public void saveMadeApply(MadeApply madeApply) {
        if (globalConfig.sendDataToServer) {
            executorService.submit(() -> {
                try {
                    log.trace("saveMadeApply: {}", madeApply);
                    madeApplyFeignClient.save(madeApply.toDto());
                } catch (Exception e) {
                    log.error("saveMadeApply: {}", madeApply, e);
                }
            });
        }
    }

    public void saveContact(Contact contact) {
        if (globalConfig.sendDataToServer) {
            executorService.submit(() -> {
                try {
                    log.trace("saveContact: {}", contact);
                    contactFeignClient.save(contact.toDto());
                } catch (Exception e) {
                    log.error("saveContact: {}", contact, e);
                }
            });
        }
    }

    public void saveCompany(Company company) {
        if (globalConfig.sendDataToServer) {
            executorService.submit(() -> {
                try {
                    log.trace("saveCompany: {}", company);
                    companyFeignClient.save(company.toDto());
                } catch (Exception e) {
                    log.error("saveCompany: {}", company, e);
                }
            });
        }
    }
}
