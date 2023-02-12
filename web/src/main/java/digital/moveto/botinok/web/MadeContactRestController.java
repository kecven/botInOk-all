package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.MadeContactDto;
import digital.moveto.botinok.model.entities.MadeContact;
import digital.moveto.botinok.model.repositories.MadeContactRepository;
import digital.moveto.botinok.model.service.AccountService;
import digital.moveto.botinok.model.service.MadeContactService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static digital.moveto.botinok.WebConst.API_ADDRESS;

@RestController
@RequestMapping(API_ADDRESS + "madeContact")
@RequiredArgsConstructor
public class MadeContactRestController {

    private final Logger log = LoggerFactory.getLogger(MadeContactRestController.class);
    private final MadeContactService madeContactService;

    @PostMapping("/save")
    public void save(@RequestBody MadeContactDto madeContactDto) {
        MadeContact entity = madeContactDto.toEntity();

        madeContactService.save(entity).toDto();
    }

}
