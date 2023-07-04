package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import com.cg.repository.TransferRepository;
import com.cg.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<Customer> findAllByNameLikeOrAddressLikeOrEmailLikeOrPhoneLike(String name, String address, String email, String phone) {
        return customerRepository.findAllByNameLikeOrAddressLikeOrEmailLikeOrPhoneLike(name,address,email,phone);
    }

    @Override
    public void setBalance(BigDecimal amount, Long id) {
        customerRepository.setBalance(amount,id);
    }

    @Override
    public void doDeposit(Deposit deposit, Customer customer) {
        customerRepository.setBalance(customer.getBalance(),customer.getId());
        depositRepository.save(deposit);
    }

    @Override
    public void doTransfer(Transfer transfer, Customer sender, Customer recipient) {
        customerRepository.setBalance(sender.getBalance(), sender.getId());
        customerRepository.setBalance(recipient.getBalance(), recipient.getId());
        transferRepository.save(transfer);
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
