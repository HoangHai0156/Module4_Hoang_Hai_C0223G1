package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerResDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {

    @Autowired
    ICustomerService customerService;

    @Autowired
    AppUtils appUtils;

    @GetMapping
    public ResponseEntity<?> getAllCustomer(){
        List<CustomerResDTO> customerResDTOS = customerService.getAllCustomerDTO();

        return new ResponseEntity<>(customerResDTOS,HttpStatus.OK);
    }

    @GetMapping("/id_ne/{customerId}")
    public ResponseEntity<?> getAllNotId(@PathVariable Long customerId){
        List<Customer> customers = customerService.findAllByIdNot(customerId);

        List<CustomerResDTO> customerResDTOS = getCustomerResDTOSFromEntity(customers);

        return new ResponseEntity<>(customerResDTOS,HttpStatus.OK);
    }

    @GetMapping("/search/{kw}")
    public ResponseEntity<?> searchByKw(@PathVariable String kw){
        kw = "%" + kw + "%";
        List<Customer> customers = customerService.findAllByFullNameLikeOrAddressLikeOrEmailLikeOrPhoneLike(kw, kw, kw, kw);

        List<CustomerResDTO> customerResDTOS = getCustomerResDTOSFromEntity(customers);

        return new ResponseEntity<>(customerResDTOS,HttpStatus.OK);
    }

    private static List<CustomerResDTO> getCustomerResDTOSFromEntity(List<Customer> customers) {
        List<CustomerResDTO> customerResDTOS = new ArrayList<>();
        for (Customer customer: customers){
            customerResDTOS.add(customer.toCustomerResDTO());
        }
        return customerResDTOS;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        Customer newCustomer = customerService.save(customer);

        return new ResponseEntity<>(newCustomer,HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId){
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerOptional.get();
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<?> editCustomer(@PathVariable Long customerId,
                                          @Valid @RequestBody Customer customer,
                                          BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Customer currentCustomer = customerOptional.get();
        customer.setId(customerId);
        customer.setBalance(currentCustomer.getBalance());
        Customer newCustomer = customerService.save(customer);

        return new ResponseEntity<>(newCustomer,HttpStatus.OK);
    }

    @PostMapping("/del/{customerId}")
    public ResponseEntity<?> removeCustomer(@PathVariable Long customerId){
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerOptional.get();
        customer.setId(customerId);
        customer.setDeleted(true);
        Customer newCustomer = customerService.save(customer);

        return new ResponseEntity<>(newCustomer,HttpStatus.OK);
    }
}
