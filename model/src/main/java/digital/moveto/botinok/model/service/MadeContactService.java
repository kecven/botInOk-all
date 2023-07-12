package digital.moveto.botinok.model.service;

import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.MadeContact;
import digital.moveto.botinok.model.repositories.MadeContactRepository;
import digital.moveto.botinok.model.utils.BotinokUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MadeContactService {

    @Autowired
    private MadeContactRepository madeContactRepository;


    @Transactional
    public MadeContact save(MadeContact madeContact) {
        if (madeContact.getId() == null) {
            madeContact.setId(UUID.randomUUID());
        } else {
            Optional<MadeContact> madeContactDbOptional = madeContactRepository.findById(madeContact.getId());
            if (madeContactDbOptional.isPresent()) {
                madeContact = madeContactDbOptional.get().updateFrom(madeContact);
            }
        }
        madeContact = madeContactRepository.save(madeContact);
        return madeContact;
    }

    public List<MadeContact> findAll(){
        return madeContactRepository.findAll();
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
