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
public class DepositCreReqDTO {

    @Pattern(regexp = "\\d+", message = "Tiền nạp vào phải là một số!")
    @Size(min = 3, max = 9, message = "Tiền nạp vào phải trong khoảng 1.000 đến 999.999.999")
    @NotBlank(message = "Hãy nhập tiền nộp vào")
    @NotNull(message = "Nhập giá trị cho tiền nộp vào")
    private String transactionAmount;
}
