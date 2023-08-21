package com.cg.model;

import com.cg.model.dto.CustomerResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "customers")
@Accessors(chain = true)
public class Customer extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, name = "full_name")
    @NotBlank(message = "Hãy nhập vào tên")
    @NotNull(message = "Hãy nhập giá trị cho tên")
    private String fullName;

    @NotBlank(message = "Hãy nhập vào email")
    @NotNull(message = "Hãy nhập giá trị cho email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Hãy nhập vào phone")
    @NotNull(message = "Hãy nhập giá trị cho phone")
    @Column(unique = true)
    private String phone;

    @OneToOne
    @JoinColumn(name = "location_region_id", referencedColumnName = "id", nullable = false)
    private LocationRegion locationRegion;

    @Column(precision = 10, scale = 0, nullable = false, updatable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    public CustomerResDTO toCustomerResDTO(){
        return new CustomerResDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegionResDTO());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;
    }
}
