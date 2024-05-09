package com.commerceapp.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ts_sale_order")
public class TsSaleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ts_sale_order_id")
    private int ts_sale_order_id;

    @Column(nullable = true,name = "ba_customer_id")
    private long ba_customer_id;

    @Column(nullable = true,name = "code")
    private String code;

    @Column(name = "date_order")
    private Date date_order;

    @Column(name = "observation")
    private String observation;

    @Column(name = "sync_id")
    private Integer sync_id;

    @Column(name = "total_amount_with_tax")
    private double total_amount_with_tax;

    // Constructor, getters, and setters

    @Override
    public String toString() {
        return "TsSaleOrder [ts_sale_order_id=" + ts_sale_order_id + ", ba_customer_id=" + ba_customer_id + ", code="
                + code + ", date_order=" + date_order + ", observation=" + observation + ", sync_id=" + sync_id
                + ", total_amount_with_tax=" + total_amount_with_tax + "]";
    }

    public int getTs_sale_order_id() {
        return ts_sale_order_id;
    }

    public void setTs_sale_order_id(int ts_sale_order_id) {
        this.ts_sale_order_id = ts_sale_order_id;
    }

    public long getBa_customer_id() {
        return ba_customer_id;
    }

    public void setBa_customer_id(long ba_customer_id) {
        this.ba_customer_id = ba_customer_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate_order() {
        return date_order;
    }

    public void setDate_order(Date date_order) {
        this.date_order = date_order;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Integer getSync_id() {
        return sync_id;
    }

    public void setSync_id(Integer sync_id) {
        this.sync_id = sync_id;
    }

    public double getTotal_amount_with_tax() {
        return total_amount_with_tax;
    }

    public void setTotal_amount_with_tax(double total_amount_with_tax) {
        this.total_amount_with_tax = total_amount_with_tax;
    }
}
