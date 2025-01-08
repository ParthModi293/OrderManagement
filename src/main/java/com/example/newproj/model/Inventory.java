package com.example.newproj.model;

import lombok.Data;

@Data
public class Inventory {
    private int inventoryId;
    private int productId;
    private String quantity;
}
