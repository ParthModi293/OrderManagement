package com.example.newproj.payment.validation;

import com.example.newproj.payment.dao.PaymentDao;
import com.example.newproj.payment.domain.Payment;
import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.payment.model.PaymentStatus;
import com.example.newproj.util.ResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestValidation {

    private final PaymentDao paymentDao;

    public PaymentRequestValidation(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public ResponseBean<Void> paymentRequestValidation(PaymentRequest paymentRequest) {

        if (paymentRequest.getOrderId() <= 0) {
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, "Please enter valid order id", "Please enter valid order id", null);
        }
        int paymentCount = paymentDao.getPayment(paymentRequest.getOrderId());
        if (paymentCount > 0) {
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, "Payment request already avalaible", "Payment request already avalaible", null);
        }
        if (!(paymentRequest.getStatus() != null && paymentRequest.getStatus().equals(PaymentStatus.PASS))) {
            return new ResponseBean<>(HttpStatus.BAD_REQUEST, "Payment status is fail", "Payment status is fail", null);
        }
        return new ResponseBean<>(HttpStatus.OK, "OK");
    }
}
