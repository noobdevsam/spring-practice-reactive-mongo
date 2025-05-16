package com.example.springpracticereactivemongo.bootstrap;

import com.example.springpracticereactivemongo.domain.Beer;
import com.example.springpracticereactivemongo.domain.Customer;
import com.example.springpracticereactivemongo.repositories.BeerRepository;
import com.example.springpracticereactivemongo.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {
	
	private final BeerRepository beerRepository;
	private final CustomerRepository customerRepository;
	
	public BootstrapData(BeerRepository beerRepository, CustomerRepository customerRepository) {
		this.beerRepository = beerRepository;
		this.customerRepository = customerRepository;
	}
	
	@Override
	public void run(String... args) throws Exception {
		beerRepository.deleteAll()
			.doOnSuccess(_ -> loadBeerData())
			.subscribe();
		
		customerRepository.deleteAll()
			.doOnSuccess(_ -> loadCustomerData())
			.subscribe();
	}
	
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
