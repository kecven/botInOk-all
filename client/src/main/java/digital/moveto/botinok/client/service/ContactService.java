package digital.moveto.botinok.client.service;

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

    @Transactional
    public void save(Contact contact) {
        contactRepository.save(contact);
    }

    @Transactional
    public void saveAndFlush(Contact contact) {
        contactRepository.saveAndFlush(contact);
    }

    @Transactional
    public Optional<Contact> findByLinkedinUrl(String linkedinUrl) {
        return contactRepository.findByLinkedinUrl(linkedinUrl);
    }
}
