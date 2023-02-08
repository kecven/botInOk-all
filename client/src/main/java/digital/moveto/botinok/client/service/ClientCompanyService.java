package digital.moveto.botinok.client.service;

import digital.moveto.botinok.client.feign.FeignClientService;
import digital.moveto.botinok.model.entities.Company;
import digital.moveto.botinok.model.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientCompanyService extends CompanyService {

    @Autowired
    private FeignClientService feignClientService;

    @Transactional
    public Company save(Company company) {
        company = super.save(company);
        feignClientService.saveCompany(company);
        return company;
    }

    @Transactional
    public Company saveAndFlush(Company company) {
        company = super.saveAndFlush(company);
        feignClientService.saveCompany(company);
        return company;
    }
}
