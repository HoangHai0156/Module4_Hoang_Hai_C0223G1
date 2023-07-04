package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGeneralService<Customer, Long> {
    List<Customer> findAllByNameLikeOrAddressLikeOrEmailLikeOrPhoneLike(String name, String address, String email, String phone);

    void setBalance(@Param("amount") BigDecimal amount, @Param("id") Long id);

    void doDeposit(Deposit deposit, Customer customer);

    void doWithdraw(Withdraw withdraw, Customer customer);

    void doTransfer(Transfer transfer, Customer sender, Customer recipient);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);

}
