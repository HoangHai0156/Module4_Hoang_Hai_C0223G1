package com.cg.service.customer;

import com.cg.model.*;
import com.cg.model.dto.customer.CustomerResDTO;
import com.cg.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class CustomerService implements ICustomerService{

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Autowired
    private LocationRegionRepository locationRegionRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }


    @Override
    public List<CustomerResDTO> getAllCustomerDTO() {
        return customerRepository.getAllCustomerDTO();
    }

    @Override
    public List<Customer> findAllByFullNameLikeOrEmailLikeOrPhoneLike(String name, String email, String phone) {
        return customerRepository.findAllByFullNameLikeOrEmailLikeOrPhoneLike(name, email, phone);
    }

    @Override
    public void setBalance(BigDecimal amount, Long id) {
        customerRepository.setBalance(amount,id);
    }

    @Override
    public List<Customer> findAllByIdNot(Long id) {
        return customerRepository.findAllByIdNot(id);
    }

    @Override
    public Customer saveNew(Customer customer, LocationRegion locationRegion) {
        LocationRegion newLocationRegion = locationRegionRepository.save(locationRegion);
        customer.setLocationRegion(newLocationRegion);
        return customerRepository.save(customer);
    }

    @Override
    public void doDeposit(Deposit deposit, Customer customer) {
        customerRepository.setBalance(customer.getBalance(),customer.getId());
        depositRepository.save(deposit);
    }

    @Override
    public Transfer doTransfer(Transfer transfer, Customer sender, Customer recipient) {
        customerRepository.setBalance(sender.getBalance(), sender.getId());
        customerRepository.setBalance(recipient.getBalance(), recipient.getId());
        return transferRepository.save(transfer);
    }

    @Override
    public void doWithdraw(Withdraw withdraw, Customer customer) {
        customerRepository.setBalance(customer.getBalance(),customer.getId());
        withdrawRepository.save(withdraw);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return customerRepository.existsByPhone(phone);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return customerRepository.existsByEmailAndIdNot(email,id);
    }

    @Override
    public boolean existsByPhoneAndIdNot(String phone, Long id) {
        return customerRepository.existsByPhoneAndIdNot(phone,id);
    }
}
