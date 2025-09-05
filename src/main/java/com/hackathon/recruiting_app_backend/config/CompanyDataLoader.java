package com.hackathon.recruiting_app_backend.config;

import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2) // Run second
public class CompanyDataLoader implements CommandLineRunner {
    private final CompanyRepository companyRepository;

    // Populates the database with sample companies
    @Override
    public void run(String... args) throws Exception {
        if (companyRepository.count() == 0) {
            // Tech Companies
            companyRepository.save(Company.builder().name("Google").description("Multinational technology company specializing in Internet-related services and products").email("contact@google.com").website("https://www.google.com").phone("+1-650-253-0000").build());
            companyRepository.save(Company.builder().name("Microsoft").description("Technology company that develops, manufactures, licenses, supports, and sells computer software").email("support@microsoft.com").website("https://www.microsoft.com").phone("+1-425-882-8080").build());
            companyRepository.save(Company.builder().name("Apple").description("Multinational technology company that designs, develops, and sells consumer electronics").email("info@apple.com").website("https://www.apple.com").phone("+1-408-996-1010").build());
            companyRepository.save(Company.builder().name("Amazon").description("Multinational technology company focusing on e-commerce, cloud computing, and artificial intelligence").email("contact@amazon.com").website("https://www.amazon.com").phone("+1-206-266-1000").build());
            companyRepository.save(Company.builder().name("Meta").description("Technology conglomerate that owns Facebook, Instagram, and WhatsApp").email("info@meta.com").website("https://www.meta.com").phone("+1-650-543-4800").build());

            // Financial Services
            companyRepository.save(Company.builder().name("JPMorgan Chase").description("Multinational investment bank and financial services holding company").email("service@jpmorgan.com").website("https://www.jpmorganchase.com").phone("+1-212-270-6000").build());
            companyRepository.save(Company.builder().name("Goldman Sachs").description("Global investment banking, securities, and investment management firm").email("info@gs.com").website("https://www.goldmansachs.com").phone("+1-212-902-1000").build());
            companyRepository.save(Company.builder().name("HSBC").description("British multinational universal bank and financial services holding company").email("contact@hsbc.com").website("https://www.hsbc.com").phone("+44-20-7991-8888").build());
            companyRepository.save(Company.builder().name("Citigroup").description("American multinational investment bank and financial services corporation").email("support@citi.com").website("https://www.citigroup.com").phone("+1-212-559-1000").build());
            companyRepository.save(Company.builder().name("Morgan Stanley").description("American multinational investment bank and financial services company").email("info@morganstanley.com").website("https://www.morganstanley.com").phone("+1-212-761-4000").build());

            // Consulting & Professional Services
            companyRepository.save(Company.builder().name("McKinsey & Company").description("Global management consulting firm").email("contact@mckinsey.com").website("https://www.mckinsey.com").phone("+1-212-446-7000").build());
            companyRepository.save(Company.builder().name("Boston Consulting Group").description("Global management consulting firm").email("info@bcg.com").website("https://www.bcg.com").phone("+1-617-973-1200").build());
            companyRepository.save(Company.builder().name("Bain & Company").description("Global management consulting firm").email("contact@bain.com").website("https://www.bain.com").phone("+1-617-572-2000").build());
            companyRepository.save(Company.builder().name("Deloitte").description("Multinational professional services network").email("support@deloitte.com").website("https://www.deloitte.com").phone("+1-212-492-4000").build());
            companyRepository.save(Company.builder().name("PwC").description("Multinational professional services network of firms").email("contact@pwc.com").website("https://www.pwc.com").phone("+1-646-471-3000").build());

            // Automotive
            companyRepository.save(Company.builder().name("Tesla").description("Electric vehicle and clean energy company").email("support@tesla.com").website("https://www.tesla.com").phone("+1-650-681-5000").build());
            companyRepository.save(Company.builder().name("Toyota").description("Japanese multinational automotive manufacturer").email("info@toyota.com").website("https://www.toyota.com").phone("+81-565-23-2111").build());
            companyRepository.save(Company.builder().name("Volkswagen").description("German automotive manufacturer").email("contact@volkswagen.com").website("https://www.volkswagen.com").phone("+49-5361-90").build());
            companyRepository.save(Company.builder().name("Ford").description("American multinational automobile manufacturer").email("support@ford.com").website("https://www.ford.com").phone("+1-313-322-3000").build());
            companyRepository.save(Company.builder().name("BMW").description("German multinational manufacturer of luxury vehicles").email("info@bmw.com").website("https://www.bmw.com").phone("+49-89-3820").build());

            // Consumer Goods
            companyRepository.save(Company.builder().name("Procter & Gamble").description("American multinational consumer goods corporation").email("contact@pg.com").website("https://www.pg.com").phone("+1-513-983-1100").build());
            companyRepository.save(Company.builder().name("Unilever").description("British multinational consumer goods company").email("info@unilever.com").website("https://www.unilever.com").phone("+44-20-7822-5252").build());
            companyRepository.save(Company.builder().name("Nestlé").description("Swiss multinational food and drink processing conglomerate").email("contact@nestle.com").website("https://www.nestle.com").phone("+41-21-924-2111").build());
            companyRepository.save(Company.builder().name("Coca-Cola").description("American multinational beverage corporation").email("support@coca-cola.com").website("https://www.coca-colacompany.com").phone("+1-404-676-2121").build());
            companyRepository.save(Company.builder().name("PepsiCo").description("American multinational food, snack, and beverage corporation").email("info@pepsico.com").website("https://www.pepsico.com").phone("+1-914-253-2000").build());
        }
    }

}
