package com.example.newproj.payment.service;


import com.example.newproj.order.dao.OrderDao;
import com.example.newproj.order.model.Order;
import com.example.newproj.payment.dao.PaymentDao;
import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.util.ResponseBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PayemtServce {

    private final PaymentDao dao;

    private final OrderDao orderDao;

    public PayemtServce(PaymentDao dao, OrderDao orderDao) {
        this.dao = dao;
        this.orderDao = orderDao;
    }

    @Transactional
    public ResponseBean<Object> savePayment(PaymentRequest paymentRequest) throws JsonProcessingException {

        if(paymentRequest.getStatus() != null && paymentRequest.getStatus().equals("PASS")) {
            dao.savePayment(paymentRequest);

            Order order = orderDao.getOrder(paymentRequest.getOrderId());

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://192.168.29.28:8080/inventory/update-qty";
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> productRequestHttpEntity = new HttpEntity<>(order, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, productRequestHttpEntity, String.class);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                return new ResponseBean<>(HttpStatus.BAD_REQUEST, responseEntity.getBody(), responseEntity.getBody(), null);
            }

            orderDao.updateOrderStatus(paymentRequest.getOrderId());

            return new ResponseBean<>(HttpStatus.OK, responseEntity.getBody(), responseEntity.getBody(), null);
        }
        return new ResponseBean<>();
    }
}
