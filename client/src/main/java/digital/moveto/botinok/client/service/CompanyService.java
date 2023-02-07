package digital.moveto.botinok.client.service;

import digital.moveto.botinok.client.feign.FeignClientService;
import digital.moveto.botinok.model.entities.Company;
import digital.moveto.botinok.model.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FeignClientService feignClientService;

    @Transactional
    public Company save(Company company) {
        company = companyRepository.save(company);
        feignClientService.saveCompany(company);
        return company;
    }

    @Transactional
    public Company saveAndFlush(Company company) {
        company = companyRepository.saveAndFlush(company);
        feignClientService.saveCompany(company);
        return company;
    }

    @Transactional
    public Optional<Company> findByLink(String link) {
        return companyRepository.findByLink(link);
    }
}
