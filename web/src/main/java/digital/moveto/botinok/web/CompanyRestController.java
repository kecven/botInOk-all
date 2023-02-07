package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.CompanyDto;
import digital.moveto.botinok.model.entities.Company;
import digital.moveto.botinok.model.repositories.CompanyRepository;
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
@RequestMapping(API_ADDRESS + "/company")
@RequiredArgsConstructor
public class CompanyRestController {

    private final Logger log = LoggerFactory.getLogger(CompanyRestController.class);

    private final CompanyRepository companyRepository;

    @PostMapping("/save")
    CompanyDto save(@RequestBody CompanyDto companyDto) {
        Optional<Company> findCompanyById = companyRepository.findById(companyDto.getId());
        if (findCompanyById.isPresent()){
            return companyRepository.save(companyDto.toEntity()).toDto();
        }
        if (Strings.isNotBlank(companyDto.getLink())) {
            Optional<Company> companyByLink = companyRepository.findByLink(companyDto.getLink());
            if (companyByLink.isPresent()){
                companyByLink.get().setName(companyDto.getName());
                return companyRepository.save(companyByLink.get()).toDto();
            }
        }

        return companyRepository.save(companyDto.toEntity()).toDto();
    }

}
