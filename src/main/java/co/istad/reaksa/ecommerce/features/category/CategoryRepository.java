package co.istad.reaksa.ecommerce.features.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    List<Category> findByParentCategoryId(Integer parentCategoryId);

    boolean existsByNameAndIdNot(String name, Integer id);
}
