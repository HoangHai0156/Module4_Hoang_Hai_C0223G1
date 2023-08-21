package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.dto.DepositCreReqDTO;
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

@RequestMapping("/api/deposits")
@RestController
public class DepositAPI {

    @Autowired
    ICustomerService customerService;

    @Autowired
    AppUtils appUtils;

    @PostMapping("/{customerId}")
    public ResponseEntity<?> deposit (@PathVariable("customerId") String customerIdStr,
                                      @Valid @RequestBody DepositCreReqDTO depositCreReqDTO,
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
        BigDecimal transactionAmount = BigDecimal.valueOf(Long.parseLong(depositCreReqDTO.getTransactionAmount()));

        customer.setId(customerId);
        customer.setBalance(customer.getBalance().add(transactionAmount));
        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        Deposit deposit = new Deposit();
        deposit.setId(null);
        deposit.setCustomer(customer);
        deposit.setTransactionAmount(transactionAmount);

        customerService.doDeposit(deposit,customer);

        return new ResponseEntity<>(customerResDTO,HttpStatus.OK);
    }
}
