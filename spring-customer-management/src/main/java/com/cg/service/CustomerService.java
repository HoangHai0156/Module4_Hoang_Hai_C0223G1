package com.cg.service;

import com.cg.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerService implements ICustomerService{
    private static final Map<Long, Customer> customers;

    static {

        customers = new HashMap<>();
        customers.put(1L, new Customer(1L, "John", "john@codegym.vn", "Hanoi"));
        customers.put(2L, new Customer(2L, "Bill", "bill@codegym.vn", "Danang"));
        customers.put(3L, new Customer(3L, "Alex", "alex@codegym.vn", "Saigon"));
        customers.put(4L, new Customer(4L, "Adam", "adam@codegym.vn", "Beijin"));
        customers.put(5L, new Customer(5L, "Sophia", "sophia@codegym.vn", "Miami"));
        customers.put(6L, new Customer(6L, "Rose", "rose@codegym.vn", "Newyork"));
    }
    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public void save(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    @Override
    public Customer findById(Long id) {
        return customers.get(id);
    }

    @Override
    public void update(Long id, Customer customer) {
        customers.put(id,customer);
    }

    @Override
    public void remove(Long id) {
        customers.remove(id);
    }
}
