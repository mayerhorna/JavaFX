package com.commerceapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ts_sale_order_line")
public class TsSaleOrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ts_sale_order_line_id;

    @Column(name = "ts_sale_order_id")
    private int tsSaleOrderId;

    @Column(name = "tb_product_id")
    private long tbProductId;

    @Column
    private Integer quantity;

    @Column
    private String name;

    @Column
    private double price_with_tax;

    @Column
    private double total_price_with_tax;

    public int getTs_sale_order_line_id() {
        return ts_sale_order_line_id;
    }

    public void setTs_sale_order_line_id(int ts_sale_order_line_id) {
        this.ts_sale_order_line_id = ts_sale_order_line_id;
    }

    public int getTsSaleOrderId() {
        return tsSaleOrderId;
    }

    public void setTsSaleOrderId(int tsSaleOrderId) {
        this.tsSaleOrderId = tsSaleOrderId;
    }

    public long getTbProductId() {
        return tbProductId;
    }

    public void setTbProductId(long tbProductId) {
        this.tbProductId = tbProductId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice_with_tax() {
        return price_with_tax;
    }

    public void setPrice_with_tax(double price_with_tax) {
        this.price_with_tax = price_with_tax;
    }

    public double getTotal_price_with_tax() {
        return total_price_with_tax;
    }

    public void setTotal_price_with_tax(double total_price_with_tax) {
        this.total_price_with_tax = total_price_with_tax;
    }

    @Override
    public String toString() {
        return "TsSaleOrderLine [ts_sale_order_line_id=" + ts_sale_order_line_id + ", tsSaleOrderId=" + tsSaleOrderId
                + ", tbProductId=" + tbProductId + ", quantity=" + quantity + ", name=" + name + ", price_with_tax="
                + price_with_tax + ", total_price_with_tax=" + total_price_with_tax + "]";
    }
}
