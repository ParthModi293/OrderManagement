package com.example.newproj.order.controller;

import com.example.newproj.order.model.OrderRequest;
import com.example.newproj.order.service.OrderService;
import com.example.newproj.util.ResponseBean;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<?> saveOrder(@RequestBody OrderRequest orderRequest) throws Exception {
        ResponseBean<?> objectResponseBean = orderService.saveOrder(orderRequest);
        return new ResponseEntity<>(objectResponseBean, objectResponseBean.getRStatus());
    }

}
