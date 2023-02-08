package digital.moveto.botinok.client.service;

import digital.moveto.botinok.client.feign.FeignClientService;
import digital.moveto.botinok.model.entities.MadeContact;
import digital.moveto.botinok.model.service.MadeContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientMadeContactService extends MadeContactService {

    @Autowired
    private FeignClientService feignClientService;

    @Transactional
    public MadeContact save(MadeContact madeContact) {
        madeContact = super.save(madeContact);
        feignClientService.saveMadeContact(madeContact);
        return madeContact;
    }

}
