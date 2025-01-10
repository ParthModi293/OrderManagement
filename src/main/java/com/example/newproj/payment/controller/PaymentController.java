package com.example.newproj.payment.controller;

import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.payment.service.PayemtServce;
import com.example.newproj.util.ResponseBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PayemtServce payemtServce;

    public PaymentController(PayemtServce payemtServce) {
        this.payemtServce = payemtServce;
    }

    @PostMapping
    public ResponseBean<?> paymentRequest(@RequestBody PaymentRequest paymentRequest) throws JsonProcessingException {
        ResponseBean<Object> objectResponseBean = payemtServce.savePayment(paymentRequest);
        return new ResponseBean<>(HttpStatus.OK,objectResponseBean);
    }



}
