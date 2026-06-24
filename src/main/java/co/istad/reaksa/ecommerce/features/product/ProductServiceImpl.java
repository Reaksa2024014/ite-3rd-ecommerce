package co.istad.reaksa.ecommerce.features.product;


import co.istad.reaksa.ecommerce.features.category.Category;
import co.istad.reaksa.ecommerce.features.category.CategoryRepository;
import co.istad.reaksa.ecommerce.features.product.dto.CreateProductRequest;
import co.istad.reaksa.ecommerce.features.product.dto.ProductResponse;
import co.istad.reaksa.ecommerce.utils.GenerateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    @Override
    public Page<ProductResponse> findAll(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);

        Page<Product> products = productRepository.findAll(pageRequest);

        return products.map(productMapper::mapProductToProductResponse);
    }

    @Override
    public ProductResponse createNew(CreateProductRequest createProductRequest) {

        //Validate product name
        if (productRepository.existsByName(createProductRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Product name has already been used");
        }

        //Validate category ID
        Category category = categoryRepository
                .findById(createProductRequest.categoryId())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        //Transfer data from dto to Model
        Product product = productMapper
                .mapCreateProductRequestToProduct(createProductRequest);

        //set generated system data
        product.setCategory(category);
        product.setCode(GenerateUtils.generateProductCode()); //ITE-3RD-1234
        product.setSlug(GenerateUtils.generateSlug(createProductRequest.name()));
        product.setIsAvailable(true);
        product.setIsDeleted(false);


        product = productRepository.save(product);

        return productMapper.mapProductToProductResponse(product);
    }
}
