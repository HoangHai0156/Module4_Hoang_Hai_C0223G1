package com.cg.model.dto;

import com.cg.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TransferResDTO {
    private Long id;
    private Integer fee = 10;
    private BigDecimal feeAmount;
    private BigDecimal transactionAmount;
    private BigDecimal transferAmount;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;
}
