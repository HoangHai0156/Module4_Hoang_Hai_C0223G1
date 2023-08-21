package com.cg.model;

import com.cg.model.dto.TransferResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transfers")
public class Transfer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer fee = 10;

    @Column(name = "fee_amount", nullable = false, precision = 10)
    private BigDecimal feeAmount;

    @Column(name = "transaction_amount", nullable = false, precision = 10)
    private BigDecimal transactionAmount;

    @Column(name = "transfer_amount", nullable = false, precision = 10)
    private BigDecimal transferAmount;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private Customer sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id", nullable = false)
    private Customer recipient;

    public Transfer(BigDecimal feeAmount, BigDecimal transactionAmount, BigDecimal transferAmount, Customer sender, Customer recipient) {
        this.setCreatedAt(new Date());
        this.feeAmount = feeAmount;
        this.transactionAmount = transactionAmount;
        this.transferAmount = transferAmount;
        this.sender = sender;
        this.recipient = recipient;
    }

    public TransferResDTO toTransferResDTO(){
        return new TransferResDTO()
                .setId(id)
                .setFee(fee)
                .setFeeAmount(feeAmount)
                .setTransactionAmount(transactionAmount)
                .setTransferAmount(transferAmount)
                .setSender(sender.toCustomerResDTO())
                .setRecipient(recipient.toCustomerResDTO());
    }
}
