package co.istad.reaksa.ecommerce.features.category.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Icon is required")
        String icon,

        @NotBlank(message = "Description is required")
        String description
) {
}
