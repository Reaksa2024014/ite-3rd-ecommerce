package co.istad.reaksa.ecommerce.features.product;

import co.istad.reaksa.ecommerce.features.product.dto.CreateProductRequest;
import co.istad.reaksa.ecommerce.features.product.dto.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product mapCreateProductRequestToProduct(CreateProductRequest createProductRequest);

    ProductResponse mapProductToProductResponse(Product product);

}
