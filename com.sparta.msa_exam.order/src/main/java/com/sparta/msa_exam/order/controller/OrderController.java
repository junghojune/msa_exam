package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
     * @return
     */
    @PostMapping
    public void createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                        @RequestHeader(value = "X-User-Id", required = true) String userId) {

        orderService.createOrder(orderRequestDto, userId);
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
