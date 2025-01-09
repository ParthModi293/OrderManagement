package com.example.newproj.model;

import lombok.Data;

import java.util.List;

@Data
public class Order {

    private int id;
    private List<ProductEntity> productEntities;
    private int totalAmount;
    private String userName;
    private int userId;

}
