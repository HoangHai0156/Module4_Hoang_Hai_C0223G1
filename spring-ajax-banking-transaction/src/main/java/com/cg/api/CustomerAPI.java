package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.LocationRegion;
import com.cg.model.dto.customer.CustomerCreReqDTO;
import com.cg.model.dto.customer.CustomerCreResDTO;
import com.cg.model.dto.customer.CustomerResDTO;
import com.cg.model.dto.location.LocationRegionCreReqDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.location.ILocationRegionService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtil;
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
    ILocationRegionService locationRegionService;

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
        List<Customer> customers = customerService.findAllByFullNameLikeOrEmailLikeOrPhoneLike(kw, kw, kw);

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
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerCreReqDTO customerCreReqDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        String email = customerCreReqDTO.getEmail();
        if (customerService.existsByEmailAndIdNot(email, -1L)){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Email đã tồn tại, hãy nhập email khác");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        LocationRegionCreReqDTO locationRegionCreReqDTO = customerCreReqDTO.getLocationRegion();
        LocationRegion locationRegion = locationRegionCreReqDTO.toLocationRegion(null);

        Customer customer = customerCreReqDTO.toCustomer(null,BigDecimal.ZERO);

        Customer newCustomer = customerService.saveNew(customer,locationRegion);
        CustomerResDTO customerResDTO = newCustomer.toCustomerResDTO();

        return new ResponseEntity<>(customerResDTO,HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable("customerId") String customerIdStr){
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
        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        return new ResponseEntity<>(customerResDTO,HttpStatus.OK);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<?> editCustomer(@PathVariable("customerId") String customerIdStr,
                                          @Valid @RequestBody CustomerCreReqDTO customerCreReqDTO,
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
        Customer currentCustomer = customerOptional.get();

        String email = customerCreReqDTO.getEmail();
        if (customerService.existsByEmailAndIdNot(email, customerId)){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Email đã tồn tại, hãy nhập email khác");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        LocationRegionCreReqDTO locationRegionCreReqDTO = customerCreReqDTO.getLocationRegion();
        LocationRegion locationRegion = locationRegionCreReqDTO.toLocationRegion(currentCustomer.getLocationRegion().getId());

        Customer customer = customerCreReqDTO.toCustomer(customerId,currentCustomer.getBalance());
        CustomerCreResDTO customerCreResDTO = customer.toCustomerCreResDTO();

        customerService.saveNew(customer,locationRegion);

        return new ResponseEntity<>(customerCreResDTO,HttpStatus.OK);
    }

    @PostMapping("/del/{customerId}")
    public ResponseEntity<?> removeCustomer(@PathVariable("customerId") String customerIdStr){
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
        customer.setId(customerId);
        customer.setDeleted(true);
        CustomerResDTO customerResDTO = customer.toCustomerResDTO();
        customerService.save(customer);

        return new ResponseEntity<>(customerResDTO,HttpStatus.OK);
    }
}
