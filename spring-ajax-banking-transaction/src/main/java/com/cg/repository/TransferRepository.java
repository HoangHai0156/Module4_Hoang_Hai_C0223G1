package com.cg.repository;

import com.cg.model.Transfer;
import com.cg.model.dto.TransferResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT new com.cg.model.dto.TransferResDTO(" +
            "trf.id," +
            "trf.fee," +
            "trf.feeAmount," +
            "trf.transactionAmount," +
            "trf.transferAmount," +
            "trf.sender," +
            "trf.recipient) from Transfer as trf")
    List<TransferResDTO> getAllTransferResDTO();
}
