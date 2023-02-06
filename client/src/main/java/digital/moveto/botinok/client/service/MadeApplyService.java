package digital.moveto.botinok.client.service;

import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.MadeApply;
import digital.moveto.botinok.model.repositories.MadeApplyRepository;
import digital.moveto.botinok.client.utils.BotinokUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MadeApplyService {

    @Autowired
    private MadeApplyRepository madeApplyRepository;


    @Transactional
    public MadeApply save(MadeApply madeApply) {
        return madeApplyRepository.save(madeApply);
    }

    @Transactional
    public MadeApply saveAndFlush(MadeApply madeApply) {
        return madeApplyRepository.saveAndFlush(madeApply);
    }

    @Transactional
    public List<MadeApply> findAllByAccount(Account account){
        return madeApplyRepository.findAllByAccount(account);
    }

    public int getCountOfTodayForAccount(Account account){
        LocalDate todayLocalDate = LocalDate.now();
        List<MadeApply> allApplyForAccount = findAllByAccount(account);
        final long finalTodayApply = allApplyForAccount.parallelStream()
                .filter(madeApply -> BotinokUtils.equalsDateAndDateTime(todayLocalDate, madeApply.getDate())).count();
        return (int) finalTodayApply;
    }
}
