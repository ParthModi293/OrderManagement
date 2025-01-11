package com.example.newproj.payment_Integration.service;

import com.example.newproj.order.dao.OrderDao;
import com.example.newproj.order.model.Order;
import com.example.newproj.order.model.ProductEntity;
import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.payment.service.PaymentService;
import com.example.newproj.payment_Integration.service.dto.StripeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {

    private PaymentService paymentService;

    @Value("${secret.key}")
    private String secretKey;

    private final OrderDao orderDao;

    public StripeService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }


    public StripeResponse checkoutPayment(int orderId) throws JsonProcessingException {

        Order order = orderDao.getOrder(orderId);

        Stripe.apiKey = secretKey;

        String defaultCurrency = "USD";

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        // Iterate over each product in the order
        for (ProductEntity productEntity : order.getProductEntities()) {
            // Create ProductData for the product name
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(productEntity.getProductName())
                            .build();

            // Create PriceData with the product details
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(defaultCurrency)
                            .setUnitAmount((long) productEntity.getPrice())
                            .setProductData(productData)
                            .build();

            // Create a LineItem for this product
            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) productEntity.getQuantity())
                            .setPriceData(priceData)
                            .build();

            lineItems.add(lineItem); // Add the line item to the list
        }

        // Create session parameters with all line items
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/payment")
                        .setCancelUrl("http://localhost:8080/order/cancel")
                        .addAllLineItem(lineItems) // Add all line items to the session
                        .build();

        Session session = null;
        try {
            session = Session.create(params);

        } catch (StripeException e) {
            //log the error
            e.printStackTrace();
        }

        return StripeResponse
                .builder()
                .status("SUCCESS")
                .message("Payment session created ")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }


}






/*

    public StripeResponse checkoutPayment(PaymentRequest paymentRequest) throws JsonProcessingException {

        Order order = orderDao.getOrder(paymentRequest.getOrderId());

        Stripe.apiKey = secretKey;


        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        // Iterate over each product in the order
        for (ProductEntity productEntity : order.getProductEntities()) {
            // Create ProductData for the product name
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(productEntity.getProductName())
                            .build();

            // Create PriceData with the product details
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(paymentRequest.getCurrency() != null ? paymentRequest.getCurrency() : "USD")
                            .setUnitAmount((long) productEntity.getPrice())
                            .setProductData(productData)
                            .build();

            // Create a LineItem for this product
            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) productEntity.getQuantity())
                            .setPriceData(priceData)
                            .build();

            lineItems.add(lineItem); // Add the line item to the list
        }

        // Create session parameters with all line items
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/order/success")
                        .setCancelUrl("http://localhost:8080/order/cancel")
                        .addAllLineItem(lineItems) // Add all line items to the session
                        .build();

        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            //log the error
            e.printStackTrace();
        }

        return StripeResponse
                .builder()
                .status("SUCCESS")
                .message("Payment session created ")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }
*/




