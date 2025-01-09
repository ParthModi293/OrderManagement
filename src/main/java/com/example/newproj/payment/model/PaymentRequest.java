package com.example.newproj.payment.model;

import lombok.Data;

@Data
public class PaymentRequest {

    private int orderId;
    private payment_type type;
    private String status;
}
