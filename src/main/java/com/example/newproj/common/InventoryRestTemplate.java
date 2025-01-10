package com.example.newproj.common;

import com.example.newproj.order.dao.OrderDao;
import com.example.newproj.order.model.Order;
import com.example.newproj.order.model.OrderRequest;
import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.util.ResponseBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class InventoryRestTemplate {

    private final OrderDao orderDao;

    @Value("${url}")
    private String url;

    public InventoryRestTemplate(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public ResponseBean<Object>  checkProductQuantity(OrderRequest orderRequest) {

            RestTemplate restTemplate = new RestTemplate();
            String requestUrl = url + "/inventory/validate-product";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> productRequestHttpEntity = new HttpEntity<>(orderRequest, headers);

            ResponseEntity<String> responseEntity=  restTemplate.exchange(requestUrl, HttpMethod.POST, productRequestHttpEntity, String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, responseEntity.getBody(), responseEntity.getBody(), null);
        }
        return new ResponseBean<>(HttpStatus.OK);

    }

    public ResponseBean<Object> decreaseProductQuantity(PaymentRequest paymentRequest) throws JsonProcessingException {

        Order order = orderDao.getOrder(paymentRequest.getOrderId());

        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = url + "/inventory/update-qty";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> productRequestHttpEntity = new HttpEntity<>(order, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, productRequestHttpEntity, String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, responseEntity.getBody(), responseEntity.getBody(), null);
        }
        return new ResponseBean<>(HttpStatus.OK);
    }


}
