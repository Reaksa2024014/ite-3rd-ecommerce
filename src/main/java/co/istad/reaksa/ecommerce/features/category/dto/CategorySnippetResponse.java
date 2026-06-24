package co.istad.reaksa.ecommerce.features.category.dto;

import lombok.Builder;

@Builder
public record CategorySnippetResponse(
        Integer id,
        String name
) {
}
