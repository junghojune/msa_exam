package com.sparta.msa_exam.order.domain;import com.sparta.msa_exam.order.dto.OrderItemsResponseDto;import jakarta.persistence.*;import lombok.*;import java.util.ArrayList;import java.util.List;@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Entity@Table(name = "order_items")public class OrderItems {    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)    private Long id;    @ManyToOne(fetch = FetchType.LAZY)    @JoinColumn(name = "order_id", nullable = false)    private Order order;    private Long orderItemId;    public OrderItems(Order order, Long orderItemId) {        this.order =order;        this.orderItemId = orderItemId;    }    public static List<OrderItemsResponseDto> toOrderItemResponseDto(List<OrderItems> orderItemsList) {        List<OrderItemsResponseDto> orderResponseList = new ArrayList<>();        for(OrderItems orderItems : orderItemsList) {            orderResponseList.add(new OrderItemsResponseDto(orderItems.getOrderItemId()));        }        return orderResponseList;    }}