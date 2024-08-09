package com.sparta.msa_exam.order.orders;

import com.sparta.msa_exam.order.core.domain.Order;
import com.sparta.msa_exam.order.core.domain.OrderItems;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.repository.OrderItemsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, String userId) {
        Order order = Order.createOrder(userId);
        Order savedOrder = orderRepository.save(order);

        List<OrderItems> orderItems = new ArrayList<>();
        for(Long orderItem : requestDto.getOrderItemsList()){
            orderItems.add(new OrderItems(savedOrder, orderItem));
        }

        orderItemsRepository.saveAll(orderItems);
        return toResponseDto(savedOrder);
    }

    @Transactional
    @Cacheable(cacheNames = "orderCache", key = "args[0]")
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        return toResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long orderId, OrderRequestDto requestDto, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        List<OrderItems> orderItems = new ArrayList<>();
        for(Long orderItem : requestDto.getOrderItemsList()){
            orderItems.add(new OrderItems(order, orderItem));
        }

        order.updateOrder(userId);
        orderItemsRepository.saveAll(orderItems);
        Order updatedOrder = orderRepository.save(order);

        return toResponseDto(updatedOrder);
    }

    private OrderResponseDto toResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getCreatedBy(),
                order.getUpdatedAt(),
                order.getUpdatedBy(),
                OrderItems.toOrderItemResponseDto(order.getOrderItems())
        );
    }
}