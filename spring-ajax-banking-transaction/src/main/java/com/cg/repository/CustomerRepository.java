package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.dto.customer.CustomerResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByFullNameLikeOrEmailLikeOrPhoneLike(String name, String email, String phone);

    @Modifying
    @Query("UPDATE Customer as cus SET cus.balance = :amount WHERE (cus.id = :id)")
    void setBalance(@Param("amount") BigDecimal amount, @Param("id") Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    List<Customer> findAllByIdNot(Long id);

    @Query("SELECT new com.cg.model.dto.customer.CustomerResDTO(" +
            "cus.id," +
            "cus.fullName," +
            "cus.email," +
            "cus.phone," +
            "cus.balance," +
            "cus.locationRegion" +
            ") " +
            "from Customer as cus where cus.deleted = false")
    List<CustomerResDTO> getAllCustomerDTO();

}
