package digital.moveto.botinok.client.service;

import digital.moveto.botinok.client.feign.FeignClientService;
import digital.moveto.botinok.model.repositories.ContactRepository;
import digital.moveto.botinok.model.entities.Contact;
import digital.moveto.botinok.model.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientContactService extends ContactService {

    @Autowired
    private FeignClientService feignClientService;

    @Transactional
    public Contact save(Contact contact) {
        contact = super.save(contact);
        feignClientService.saveContact(contact);
        return contact;
    }

    @Transactional
    public Contact saveAndFlush(Contact contact) {
        contact = super.saveAndFlush(contact);
        feignClientService.saveContact(contact);
        return contact;
    }

}
