package co.istad.reaksa.ecommerce.repository;

import co.istad.reaksa.ecommerce.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
