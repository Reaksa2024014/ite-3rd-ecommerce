package co.istad.reaksa.ecommerce.features.order;

import co.istad.reaksa.ecommerce.features.order.dto.CreateOrderRequest;
import co.istad.reaksa.ecommerce.features.order.dto.OrderResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order mapCreateOrderRequestToOrder(CreateOrderRequest createOrderRequest);

    OrderResponse mapOrderToOrderResponse(Order order);
}
