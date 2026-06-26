package co.istad.reaksa.ecommerce.features.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderLineDto(
        @NotBlank(message = "Code is required")
        String code,

        @Positive
        @NotNull(message = "Qty  is required")
        Integer qty,

        @Positive
        @NotNull(message = "Unit Price  is required")
        BigDecimal unitPrice
) {
}
