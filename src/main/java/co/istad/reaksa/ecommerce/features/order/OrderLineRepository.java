package co.istad.reaksa.ecommerce.repository;

import co.istad.reaksa.ecommerce.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
}
