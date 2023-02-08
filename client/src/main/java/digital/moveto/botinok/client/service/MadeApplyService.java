package digital.moveto.botinok.client.service;

import digital.moveto.botinok.client.feign.FeignClientService;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.MadeApply;
import digital.moveto.botinok.model.repositories.MadeApplyRepository;
import digital.moveto.botinok.client.utils.BotinokUtils;
import digital.moveto.botinok.model.repositories.MadeContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MadeApplyService {

    @Autowired
    private MadeApplyRepository madeApplyRepository;

    @Autowired
    private FeignClientService feignClientService;


    @Transactional
    public MadeApply save(MadeApply madeApply) {
        madeApply = madeApplyRepository.save(madeApply);
        feignClientService.saveMadeApply(madeApply);
        return madeApply;
    }

    public List<MadeApply> findAll(){
        return madeApplyRepository.findAll();
    }

    @Transactional
    public MadeApply saveAndFlush(MadeApply madeApply) {
        madeApply = madeApplyRepository.saveAndFlush(madeApply);
        feignClientService.saveMadeApply(madeApply);
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
