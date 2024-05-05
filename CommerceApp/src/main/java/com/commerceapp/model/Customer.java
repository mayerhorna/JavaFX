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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFiscalNumber() {
		return fiscalNumber;
	}

	public void setFiscalNumber(String fiscalNumber) {
		this.fiscalNumber = fiscalNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommercialName() {
		return commercialName;
	}

	public void setCommercialName(String commercialName) {
		this.commercialName = commercialName;
	}

	public BigDecimal getDiscountProduct() {
		return discountProduct;
	}

	public void setDiscountProduct(BigDecimal discountProduct) {
		this.discountProduct = discountProduct;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", code=" + code + ", fiscalNumber=" + fiscalNumber + ", name=" + name
				+ ", commercialName=" + commercialName + ", discountProduct=" + discountProduct + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}

    // Constructor, getters, and setters
    
}

