package co.istad.reaksa.ecommerce.features.order;

import co.istad.reaksa.ecommerce.features.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {



}
