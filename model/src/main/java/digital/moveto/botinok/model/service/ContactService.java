package digital.moveto.botinok.model.service;

import digital.moveto.botinok.model.entities.Contact;
import digital.moveto.botinok.model.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;


    @Transactional
    public Contact save(Contact contact) {
        if (contact.getLinkedinUrl() != null && contact.getLinkedinUrl().length() > 0) {
            Optional<Contact> contactOptional = contactRepository.findByLinkedinUrl(contact.getLinkedinUrl());
            if (contactOptional.isPresent()) {
                contact = contactOptional.get().updateFrom(contact);
            }
        }
        if (contact.getId() == null){
            contact.setId(UUID.randomUUID());
        }
        contact = contactRepository.save(contact);
        return contact;
    }

    public List<Contact> findAll(){
        return contactRepository.findAll();
    }

    @Transactional
    public Contact saveAndFlush(Contact contact) {
        contact = save(contact);
        contactRepository.flush();
        return contact;
    }

    @Transactional
    public Optional<Contact> findByLinkedinUrl(String linkedinUrl) {
        return contactRepository.findByLinkedinUrl(linkedinUrl);
    }

    public Optional<Contact> findById(UUID uuid){
        return contactRepository.findById(uuid);
    }

    @Transactional
    public Contact getFullContact(UUID uuid){
        Contact contact = contactRepository.findById(uuid).orElse(null);
        if (contact == null) {
            return null;
        }
        contact.getAccount().getFullName();
        return contact;
    }
}
