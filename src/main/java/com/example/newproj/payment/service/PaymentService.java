package com.example.newproj.payment.service;


import com.example.newproj.common.InventoryRestTemplate;
import com.example.newproj.order.dao.OrderDao;
import com.example.newproj.payment.dao.PaymentDao;
import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.payment.validation.PaymentRequestValidation;
import com.example.newproj.util.ResponseBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentDao dao;

    private final OrderDao orderDao;

    private final InventoryRestTemplate restTemplate;
    private final PaymentRequestValidation paymentRequestValidation;

    public PaymentService(PaymentDao dao, OrderDao orderDao, InventoryRestTemplate restTemplate, PaymentRequestValidation paymentRequestValidation) {
        this.dao = dao;
        this.orderDao = orderDao;
        this.restTemplate = restTemplate;
        this.paymentRequestValidation = paymentRequestValidation;
    }

    @Transactional
    public ResponseBean<?> savePayment(PaymentRequest paymentRequest) throws JsonProcessingException {

        ResponseBean<Void> responseBean = paymentRequestValidation.paymentRequestValidation(paymentRequest);
        if (responseBean.getRStatus() != HttpStatus.OK) {
            return responseBean;
        }

        if(paymentRequest.getStatus() != null && paymentRequest.getStatus().equals("PASS")) {
            dao.savePayment(paymentRequest);


            ResponseBean<Object> objectResponseBean = restTemplate.decreaseProductQuantity(paymentRequest);
            if(objectResponseBean.getRStatus()!=HttpStatus.OK) {
                return new ResponseBean<>(HttpStatus.BAD_REQUEST, objectResponseBean.getDisplayMessage(), objectResponseBean.getRMsg(), null);
            }

            orderDao.updateOrderStatus(paymentRequest.getOrderId());

            return new ResponseBean<>(HttpStatus.OK, "Payment Successful", null, null);
        }
        return new ResponseBean<>(HttpStatus.BAD_REQUEST,"paymentRequest  is not passed valid");
    }
}
