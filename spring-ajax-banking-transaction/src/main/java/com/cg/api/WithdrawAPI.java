package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Withdraw;
import com.cg.model.dto.DepositCreReqDTO;
import com.cg.model.dto.WithdrawCreReqDTO;
import com.cg.model.dto.customer.CustomerResDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/withdraws")
public class WithdrawAPI {
    @Autowired
    ICustomerService customerService;

    @Autowired
    AppUtils appUtils;

    @PostMapping("/{customerId}")
    public ResponseEntity<?> withdraw (@PathVariable("customerId") String customerIdStr,
                                      @Valid @RequestBody WithdrawCreReqDTO withdrawCreReqDTO,
                                      BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (!ValidateUtil.isNumberValid(customerIdStr)){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không hợp lệ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long customerId = Long.parseLong(customerIdStr);
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerOptional.get();
        BigDecimal transactionAmount = BigDecimal.valueOf(Long.parseLong(withdrawCreReqDTO.getTransactionAmount()));

        if (transactionAmount.compareTo(customer.getBalance()) > 0){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Số dư không đủ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        customer.setId(customerId);
        customer.setBalance(customer.getBalance().subtract(transactionAmount));
        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        Withdraw withdraw = new Withdraw();
        withdraw.setId(null);
        withdraw.setCustomer(customer);
        withdraw.setTransactionAmount(transactionAmount);

        customerService.doWithdraw(withdraw,customer);

        return new ResponseEntity<>(customerResDTO,HttpStatus.OK);
    }
}
