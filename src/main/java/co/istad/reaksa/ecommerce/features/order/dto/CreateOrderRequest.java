package co.istad.reaksa.ecommerce.features.order.dto;


import jakarta.validation.constraints.*;

import java.util.List;

public record CreateOrderRequest (
        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Discount is required")
        @Min(0)
        @Max(100)
        Float discount,

        @Size(max = 255)
        String remark,

        @NotEmpty(message="Order Line is required")
        List<OrderLineDto> orderLines


){
}
