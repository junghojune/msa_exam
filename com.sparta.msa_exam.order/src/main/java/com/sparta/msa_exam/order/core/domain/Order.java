package com.sparta.msa_exam.order.core.domain;

import com.sparta.msa_exam.order.core.enums.OrderStatus;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    List<OrderItems> orderItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.CREATED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // 팩토리 메서드
    public static Order createOrder(String createdBy) {
        return Order.builder()
                .createdBy(createdBy)
                .status(OrderStatus.CREATED)
                .build();
    }

    // 업데이트 메서드
    public void updateOrder(String updatedBy) {
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }



    // DTO로 변환하는 메서드
    public OrderResponseDto toResponseDto() {
        return new OrderResponseDto(
                this.id,
                this.status.name(),
                this.createdAt,
                this.createdBy,
                this.updatedAt,
                this.updatedBy,
                OrderItems.toOrderItemResponseDto(this.orderItems)
        );
    }
}

