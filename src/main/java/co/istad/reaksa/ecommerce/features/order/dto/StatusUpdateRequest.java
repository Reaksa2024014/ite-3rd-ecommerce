package co.istad.reaksa.ecommerce.features.order.dto;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateRequest(

        Boolean status
) {
}
