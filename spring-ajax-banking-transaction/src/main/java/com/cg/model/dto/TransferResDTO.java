package com.cg.model.dto;

import com.cg.model.Customer;
import com.cg.model.dto.customer.CustomerResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class TransferResDTO {
    private Long id;
    private Integer fee = 10;
    private BigDecimal feeAmount;
    private BigDecimal transactionAmount;
    private BigDecimal transferAmount;
    private CustomerResDTO sender;
    private CustomerResDTO recipient;

    public TransferResDTO(Long id, Integer fee,BigDecimal feeAmount, BigDecimal transactionAmount, BigDecimal transferAmount, Customer sender, Customer recipient){
        this.id = id;
        this.fee = fee;
        this.feeAmount = feeAmount;
        this.transactionAmount = transactionAmount;
        this.transferAmount = transferAmount;
        this.sender = sender.toCustomerResDTO();
        this.recipient = recipient.toCustomerResDTO();
    }
}
