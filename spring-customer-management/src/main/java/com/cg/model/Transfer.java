package com.cg.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

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

    public Transfer() {
    }

    public Transfer(Long id, Date createdAt, Integer fee, BigDecimal feeAmount, BigDecimal transactionAmount, BigDecimal transferAmount, Customer sender, Customer recipient) {
        this.id = id;
        this.setCreatedAt(createdAt);
        this.fee = fee;
        this.feeAmount = feeAmount;
        this.transactionAmount = transactionAmount;
        this.transferAmount = transferAmount;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Transfer(BigDecimal feeAmount, BigDecimal transactionAmount, BigDecimal transferAmount, Customer sender, Customer recipient) {
        this.setCreatedAt(new Date());
        this.feeAmount = feeAmount;
        this.transactionAmount = transactionAmount;
        this.transferAmount = transferAmount;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }

    public Customer getRecipient() {
        return recipient;
    }

    public void setRecipient(Customer recipient) {
        this.recipient = recipient;
    }
}
