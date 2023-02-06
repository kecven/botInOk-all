package digital.moveto.botinok.client.service;

import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.MadeApply;
import digital.moveto.botinok.model.entities.MadeContact;
import digital.moveto.botinok.model.repositories.MadeContactRepository;
import digital.moveto.botinok.client.utils.BotinokUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MadeContactService {

    @Autowired
    private MadeContactRepository madeContactRepository;

    @Transactional
    public void save(MadeContact madeContact) {
        madeContactRepository.save(madeContact);
    }

    @Transactional
    public List<MadeContact> findAllByAccount(Account account){
        return madeContactRepository.findAllByAccount(account);
    }

    public int getCountOfTodayForAccount(Account account){
        LocalDate todayLocalDate = LocalDate.now();
        List<MadeContact> allContactForAccount = findAllByAccount(account);
        final long finalTodayContact = allContactForAccount.parallelStream()
                .filter(madeContact -> BotinokUtils.equalsDateAndDateTime(todayLocalDate, madeContact.getDate())).count();
        return (int) finalTodayContact;
    }
}
