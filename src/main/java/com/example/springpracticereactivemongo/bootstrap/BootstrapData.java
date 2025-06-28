package com.example.springpracticereactivemongo.bootstrap;

import com.example.springpracticereactivemongo.domain.Beer;
import com.example.springpracticereactivemongo.domain.Customer;
import com.example.springpracticereactivemongo.repositories.BeerRepository;
import com.example.springpracticereactivemongo.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * BootstrapData is a Spring Component that implements CommandLineRunner.
 * It is responsible for initializing the database with sample data for Beer and Customer entities.
 * This class deletes all existing data in the repositories and loads new data if the repositories are empty.
 */
@Component
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    /**
     * Constructor for BootstrapData.
     *
     * @param beerRepository     the repository for Beer entities
     * @param customerRepository the repository for Customer entities
     */
    public BootstrapData(BeerRepository beerRepository, CustomerRepository customerRepository) {
        this.beerRepository = beerRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Executes the database initialization logic when the application starts.
     * Deletes all existing data in Beer and Customer repositories and loads new data if the repositories are empty.
     *
     * @param args command-line arguments passed to the application
     * @throws Exception if an error occurs during execution
     */
    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll()
                .doOnSuccess(_ -> loadBeerData())
                .subscribe();

        customerRepository.deleteAll()
                .doOnSuccess(_ -> loadCustomerData())
                .subscribe();
    }

    /**
     * Loads sample Customer data into the repository if it is empty.
     * Creates and saves a list of Customer entities.
     */
    private void loadCustomerData() {
        customerRepository.count().subscribe(count -> {
            if (count == 0) {
                customerRepository.saveAll(List.of(
                        new Customer("John Doe"),
                        new Customer("Jane Doe"),
                        new Customer("Jack Doe")
                )).subscribe();
                System.out.println("Loading customer data...");
            }
        });
    }

    /**
     * Loads sample Beer data into the repository if it is empty.
     * Creates and saves a list of Beer entities.
     */
    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                beerRepository.saveAll(
                        List.of(
                                new Beer("Galaxy Cat", "Pale Ale", "146514", 5, BigDecimal.TEN),
                                new Beer("Crank", "Pale Ale", "32154", 9, BigDecimal.TEN),
                                new Beer("Sunshine City", "IPA", "94546", 10, BigDecimal.TEN)
                        )
                ).subscribe();
                System.out.println("Loading beer data...");
            }
        });
    }
}
