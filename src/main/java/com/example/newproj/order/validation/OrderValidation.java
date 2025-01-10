package com.example.newproj.order.validation;

import com.example.newproj.order.dao.OrderDao;
import com.example.newproj.order.model.OrderRequest;
import com.example.newproj.util.ResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderValidation {

    private final OrderDao orderDao;

    public OrderValidation(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public ResponseBean<Void> userIdValidation(OrderRequest orderRequest) {

        if(orderRequest.getUserId() <=0) {
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, "Please enter valid userId", "Please enter valid userId", null);

        }

        int userId = orderDao.fetchUserId(orderRequest.getUserId());

        if(orderRequest.getUserId() != userId) {
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, "Invalid user", "Invalid user", null);
        }
        return new ResponseBean<>(HttpStatus.OK, "OK");

    }

}
