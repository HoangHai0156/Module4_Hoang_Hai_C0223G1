package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.model.dto.TransferCreReqDTO;
import com.cg.model.dto.TransferResDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.transfer.ITransferService;
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
@RequestMapping("/api/transfers")
public class TransferAPI {

    @Autowired
    ICustomerService customerService;

    @Autowired
    ITransferService transferService;

    @Autowired
    AppUtils appUtils;

    @GetMapping
    public ResponseEntity<?> getAllTransfer(){
        List<Transfer> transfers = transferService.findAll();



        return new ResponseEntity<>(transfers,HttpStatus.OK);
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<?> transfer(@PathVariable("customerId") String customerIdStr,
                                      @Valid @RequestBody TransferCreReqDTO transferCreReqDTO,
                                      BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (!ValidateUtil.isNumberValid(customerIdStr)){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã nguời gửi không hợp lệ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long senderId = Long.parseLong(customerIdStr);
        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (senderOptional.isEmpty()){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã người gửi không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long recipientId = Long.parseLong(transferCreReqDTO.getRecipientId());
        Optional<Customer> recipientOptional = customerService.findById(recipientId);

        if (recipientOptional.isEmpty()){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã người nhận không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        if (senderId.equals(recipientId)){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Người nhận và người gửi không được trùng nhau");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Customer sender = senderOptional.get();
        Customer recipient = recipientOptional.get();
        Transfer transfer = new Transfer();

        BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(transferCreReqDTO.getTransferAmount()));
        Integer fee = transfer.getFee();
        BigDecimal feeAmount = transferAmount.multiply(BigDecimal.valueOf(fee)).divide(BigDecimal.valueOf(100));
        BigDecimal transactionAmount = transferAmount.add(feeAmount);

        if (transactionAmount.compareTo(sender.getBalance()) > 0){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Số dư không đủ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        sender.setBalance(sender.getBalance().subtract(transactionAmount));
        recipient.setBalance(recipient.getBalance().add(transferAmount));

        transfer.setId(null);
        transfer.setTransferAmount(transferAmount);
        transfer.setTransactionAmount(transactionAmount);
        transfer.setFeeAmount(feeAmount);
        transfer.setSender(sender);
        transfer.setRecipient(recipient);

        Transfer newTransfer = customerService.doTransfer(transfer,sender,recipient);
        TransferResDTO transferResDTO = newTransfer.toTransferResDTO();

        return new ResponseEntity<>(transferResDTO,HttpStatus.OK);
    }
}
