package co.istad.reaksa.ecommerce.features.product.dto;

import co.istad.reaksa.ecommerce.features.category.dto.CategorySnippetResponse;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 255)
        String name,
        @Size(max = 500)
        String description,
        @Size(max = 255)
        String thumbnail,
        @NotNull(message = "Ünit price is required")
        @Min(0)
        BigDecimal unitPrice,
        @NotNull(message = "QTY is required")
        @Min(0)
        Integer qty,
        @NotNull(message = "Category ID is required")
        @Positive
        Integer categoryId
) {
}
