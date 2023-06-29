package com.cg.service;

import com.cg.model.Customer;

import java.util.List;

public interface ICustomerService {
    List<Customer> findAll();

    void save(Customer customer);

    Customer findById(Long id);

    void update(Long id, Customer customer);

    void remove(Long id);
}
