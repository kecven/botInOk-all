package digital.moveto.botinok.model.service;

import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.MadeApply;
import digital.moveto.botinok.model.repositories.MadeApplyRepository;
import digital.moveto.botinok.model.utils.BotinokUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MadeApplyService {

    @Autowired
    private MadeApplyRepository madeApplyRepository;


    @Transactional
    public MadeApply save(MadeApply madeApply) {
        if (madeApply.getId() == null) {
            madeApply.setId(UUID.randomUUID());
        }
        madeApply = madeApplyRepository.save(madeApply);
        return madeApply;
    }

    public List<MadeApply> findAll(){
        return madeApplyRepository.findAll();
    }

    @Transactional
    public MadeApply saveAndFlush(MadeApply madeApply) {
        madeApply = save(madeApply);
        madeApplyRepository.flush();
        return madeApply;
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