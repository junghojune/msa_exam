package com.sparta.msa_exam.order.orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;

    /**
     * 주문 생성
     *
     * @param orderRequestDto
     * @param userId
     * @param role
     * @return
     */
    @PostMapping
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                        @RequestHeader(value = "X-User-Id", required = true) String userId,
                                        @RequestHeader(value = "X-Role", required = true) String role) {

        return orderService.createOrder(orderRequestDto, userId);
    }

    /**
     * 주문 전체 조회
     * @param searchDto
     * @param pageable
     * @param userId
     * @param role
     * @return
     */
    @GetMapping
    public Page<OrderResponseDto> getOrders(OrderSearchDto searchDto, Pageable pageable,
                                            @RequestHeader(value = "X-User-Id", required = true) String userId,
                                            @RequestHeader(value = "X-Role", required = true) String role) {
        // 역할이 MANAGER인지 확인
        if (!"MANAGER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }
        return orderService.getOrders(searchDto, pageable,role, userId);
    }

    /**
     * 주문 단건 조회
     *
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    /**
     * 주문 상품 추가
     *
     * @param orderId
     * @param orderRequestDto
     * @param userId
     * @param role
     * @return
     */
    @PutMapping("/{orderId}")
    public OrderResponseDto updateOrder(@PathVariable Long orderId,
                                        @RequestBody OrderRequestDto orderRequestDto,
                                        @RequestHeader(value = "X-User-Id") String userId,
                                        @RequestHeader(value = "X-Role") String role) {
        return orderService.updateOrder(orderId, orderRequestDto, userId);
    }
}
