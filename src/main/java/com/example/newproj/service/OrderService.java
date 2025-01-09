package com.example.newproj.service;

import com.example.newproj.dao.OrderDao;
import com.example.newproj.model.Order;
import com.example.newproj.model.OrderRequest;
import com.example.newproj.model.ProductEntity;
import com.example.newproj.model.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
public class OrderService {

    private final OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public ResponseEntity<?> saveOrder(OrderRequest orderRequest) throws Exception {
        int s = orderDao.fetchUserId(orderRequest.getUserId());
        if (!(s > 0)) {
            throw new Exception("user not found with Id: " + s);
        }


        RestTemplate restTemplate = new RestTemplate();
        String url = "";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderRequest> productRequestHttpEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> res = restTemplate.postForEntity(url, productRequestHttpEntity, String.class);

        if (!(res.getStatusCode().is2xxSuccessful())) {
            return new ResponseEntity<>(, res.getStatusCode());
        }


        // If it is true then... Write query code to insert

        int totalAmount = 0;
        Order order = new Order();

        String userName = orderDao.fetchUserName(orderRequest.getUserId());
        String productName = orderDao.fetchProductName(orderRequest.getUserId());

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

            order.getProductEntities().add(productEntity);
        }
        order.setUserId(orderRequest.getUserId());
        order.setTotalAmount(totalAmount);
        order.setUserName(userName);

        // pay
        //if payment is successful order insert and minus qty from the inventory




        return null;

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

}
