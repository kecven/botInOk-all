package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.CompanyDto;
import digital.moveto.botinok.model.entities.Company;
import digital.moveto.botinok.model.service.CompanyService;
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
@RequestMapping(API_ADDRESS + "company")
@RequiredArgsConstructor
public class CompanyRestController {

    private final Logger log = LoggerFactory.getLogger(CompanyRestController.class);

    private final CompanyService companyService;

    @PostMapping("/save")
    public void save(@RequestBody CompanyDto companyDto) {
        if (Strings.isNotBlank(companyDto.getLink())) {
            Optional<Company> companyByLink = companyService.findByLink(companyDto.getLink());
            if (companyByLink.isPresent()){
                companyByLink.get().updateFrom(companyDto.toEntity());
                companyService.save(companyByLink.get()).toDto();
            }
        }

        Optional<Company> findCompanyById = companyService.findById(companyDto.getId());

        companyService.save(companyDto.toEntity()).toDto();
    }

}
