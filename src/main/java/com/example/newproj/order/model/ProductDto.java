package com.example.newproj.order.model;

import lombok.Data;

@Data
public class ProductDto {
    private  int orderId;
    private  int productId;
    private  int quantity;
    private int inventryId;
    private int price;
}
