package co.istad.reaksa.ecommerce.features.product.dto;

import co.istad.reaksa.ecommerce.features.category.dto.CategorySnippetResponse;

import java.math.BigDecimal;

public record ProductResponse(
        String code,
        String slug,
        String name,
        String description,
        String thumbnail,
        BigDecimal unitPrice,
        Integer qty,
        Boolean isAvailable,
        Boolean isDeleted,
        CategorySnippetResponse category

) {
}
