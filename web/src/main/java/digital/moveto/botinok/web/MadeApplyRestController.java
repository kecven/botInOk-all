package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.MadeApplyDto;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.MadeApply;
import digital.moveto.botinok.model.repositories.MadeApplyRepository;
import digital.moveto.botinok.model.service.AccountService;
import digital.moveto.botinok.model.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static digital.moveto.botinok.WebConst.API_ADDRESS;

@RestController
@RequestMapping(API_ADDRESS + "madeApply")
@RequiredArgsConstructor
public class MadeApplyRestController {

    private final Logger log = LoggerFactory.getLogger(MadeApplyRestController.class);

    private final MadeApplyRepository madeApplyRepository;
    private final AccountService accountService;
    private final CompanyService companyService;

    @PostMapping("/save")
    public void save(@RequestBody MadeApplyDto madeApplyDto) {
        MadeApply madeApply = madeApplyDto.toEntity();

//        accountService.findById(madeApplyDto.getAccount().getId())
//                .ifPresent(madeApply::setAccount);
//
//        companyService.findById(madeApplyDto.getCompany().getId())
//                .ifPresent(madeApply::setCompany);

        madeApplyRepository.save(madeApply).toDto();
    }

}
