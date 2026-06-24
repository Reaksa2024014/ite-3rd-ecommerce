package co.istad.reaksa.ecommerce.features.product;

import co.istad.reaksa.ecommerce.features.product.dto.CreateProductRequest;
import co.istad.reaksa.ecommerce.features.product.dto.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    /**

     */
    Page<ProductResponse> findAll(int pageNumber, int pageSize);

    //mostly comment on service
    /**
     * Create a new product
     * @param createProductRequest is requesting data for creating product
     * @return {@link ProductResponse}
     * @author ky_reaksa
     * @since 23-June-2023
     */
    ProductResponse createNew(CreateProductRequest createProductRequest);
}
