package co.istad.reaksa.ecommerce.features.file.dto;

import lombok.Builder;

@Builder
public record FileUploadResponse(
        String name,
        String caption,
        Long size,
        String mediaType,
        //url
        String uri
//        String downloadUri
) {
}
