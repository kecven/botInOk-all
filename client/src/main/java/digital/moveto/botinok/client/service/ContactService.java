package digital.moveto.botinok.client.service;

import digital.moveto.botinok.client.feign.FeignClientService;
import digital.moveto.botinok.model.repositories.ContactRepository;
import digital.moveto.botinok.model.entities.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private FeignClientService feignClientService;

    @Transactional
    public Contact save(Contact contact) {
        contact = contactRepository.save(contact);
        feignClientService.saveContact(contact);
        return contact;
    }

    @Transactional
    public Contact saveAndFlush(Contact contact) {
        contact = contactRepository.saveAndFlush(contact);
        feignClientService.saveContact(contact);
        return contact;
    }

    @Transactional
    public Optional<Contact> findByLinkedinUrl(String linkedinUrl) {
        return contactRepository.findByLinkedinUrl(linkedinUrl);
    }
}
