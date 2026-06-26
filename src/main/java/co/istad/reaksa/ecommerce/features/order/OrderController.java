package co.istad.reaksa.ecommerce.features.order;

import co.istad.reaksa.ecommerce.features.order.dto.CreateOrderRequest;
import co.istad.reaksa.ecommerce.features.order.dto.OrderResponse;
import co.istad.reaksa.ecommerce.features.order.dto.StatusUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void hardDeleteById(@PathVariable UUID id) {
        orderService.hardDeleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/soft-delete")
    public OrderResponse softDeleteById(@PathVariable UUID id) {
        return orderService.softDeleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/status")
    public OrderResponse changeStatus(@PathVariable UUID id, @RequestBody StatusUpdateRequest statusUpdateRequest) {
        return orderService.setPaymentStatus(id,statusUpdateRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable UUID id){
        return orderService.findById(id);
    }

    @GetMapping
    public Page<OrderResponse> findAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return orderService.findAll(pageNumber, pageSize);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderResponse createNew(@Valid @RequestBody CreateOrderRequest createOrderRequest){
        return orderService.createNew(createOrderRequest);
    }
}
