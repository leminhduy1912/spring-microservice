package com.minnduy.customer.customer;

import com.minnduy.customer.exception.CustomerNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;
    public String createCustomer(CustomerRequest request){
        Customer customer = customerRepository.save(mapper.toCustomer(request));
        return customer.getId();
    }
    public void updateCustomer(CustomerRequest request){
        Customer customer = customerRepository.findById(request.getId())
                .orElseThrow(()->
                        new CustomerNotFoundException(
                                String.format("Can not update customer :: Customer not found with ID",request.getId())
                        ));
        mergeCustomer(customer,request);
        customerRepository.save(customer);
    }
    public List<CustomerResponse> findAllCustomers(){
        return customerRepository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }
    public void mergeCustomer(Customer customer,CustomerRequest request){
        if (StringUtils.isNotBlank(request.getFirstName())){
            customer.setFirstname(request.getFirstName());
        }
        if (StringUtils.isNotBlank(request.getLastName())){
            customer.setFirstname(request.getLastName());
        }
        if (StringUtils.isNotBlank(request.getEmail())){
            customer.setFirstname(request.getEmail());
        }
        if (request.getAddress() != null){
            customer.setAddress(request.getAddress());
        }
    }

    public Boolean existById(String id) {
        return customerRepository.findById(id).isPresent();
    }

    public CustomerResponse findById(String id) {
        return customerRepository.findById(id)
                .map(mapper::fromCustomer)
                .orElseThrow(()-> new CustomerNotFoundException(String.format("No customer found with customer ID",id)));
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }
}
