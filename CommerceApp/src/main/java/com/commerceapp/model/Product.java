package com.commerceapp.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "seq_tb_product", allocationSize = 1)
    private Long tb_product_id;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private String description;

    @Column(name = "sales_price_with_tax", precision = 11, scale = 3)
    private BigDecimal salesPriceWithTax;

    @Column(name = "default_uom", length = 20)
    private String defaultUom;

    @Column(length = 20)
    private String ean;

    // Constructor, getters, and setters
    
    @Override
    public String toString() {
        return "Product [tb_product_id=" + tb_product_id + ", code=" + code + ", name=" + name + ", description="
                + description + ", salesPriceWithTax=" + salesPriceWithTax + ", defaultUom=" + defaultUom + ", ean="
                + ean + "]";
    }

	public Long getTb_product_id() {
		return tb_product_id;
	}

	public void setTb_product_id(Long tb_product_id) {
		this.tb_product_id = tb_product_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getSalesPriceWithTax() {
		return salesPriceWithTax;
	}

	public void setSalesPriceWithTax(BigDecimal salesPriceWithTax) {
		this.salesPriceWithTax = salesPriceWithTax;
	}

	public String getDefaultUom() {
		return defaultUom;
	}

	public void setDefaultUom(String defaultUom) {
		this.defaultUom = defaultUom;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}
}
