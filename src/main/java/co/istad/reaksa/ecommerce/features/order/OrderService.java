package co.istad.reaksa.ecommerce.features.order;

import co.istad.reaksa.ecommerce.features.order.dto.CreateOrderRequest;
import co.istad.reaksa.ecommerce.features.order.dto.OrderResponse;
import co.istad.reaksa.ecommerce.features.order.dto.StatusUpdateRequest;
import co.istad.reaksa.ecommerce.features.product.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse createNew(CreateOrderRequest createOrderRequest);


    Page<OrderResponse> findAll(int pageNumber, int pageSize);

    OrderResponse findById(UUID id);

    OrderResponse softDeleteById(UUID id);

    void hardDeleteById(UUID id);

    OrderResponse setPaymentStatus(UUID id, StatusUpdateRequest statusUpdateRequest);

}
