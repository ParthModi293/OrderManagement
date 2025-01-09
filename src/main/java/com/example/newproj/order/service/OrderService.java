package com.example.newproj.order.service;

import com.example.newproj.order.dao.OrderDao;
import com.example.newproj.order.dto.userDto;
import com.example.newproj.order.model.Order;
import com.example.newproj.order.model.OrderRequest;
import com.example.newproj.order.model.ProductEntity;
import com.example.newproj.order.model.ProductRequest;
import com.example.newproj.util.ResponseBean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Transactional
    public ResponseBean<Object> saveOrder(OrderRequest orderRequest) throws Exception {
//        int s = orderDao.fetchUserId(orderRequest.getUserId());

        userDto userD = orderDao.fetchUserDto(orderRequest.getUserId());
        if (!(userD.getUserId() > 0)) {
            throw new Exception("user not found with Id: " + userD.getUserId());
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.29.28:8080/inventory/validate-product";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> productRequestHttpEntity = new HttpEntity<>(orderRequest, headers);
        ResponseEntity<String> responseEntity;
try {
   responseEntity = restTemplate.postForEntity(url, productRequestHttpEntity, String.class);
}catch (HttpClientErrorException e){
    ObjectMapper objectMapper = new ObjectMapper();
   ResponseBean responseBean = objectMapper.readValue(e.getResponseBodyAs(String.class),ResponseBean.class);


    return new ResponseBean<>(HttpStatus.BAD_REQUEST, responseBean.getDisplayMessage(), null, null);


}
        /*if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, responseEntity.getBody(), responseEntity.getBody(), null);
        }*/


        // If it is true then... Write query code to insert

        int totalAmount = 0;
        Order order = new Order();

//        String userName = orderDao.fetchUserName(orderRequest.getUserId());

        String productName = orderDao.fetchProductName(orderRequest.getUserId());

        List<ProductEntity> productEntityList = new ArrayList<>();
        for (ProductRequest productRequest : orderRequest.getProducts()) {

            String price = orderDao.getPrice(productRequest.getProductId());
            String sellerName = orderDao.fetchSellerName(productRequest.getProductId());

            ProductEntity productEntity = new ProductEntity();

            productEntity.setProductName(productName);
            productEntity.setQuantity(productRequest.getQuantity());
            productEntity.setPrice(Integer.parseInt(price));
            productEntity.setProductId(productRequest.getProductId());
            productEntity.setSellerName(sellerName);

            totalAmount = totalAmount + (productEntity.getPrice() * productRequest.getQuantity());

            productEntityList.add(productEntity);


        }
        order.setProductEntities(productEntityList);
        order.setUserId(orderRequest.getUserId());
        order.setTotalAmount(totalAmount);
        order.setUserName(userD.getUserName());
        order.setStatus("Pending");

        // pay
        //if payment is successful order insert and minus qty from the inventory

        int i = orderDao.insertIntoOrder(order, totalAmount);

//        order.setId(i);

        return new ResponseBean<>(HttpStatus.OK, i);

    }

    /*public ResponseEntity<?> checkProductQty(List<ProductRequest> productRequest) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "";
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            ProductRequest productRequest1 = new ProductRequest();
            for (ProductRequest pr : productRequest) {
                productRequest1.setProductId(pr.getProductId());
                productRequest1.setQuantity(pr.getQuantity());
            }

            HttpEntity<ProductRequest> productRequestHttpEntity = new HttpEntity<>(productRequest1, headers);


            ResponseEntity<?> res = restTemplate.postForEntity(url, productRequestHttpEntity, ResponseEntity.class);

            return new ResponseEntity<>(res.getBody(), res.getStatusCode());

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }*/

//    public ResponseEntity<?> inventoryUpdate(OrderRequest orderRequest) throws Exception {






}
