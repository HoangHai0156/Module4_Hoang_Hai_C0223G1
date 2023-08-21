package com.cg.model.dto.customer;

import com.cg.model.Customer;
import com.cg.model.dto.location.LocationRegionCreReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerCreReqDTO {

    @Column(nullable = false, name = "full_name")
    @NotBlank(message = "Hãy nhập vào tên")
    @NotNull(message = "Hãy nhập giá trị cho tên")
    @Size(message = "Tên nằm trong khoảng từ ")
    private String fullName;

    @NotBlank(message = "Hãy nhập vào email")
    @NotNull(message = "Hãy nhập giá trị cho email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Hãy nhập vào phone")
    @NotNull(message = "Hãy nhập giá trị cho phone")
    private String phone;

    private LocationRegionCreReqDTO locationRegion;

    public Customer toCustomer(Long id, BigDecimal balance){
        return new Customer()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setLocationRegion(locationRegion.toLocationRegion(0L))
                ;
    }
}
