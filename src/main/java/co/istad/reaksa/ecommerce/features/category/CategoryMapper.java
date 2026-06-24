package co.istad.reaksa.ecommerce.mapper;

import co.istad.reaksa.ecommerce.domain.Category;
import co.istad.reaksa.ecommerce.dto.CategoryResponse;
import co.istad.reaksa.ecommerce.dto.CreateCategoryRequest;
import co.istad.reaksa.ecommerce.dto.UpdateCategoryRequest;
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

