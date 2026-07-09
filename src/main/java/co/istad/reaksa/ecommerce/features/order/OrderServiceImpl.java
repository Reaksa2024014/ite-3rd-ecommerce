package co.istad.reaksa.ecommerce.features.order;

import co.istad.reaksa.ecommerce.features.order.dto.CreateOrderRequest;
import co.istad.reaksa.ecommerce.features.order.dto.OrderResponse;
import co.istad.reaksa.ecommerce.features.order.dto.SoftDeleteUpdateRequest;
import co.istad.reaksa.ecommerce.features.order.dto.StatusUpdateRequest;
import co.istad.reaksa.ecommerce.features.product.Product;
import co.istad.reaksa.ecommerce.features.product.ProductRepository;
import co.istad.reaksa.ecommerce.security.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;


    @Override
    public OrderResponse setPaymentStatus(UUID id, StatusUpdateRequest statusUpdateRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found with id: " + id
                ));

        order.setStatus(statusUpdateRequest.status());
        order = orderRepository.save(order);
        return orderMapper.mapOrderToOrderResponse(order);

    }

    @Override
    public OrderResponse softDeleteById(UUID id, SoftDeleteUpdateRequest softDeleteUpdateRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found with id: " + id
                ));

        order.setIsDeleted(softDeleteUpdateRequest.isDeleted());
        order = orderRepository.save(order);
        return orderMapper.mapOrderToOrderResponse(order);
    }

    @Override
    public void hardDeleteById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found with id: " + id
                ));

        orderRepository.delete(order);
    }


    @Override
    public OrderResponse findById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found with id: " + id
                ));
        return orderMapper.mapOrderToOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> findAll(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);
        Page<Order> orders = orderRepository.findAll(pageRequest);
        return orders.map(orderMapper::mapOrderToOrderResponse);
    }


    @Override
    public OrderResponse createNew(CreateOrderRequest createOrderRequest) {

        final Order order = orderMapper.mapCreateOrderRequestToOrder(createOrderRequest);

        List<OrderLine> orderLines = new ArrayList<>();

        //Validate order Lines(LIST)
        boolean isValidOrder = createOrderRequest.orderLines().stream()
                .allMatch(orderLineDto -> {
                    Optional<Product> productOptional =  productRepository.findByCode(orderLineDto.code());
                    if (productOptional.isPresent()){
                        OrderLine orderLine = new OrderLine();
                        orderLine.setProduct(productOptional.get());
                        orderLine.setQty(orderLineDto.qty());
                        orderLine.setUnitPrice(orderLineDto.unitPrice());
                        orderLine.setOrder(order);
                        orderLines.add(orderLine);
                        return true;

                    }
                    return false;

                });
        if(!isValidOrder){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid Order Line");
        }
        order.setOrderLines(orderLines);

        //Security related
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();


        order.setCustomerId(SecurityUtils.extractUserId());

        order.setIsDeleted(false);
        order.setOrderedAt(LocalDateTime.now());
        order.setStatus(false);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.mapOrderToOrderResponse(savedOrder);
    }
}
