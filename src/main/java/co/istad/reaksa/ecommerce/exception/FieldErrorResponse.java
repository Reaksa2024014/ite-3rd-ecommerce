package co.istad.reaksa.ecommerce.exception;


import lombok.Builder;

@Builder
public record FieldErrorResponse(
        String field,
        String message
) {
}
