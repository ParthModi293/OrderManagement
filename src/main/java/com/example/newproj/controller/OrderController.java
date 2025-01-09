package com.example.newproj.controller;

import com.example.newproj.model.OrderRequest;
import com.example.newproj.model.ProductRequest;
import com.example.newproj.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<?> saveOrder(@RequestBody OrderRequest orderRequest) throws Exception {

            orderService.saveOrder(orderRequest);

            return ResponseEntity.ok().build();

    }

}
