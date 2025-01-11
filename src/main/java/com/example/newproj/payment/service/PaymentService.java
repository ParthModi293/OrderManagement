package com.example.newproj.payment.service;


import com.example.newproj.common.InventoryRestTemplate;
import com.example.newproj.common.JacksonService;
import com.example.newproj.order.dao.OrderDao;
import com.example.newproj.payment.dao.PaymentDao;
import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.payment.model.PaymentStatus;
import com.example.newproj.payment.validation.PaymentRequestValidation;

import com.example.newproj.payment_Integration.service.StripeService;
import com.example.newproj.payment_Integration.service.dto.StripeResponse;
import com.example.newproj.util.ResponseBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class PaymentService {

    private final PaymentDao dao;

    private final OrderDao orderDao;

    private final InventoryRestTemplate restTemplate;
    private final PaymentRequestValidation paymentRequestValidation;
    private final ReentrantLock lock = new ReentrantLock();



    public PaymentService(PaymentDao dao, OrderDao orderDao, InventoryRestTemplate restTemplate, PaymentRequestValidation paymentRequestValidation) {
        this.dao = dao;
        this.orderDao = orderDao;
        this.restTemplate = restTemplate;
        this.paymentRequestValidation = paymentRequestValidation;
    }

    @Transactional
    public ResponseBean<?> savePayment(PaymentRequest paymentRequest) throws JsonProcessingException {

        ResponseBean<Void> validatePayment = paymentRequestValidation.paymentRequestValidation(paymentRequest);
        if (validatePayment.getRStatus() != HttpStatus.OK) {
            return validatePayment;
        }

        if (paymentRequest.getStatus() != null && paymentRequest.getStatus().equals(PaymentStatus.PASS)) {
            dao.savePayment(paymentRequest);

            try {
                lock.lock();
              restTemplate.decreaseProductQuantity(paymentRequest);
            } catch (HttpClientErrorException e) {
                ResponseBean responseBean = JacksonService.getInstance().convertJsonToDto(e.getResponseBodyAs(String.class), ResponseBean.class);
                return new ResponseBean<>(HttpStatus.BAD_REQUEST, responseBean.getDisplayMessage(), null, null);
            }
            finally {
                lock.unlock();
            }




            orderDao.updateOrderStatus(paymentRequest.getOrderId());

            return new ResponseBean<>(HttpStatus.OK, "Payment Successful", null, null);
        }
        return new ResponseBean<>(HttpStatus.BAD_REQUEST, "paymentRequest  is not passed valid");
    }
}
