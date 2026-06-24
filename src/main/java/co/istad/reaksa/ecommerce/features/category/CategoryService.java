package co.istad.reaksa.ecommerce.service;

import co.istad.reaksa.ecommerce.dto.CategoryResponse;
import co.istad.reaksa.ecommerce.dto.CreateCategoryRequest;
import co.istad.reaksa.ecommerce.dto.UpdateCategoryRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    CategoryResponse createNew(CreateCategoryRequest createCategoryRequest);


    Page<CategoryResponse> getAllCategories(int pageNumber, int pageSize);

    CategoryResponse getCategoryById(Integer id);

    List<CategoryResponse> getSubcategoriesByMainCategoryId(Integer id);

    void hardDeleteCategory(Integer id);

    CategoryResponse softDeleteCategory(Integer id);

    CategoryResponse updateCategory(Integer id, UpdateCategoryRequest updateCategoryRequest);
}
