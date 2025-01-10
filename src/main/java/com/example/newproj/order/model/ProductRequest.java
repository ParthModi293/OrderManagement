package com.example.newproj.order.model;

import lombok.Data;

import java.util.Objects;

@Data
public class ProductRequest {

    private int productId;
    private int quantity;

}
