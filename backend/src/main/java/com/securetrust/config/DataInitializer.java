package com.securetrust.config;

import com.securetrust.model.Account;
import com.securetrust.model.AccountType;
import com.securetrust.model.Customer;
import com.securetrust.repository.AccountRepository;
import com.securetrust.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepo, AccountRepository accountRepo) {
        return args -> {
            // Check if data already exists
            if (customerRepo.count() > 0) {
                return;
            }

            // Create customers
            Customer john = new Customer();
            john.setCustomerId("CUST001");
            john.setFirstName("John");
            john.setSurname("Doe");
            john.setAddress("123 Main Street, Gaborone");
            john.setPhoneNumber("71123456");
            john.setEmail("john.doe@securetrust.com");
            customerRepo.save(john);

            Customer jane = new Customer();
            jane.setCustomerId("CUST002");
            jane.setFirstName("Jane");
            jane.setSurname("Smith");
            jane.setAddress("456 Broad Street, Francistown");
            jane.setPhoneNumber("72123456");
            jane.setEmail("jane.smith@securetrust.com");
            customerRepo.save(jane);
            
            Customer bob = new Customer();
            bob.setCustomerId("CUST003");
            bob.setFirstName("Bob");
            bob.setSurname("Johnson");
            bob.setAddress("789 Market Road, Maun");
            bob.setPhoneNumber("73123456");
            bob.setEmail("bob.johnson@securetrust.com");
            customerRepo.save(bob);

            // Create accounts
            Account account1 = new Account();
            account1.setCustomer(john);
            account1.setAccountType(AccountType.SAVINGS);
            account1.setAccountNumber("SAV001");
            account1.setBalance(12500.00);
            account1.setBranch("Main Branch");
            accountRepo.save(account1);

            Account account2 = new Account();
            account2.setCustomer(john);
            account2.setAccountType(AccountType.INVESTMENT);
            account2.setAccountNumber("INV001");
            account2.setBalance(8500.50);
            account2.setBranch("Main Branch");
            accountRepo.save(account2);

            Account account3 = new Account();
            account3.setCustomer(jane);
            account3.setAccountType(AccountType.CHEQUE);
            account3.setAccountNumber("CHQ001");
            account3.setBalance(34200.75);
            account3.setBranch("City Branch");
            account3.setEmployer("Tech Solutions Ltd");
            account3.setEmployerAddress("Gaborone");
            accountRepo.save(account3);
            
            Account account4 = new Account();
            account4.setCustomer(bob);
            account4.setAccountType(AccountType.SAVINGS);
            account4.setAccountNumber("SAV002");
            account4.setBalance(5000.00);
            account4.setBranch("North Branch");
            accountRepo.save(account4);

            System.out.println("Initial data loaded successfully!");
            System.out.println("Created " + customerRepo.count() + " customers and " + accountRepo.count() + " accounts");
        };
    }
}
