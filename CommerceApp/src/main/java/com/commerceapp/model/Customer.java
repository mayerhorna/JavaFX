package com.commerceapp.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ba_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ba_customer_id")
    private Long id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "fiscal_number", length = 25)
    private String fiscalNumber;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "commercial_name", length = 200)
    private String commercialName;

    @Column(name = "discount_product", precision = 6, scale = 3, columnDefinition = "DECIMAL(6,3) DEFAULT 0")
    private BigDecimal discountProduct;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Constructor, getters, and setters
}

