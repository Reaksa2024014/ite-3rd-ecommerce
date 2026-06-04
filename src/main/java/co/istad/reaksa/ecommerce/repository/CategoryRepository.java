package co.istad.reaksa.ecommerce.repository;

import co.istad.reaksa.ecommerce.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
