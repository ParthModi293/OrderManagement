package com.example.newproj.payment.controller;

import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.payment.service.PayemtServce;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PayemtServce payemtServce;

    public PaymentController(PayemtServce payemtServce) {
        this.payemtServce = payemtServce;
    }

    @PostMapping
    public ResponseEntity<?> paymentRequest(@RequestBody PaymentRequest paymentRequest) throws JsonProcessingException {
        payemtServce.savePayment(paymentRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
