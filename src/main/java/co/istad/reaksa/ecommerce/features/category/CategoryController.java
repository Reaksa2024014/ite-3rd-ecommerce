package co.istad.reaksa.ecommerce.features.category;

import co.istad.reaksa.ecommerce.features.category.dto.CategoryResponse;
import co.istad.reaksa.ecommerce.features.category.dto.CreateCategoryRequest;
import co.istad.reaksa.ecommerce.features.category.dto.UpdateCategoryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryResponse createNew(@Valid @RequestBody CreateCategoryRequest createCategoryRequest){
        return categoryService.createNew(createCategoryRequest);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<CategoryResponse> getAllCategories(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "25") int pageSize) {
        return categoryService.getAllCategories(pageNumber, pageSize);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/subcategories")
    public List<CategoryResponse> getSubcategoriesByMainCategoryId(@PathVariable Integer id) {
        return categoryService.getSubcategoriesByMainCategoryId(id);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void hardDeleteCategory(@PathVariable Integer id) {
        categoryService.hardDeleteCategory(id);
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public CategoryResponse softDeleteCategory(@PathVariable Integer id) {
        return categoryService.softDeleteCategory(id);
    }


    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        return categoryService.updateCategory(id, updateCategoryRequest);
    }

}
