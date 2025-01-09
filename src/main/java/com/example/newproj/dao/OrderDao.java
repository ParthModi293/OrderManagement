package com.example.newproj.dao;

import com.example.newproj.model.*;
import com.example.newproj.util.SqlUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao {

    @Autowired
    private SqlUtil sqlUtil;

 /*   public void insertIntoOrder(OrderRequest orderRequest,double totalAmount){

        String sql = "INSERT INTO order_header(seller,user,total_amount) VALUES (:seller,:user,:total_amount)";


        Map<String, Object> param = new HashMap<>();
        param.put("seller", orderRequest.getSellerId());
        param.put("user", orderRequest.getUserId());
        param.put("total_amount", totalAmount);
        int orderId= sqlUtil.persistKey(sql, new MapSqlParameterSource(param));


    }*/


    public void insertIntoOrder(Order order,double totalAmount) {
        String orderInsertSql = "INSERT INTO order (user_name, total_amount) VALUES (:userName, :totalAmount)";
        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("userName", order.getUserName());
        orderParams.put("totalAmount", totalAmount);

        int orderId = sqlUtil.persist(orderInsertSql, new MapSqlParameterSource(orderParams));

        String productInsertSql = "INSERT INTO productEntity (order_id, product_id, product_name, seller_name, price, quantity) " +
                "VALUES (:orderId, :productId, :productName, :sellerName, :price, :qty)";

        for (ProductEntity product : order.getProductEntities()) {
            Map<String, Object> productParams = new HashMap<>();
            productParams.put("orderId", orderId);
            productParams.put("productId", product.getProductId());
            productParams.put("productName", product.getProductName());
            productParams.put("sellerName", product.getSellerName());
            productParams.put("price", product.getPrice());
            productParams.put("qty", product.getQuantity());

            sqlUtil.persist(productInsertSql, new MapSqlParameterSource(productParams));

        }
    }

   /* public void insertIntoOrderItem(int orderRequest){



        String sql="INSERT INTO order_item(order_id,product_id,inventory_id,price,qty) VALUES (:orderId,:productId,:inventoryId,:price,:qty)";

        Map<String, Object> param = new HashMap<>();
        param.put("seller", orderRequest.getSellerId());

        sqlUtil.persist(sql, new MapSqlParameterSource(param));
    }*/

    // New class 4 param
    //dto

/*    public List<ProductDto> getProductDto(OrderRequest orderRequest) throws Exception {

        for(ProductRequest od: orderRequest.getProducts()){

            String inventoryId = getInventory(od.productId);
            String price = getPrice(od.productId);

            ProductDto productDto = new ProductDto();
            productDto.setProductId(od.productId);
            productDto.setPrice(Integer.parseInt(price));
            productDto.setInventryId(Integer.parseInt(inventoryId));
            productDto.setQuantity(od.quantity);


        }
    }*/

    public String getInventory(int productId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        String sql = """
                select inventory_id 
                from inventory
                where product_id = :productId
                """;
        return sqlUtil.getString(sql);
    }

    public String getPrice(int productId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        String sql = """
                select price 
                from product
                where product_id = :productId
                """;
        return sqlUtil.getString(sql);
    }


    public String fetchProductName(int productId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        String sql = """
                select product_name 
                from product
                where product_id = :productId
                """;
        return sqlUtil.getString(sql);
    }

    public String fetchSellerName(int productId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        String sql = """
                select s.seller_name 
                from seller s join product p on s.seller_id = p.seller_id
                where p.product_id = :productId
                """;
        return sqlUtil.getString(sql);
    }

    public int fetchUserId(int userId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        String sql = """
                select user_id 
                from user
                where user_id = :userId
                """;
        return sqlUtil.getInteger(sql,new MapSqlParameterSource(map));
    }

    public String fetchUserName(int userId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        String sql = """
                select user_name 
                from user
                where user_id = :userId
                """;
        return sqlUtil.getString(sql);
    }







    /*public void batchInsertOrderItem(ProductDto productDto,int orderId) throws Exception {
        String sql = " INSERT INTO order_item(order_id,product_id,inventory_id,price,qty) VALUES (?,?,?,?,?) ";

        sqlUtil.getJdbcTemplate().batchUpdate(
                sql,
                productDto,
                productDto.size(),  // Batch size (tune this as needed)
                (ps, productRequest) -> {
                    try {
                        ps.setInt(1, orderId);
                        ps.setInt(2, productDto.getProductId());
                        ps.setInt(2, productRequest.);


                        ps.setString(2, objectMapper.writeValueAsString(segments));
                        ps.setString(3, objectMapper.writeValueAsString(segments));
                    } catch (Exception e) {
                        LogUtil.printErrorStackTraceLog(e);
                    }
                }
        );
    }*/

}
