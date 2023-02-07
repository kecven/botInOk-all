package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.CompanyDto;
import digital.moveto.botinok.model.dto.ContactDto;
import digital.moveto.botinok.model.entities.Company;
import digital.moveto.botinok.model.entities.Contact;
import digital.moveto.botinok.model.repositories.CompanyRepository;
import digital.moveto.botinok.model.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static digital.moveto.botinok.WebConst.API_ADDRESS;

@RestController
@RequestMapping(API_ADDRESS + "/contact")
@RequiredArgsConstructor
public class ContactRestController {

    private final Logger log = LoggerFactory.getLogger(ContactRestController.class);

    private final ContactRepository contactRepository;

    @PostMapping("/save")
    public ContactDto save(@RequestBody ContactDto contactDto) {
        Optional<Contact> contact = contactRepository.findById(contactDto.getId());
        if (contact.isPresent()){
            return contactRepository.save(contactDto.toEntity()).toDto();
        }
        contact = contactRepository.findByLinkedinUrl(contactDto.getLinkedinUrl());
        if (contact.isPresent()){
            if (contactDto.getParseDate() != null
                    && contactDto.getParseDate().isAfter(contact.get().getParseDate())) {

                if (Strings.isNotBlank(contactDto.getEmail())){
                    contact.get().setEmail(contactDto.getEmail());
                }
                if (Strings.isNotBlank(contactDto.getLocation())){
                    contact.get().setLocation(contactDto.getLocation());
                }
                if (Strings.isNotBlank(contactDto.getPhone())){
                    contact.get().setPhone(contactDto.getPhone());
                }
                return contactRepository.save(contact.get()).toDto();
            } else {
                return contact.get().toDto();
            }
        }

        return contactRepository.save(contactDto.toEntity()).toDto();
    }

}
