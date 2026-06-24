package co.istad.reaksa.ecommerce.features.category;

import co.istad.reaksa.ecommerce.features.category.dto.CategoryResponse;
import co.istad.reaksa.ecommerce.features.category.dto.CreateCategoryRequest;
import co.istad.reaksa.ecommerce.features.category.dto.UpdateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;



@Mapper (componentModel = "spring")
public interface CategoryMapper {


    //Return type = Target
    //Parameter = source
    Category mapCreateCategoryRequestToCategory(CreateCategoryRequest createCategoryRequest);

    CategoryResponse mapCategoryToCategoryResponse(Category category);

    void updateCategoryFromRequest(UpdateCategoryRequest request, @MappingTarget Category category);
}

