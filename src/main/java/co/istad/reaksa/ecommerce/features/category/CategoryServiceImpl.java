package co.istad.reaksa.ecommerce.features.category;

import co.istad.reaksa.ecommerce.features.category.dto.CategoryResponse;
import co.istad.reaksa.ecommerce.features.category.dto.CreateCategoryRequest;
import co.istad.reaksa.ecommerce.features.category.dto.UpdateCategoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;


    @Override
    public CategoryResponse createNew(CreateCategoryRequest createCategoryRequest) {

        log.info("createNew {}",createCategoryRequest);

        //validate category name // name must be unique
        boolean isExisting = categoryRepository
                .existsByName(createCategoryRequest.name());

        if (isExisting)
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category has already been used"
            );

        Category parentCategory = null;

        //validate parent category
        if (createCategoryRequest.parentCategoryId()!= null){
            parentCategory = categoryRepository.findById(createCategoryRequest.parentCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Parent category has not been found"
                    ));
        }

        Category category = categoryMapper
                .mapCreateCategoryRequestToCategory(createCategoryRequest);

        //System generated data
        category.setIsDeleted(false);
        category.setParentCategory(parentCategory);

        //Insert if primary key is null
        //Update if primary key has value
        category = categoryRepository.save(category);



        return categoryMapper.mapCategoryToCategoryResponse(category);

    }

    @Override
    public Page<CategoryResponse> getAllCategories(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.map(categoryMapper::mapCategoryToCategoryResponse);
    }


    @Override
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
        return categoryMapper.mapCategoryToCategoryResponse(category);
    }


    @Override
    public List<CategoryResponse> getSubcategoriesByMainCategoryId(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Category not found with id: " + id
            );
        }

        List<Category> subcategories = categoryRepository.findByParentCategoryId(id);
        return subcategories.stream()
                .map(categoryMapper::mapCategoryToCategoryResponse)
                .toList();
    }


    @Override
    public void hardDeleteCategory(Integer id) {
        // check if category exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category not found with id: " + id
                ));

        categoryRepository.delete(category);
    }


    @Override
    public CategoryResponse softDeleteCategory(Integer id) {
        // check if category exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category not found with id: " + id
                ));

        // check if already soft deleted
        if (category.getIsDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category with id: " + id + " has already been deleted"
            );
        }

        category.setIsDeleted(true);
        category = categoryRepository.save(category);

        return categoryMapper.mapCategoryToCategoryResponse(category);

    }


    @Override
    public CategoryResponse updateCategory(Integer id, UpdateCategoryRequest updateCategoryRequest) {
        // check if category exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category not found with id: " + id
                ));

        // check if soft deleted
        if (category.getIsDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Category with id: " + id + " has been deleted, cannot update"
            );
        }

        // check if new name already exists (exclude current id)
        if (categoryRepository.existsByNameAndIdNot(updateCategoryRequest.name(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category name already exists: " + updateCategoryRequest.name()
            );
        }

        // update fields
        categoryMapper.updateCategoryFromRequest(updateCategoryRequest, category);
        category = categoryRepository.save(category);

        return categoryMapper.mapCategoryToCategoryResponse(category);
    }
}
