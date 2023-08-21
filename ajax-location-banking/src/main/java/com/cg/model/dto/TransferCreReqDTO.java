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
public class TransferCreReqDTO {

    @NotBlank(message = "ID người nhận không được để trống")
    @Pattern(regexp = "\\d+", message = "ID người nhận phải là một số")
    @NotNull(message = "Nhập giá trị cho ID người nhận")
    private String recipientId;

    @Pattern(regexp = "\\d+", message = "Tiền chuyển phải là một số!")
    @Size(min = 3, max = 9, message = "Tiền chuyển phải trong khoảng 1.000 đến 999.999.999")
    @NotBlank(message = "Hãy nhập tiền chuyển")
    @NotNull(message = "Nhập giá trị cho tiền chuyển")
    private String transferAmount;
}
