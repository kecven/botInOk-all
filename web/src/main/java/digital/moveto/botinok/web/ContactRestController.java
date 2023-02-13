package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.ContactDto;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.Contact;
import digital.moveto.botinok.model.service.AccountService;
import digital.moveto.botinok.model.service.ContactService;
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
@RequestMapping(API_ADDRESS + "contact")
@RequiredArgsConstructor
public class ContactRestController {

    private final Logger log = LoggerFactory.getLogger(ContactRestController.class);

    private final ContactService contactService;

    @PostMapping("/save")
    public void save(@RequestBody ContactDto contactDto) {
        Contact contact = contactDto.toEntity();
        log.trace("save({})", contact);

        Optional<Contact> contactDb = contactService.findByLinkedinUrl(contactDto.getLinkedinUrl());
        if (contactDb.isPresent()){

            if (contactDb.get().getParseDate() == null
                    || contactDto.getParseDate() != null && contactDto.getParseDate().isAfter(contactDb.get().getParseDate())) {

                if (Strings.isNotBlank(contactDto.getEmail())){
                    contactDb.get().setEmail(contactDto.getEmail());
                }
                if (Strings.isNotBlank(contactDto.getLocation())){
                    contactDb.get().setLocation(contactDto.getLocation());
                }
                if (Strings.isNotBlank(contactDto.getPhone())){
                    contactDb.get().setPhone(contactDto.getPhone());
                }
                contactService.save(contactDb.get());
                return;
            }
        }

        contactService.save(contact);
    }

}
