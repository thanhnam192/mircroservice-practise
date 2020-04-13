package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements  CustomerService {
    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Override
    public CustomerDto getCustomerById(UUID id) {
        return CustomerDto.builder().id(UUID.randomUUID()).name("Nam Nguyen").build();
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customerDto) {
        return CustomerDto.builder().id(UUID.randomUUID()).name(customerDto.getName()).build();
    }

    @Override
    public void updateCustomer(UUID id, CustomerDto customerDto) {
        log.info("Update Customer with ID " + id  + " - and Data " + customerDto.toString());
    }

    @Override
    public void deleteCustomer(UUID id) {
        log.info("Delete Customer with ID " + id);
    }
}
