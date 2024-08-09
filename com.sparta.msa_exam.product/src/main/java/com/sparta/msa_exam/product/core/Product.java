package com.sparta.msa_exam.product.core;



import com.sparta.msa_exam.product.products.ProductRequestDto;
import com.sparta.msa_exam.product.products.ProductResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer price;

    private LocalDateTime createdAt;
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static Product createProduct(ProductRequestDto requestDto, String userId) {
        return Product.builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .createdBy(userId)
                .build();
    }
    // DTO로 변환하는 메서드
    public ProductResponseDto toResponseDto() {
        return new ProductResponseDto(
                this.id,
                this.name,
                this.price,
                this.createdAt,
                this.createdBy
        );
    }
}