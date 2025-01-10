package com.example.newproj.order.service;

import com.example.newproj.common.InventoryRestTemplate;
import com.example.newproj.order.dao.OrderDao;
import com.example.newproj.order.dto.UserDto;
import com.example.newproj.order.model.*;
import com.example.newproj.order.validation.OrderValidation;
import com.example.newproj.util.ResponseBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final InventoryRestTemplate inventoryRestTemplate;
    private final OrderValidation orderValidation;

    public OrderService(OrderDao orderDao, InventoryRestTemplate inventoryRestTemplate, OrderValidation orderValidation) {
        this.orderDao = orderDao;
        this.inventoryRestTemplate = inventoryRestTemplate;
        this.orderValidation = orderValidation;
    }

    @Transactional
    public ResponseBean<?> saveOrder(OrderRequest orderRequest) throws Exception {

        ResponseBean<Void> voidResponseBean = orderValidation.userIdValidation(orderRequest);

        if(voidResponseBean.getRStatus()!=HttpStatus.OK){
            return voidResponseBean;
        }

        UserDto userD = orderDao.fetchUserDto(orderRequest.getUserId());
        if (userD.getUserId() < 0) {
            throw new Exception("user not found with Id: " + userD.getUserId());
        }
      /*  RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.29.28:8080/inventory/validate-product";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> productRequestHttpEntity = new HttpEntity<>(orderRequest, headers);
        try {
            restTemplate.postForEntity(url, productRequestHttpEntity, String.class);
        } catch (HttpClientErrorException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBean responseBean = objectMapper.readValue(e.getResponseBodyAs(String.class), ResponseBean.class);
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, responseBean.getDisplayMessage(), null, null);
        }*/

        try {
            inventoryRestTemplate.checkProductQuantity(orderRequest);
        } catch (HttpClientErrorException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBean responseBean = objectMapper.readValue(e.getResponseBodyAs(String.class), ResponseBean.class);
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, responseBean.getDisplayMessage(), null, null);

        }

        // If it is true then... Write query code to insert

        int totalAmount = 0;
        Order order = new Order();

        List<Integer> productIds =
                orderRequest.getProducts().stream().map(ProductRequest::getProductId).collect(Collectors.toList());


        List<ProductDto> productDto = orderDao.fetchProductDto(productIds);

        Map<Integer, ProductDto> productmap = productDto.stream().collect(Collectors.toMap(ProductDto::getProductId, dto -> dto));

        List<ProductEntity> productEntityList = new ArrayList<>();
        for (ProductRequest productRequest : orderRequest.getProducts()) {

            ProductDto product = productmap.get(productRequest.getProductId());

            ProductEntity productEntity = new ProductEntity();
            productEntity.setProductName(product.getProductName());
            productEntity.setQuantity(productRequest.getQuantity());
            productEntity.setPrice(product.getPrice());
            productEntity.setProductId(productRequest.getProductId());
            productEntity.setSellerName(product.getSellerName());

            totalAmount = totalAmount + (productEntity.getPrice() * productRequest.getQuantity());
            productEntityList.add(productEntity);
        }
        order.setProductEntities(productEntityList);
        order.setUserId(orderRequest.getUserId());
        order.setTotalAmount(totalAmount);
        order.setUserName(userD.getUserName());
        order.setStatus(String.valueOf(OrderStatus.PENDING));

        int orderId = orderDao.insertIntoOrder(order, totalAmount);

        Map<String, Integer> map = new HashMap<>();
        map.put("orderId", orderId);


        return new ResponseBean<>(HttpStatus.OK, map);

    }
}


