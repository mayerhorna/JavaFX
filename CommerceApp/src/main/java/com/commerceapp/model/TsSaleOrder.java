package com.commerceapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ts_sale_order")
public class TsSaleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ts_sale_order_id;

    @Column
    private long ba_customer_id;

    @Column
    private String code;

    @Column
    private Date date_order;

    @Column
    private String observation;

    @Column
    private Integer sync_id;

    @Column
    private double total_amount_with_tax;

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

    @Override
    public String toString() {
        return "TsSaleOrder [ts_sale_order_id=" + ts_sale_order_id + ", ba_customer_id=" + ba_customer_id + ", code=" + code
                + ", date_order=" + date_order + ", observation=" + observation + ", sync_id=" + sync_id
                + ", total_amount_with_tax=" + total_amount_with_tax + "]";
    }
}