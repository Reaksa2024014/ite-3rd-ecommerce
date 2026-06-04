package co.istad.reaksa.ecommerce.repository;

import co.istad.reaksa.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
