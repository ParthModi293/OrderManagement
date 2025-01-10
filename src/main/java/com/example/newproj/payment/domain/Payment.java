package com.example.newproj.payment.domain;

import lombok.Data;

@Data
public class Payment {

    private int id;
    private int orderId;
    private String type;
    private String status;
}