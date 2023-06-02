package digital.moveto.botinok.client.service;

import digital.moveto.botinok.client.feign.FeignClientService;
import digital.moveto.botinok.model.entities.MadeApply;
import digital.moveto.botinok.model.service.MadeApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientMadeApplyService extends MadeApplyService {

    @Autowired
    private FeignClientService feignClientService;


    @Transactional
    public MadeApply save(MadeApply madeApply) {
        madeApply = super.save(madeApply);
        feignClientService.saveMadeApply(madeApply);
        return madeApply;
    }

//    @Transactional
//    public MadeApply saveAndFlush(MadeApply madeApply) {
//        madeApply = save(madeApply);
//        madeApplyRepository.flush();
//        return madeApply;
//    }
}
