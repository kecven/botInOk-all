package digital.moveto.botinok;

import digital.moveto.botinok.client.service.*;
import digital.moveto.botinok.model.entities.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MigrationDataToServerService {

    private final AccountService accountService;
    private final CompanyService companyService;
    private final ContactService contactService;
    private final MadeApplyService madeApplyService;
    private final MadeContactService madeContactService;

    @PostConstruct
    public void init(){
        List<Account> all = accountService.findAll();
        all.forEach(account -> accountService.save(account));

        List<Company> companies = companyService.findAll();
        companies.forEach(company -> companyService.save(company));

        List<Contact> contacts = contactService.findAll();
        contacts.forEach(contact -> contactService.save(contact));

        List<MadeApply> madeApplies = madeApplyService.findAll();
        madeApplies.forEach(madeApply -> madeApplyService.save(madeApply));

        List<MadeContact> madeContacts = madeContactService.findAll();
        madeContacts.forEach(madeContact -> madeContactService.save(madeContact));
    }

}
