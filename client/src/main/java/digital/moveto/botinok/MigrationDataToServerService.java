package digital.moveto.botinok;

import digital.moveto.botinok.client.service.*;
import digital.moveto.botinok.model.entities.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MigrationDataToServerService {

    private final ClientAccountService clientAccountService;
    private final ClientCompanyService clientCompanyService;
    private final ClientContactService clientContactService;
    private final ClientMadeApplyService clientMadeApplyService;
    private final ClientMadeContactService clientMadeContactService;

//    @PostConstruct
    public void init(){
        List<Account> all = clientAccountService.findAll();
        all.forEach(account -> clientAccountService.save(account));

        List<Company> companies = clientCompanyService.findAll();
        companies.forEach(company -> clientCompanyService.save(company));

        List<Contact> contacts = clientContactService.findAll();
        contacts.forEach(contact -> clientContactService.save(contact));

        List<MadeApply> madeApplies = clientMadeApplyService.findAll();
        madeApplies.forEach(madeApply -> clientMadeApplyService.save(madeApply));

        List<MadeContact> madeContacts = clientMadeContactService.findAll();
        madeContacts.forEach(madeContact -> clientMadeContactService.save(madeContact));
    }

}
