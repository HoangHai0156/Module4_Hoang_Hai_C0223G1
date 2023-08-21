package com.cg.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WithdrawCreReqDTO {

    @Pattern(regexp = "\\d+", message = "Tiền rút ra phải là một số!")
    @Size(min = 3, max = 9, message = "Tiền rút ra phải trong khoảng 1.000 đến 999.999.999")
    @NotBlank(message = "Hãy nhập tiền rút ra")
    @NotNull(message = "Nhập giá trị cho tiền rút ra")
    private String transactionAmount;
}
