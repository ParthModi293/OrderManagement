package com.example.newproj.payment.dao;

import com.example.newproj.payment.domain.Payment;
import com.example.newproj.payment.model.PaymentRequest;
import com.example.newproj.util.SqlUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PaymentDao {

    private final SqlUtil sqlUtil;

    public PaymentDao(SqlUtil sqlUtil) {
        this.sqlUtil = sqlUtil;
    }

    public void savePayment(PaymentRequest paymentRequest) {
        String orderInsertSql = "INSERT INTO payment (order_id,type,status) VALUES (:orderId, :type, :status)";
        Map<String, Object> params = new HashMap<>();

        params.put("orderId", paymentRequest.getOrderId());
        params.put("type", paymentRequest.getType().toString());
        params.put("status", paymentRequest.getStatus().toString());

        sqlUtil.persist(orderInsertSql, new MapSqlParameterSource(params));
    }

    public int getPayment(int orderId) {
        String sql = "select count(*) from payment where order_id=:orderId";
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);

        return  sqlUtil.getInteger(sql, new MapSqlParameterSource(params));
    }


}
