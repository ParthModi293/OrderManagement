package com.example.newproj.payment.controller;

import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.payment.service.PaymentService;
import com.example.newproj.payment_Integration.service.StripeService;
import com.example.newproj.payment_Integration.service.dto.StripeResponse;
import com.example.newproj.util.ResponseBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {



    private final PaymentService payemtServce;

    public PaymentController(PaymentService payemtServce) {
        this.payemtServce = payemtServce;
    }

    @PostMapping
    public ResponseBean<?> paymentRequest(@RequestBody PaymentRequest paymentRequest) throws JsonProcessingException {


        ResponseBean<?> objectResponseBean = payemtServce.savePayment(paymentRequest);
        return new ResponseBean<>(HttpStatus.OK,objectResponseBean);
    }



}
