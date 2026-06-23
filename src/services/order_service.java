package services;

import db.db_connection;
import models.CartItem;
import java.sql.*;
import java.util.List;

public class order_service {
    
    public boolean createOrder(int userId, List<CartItem> cartItems, 
                               String shippingAddress, String deliveryPhone, 
                               int paymentMethodId, String comment) {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement itemStmt = null;
        PreparedStatement deliveryStmt = null;
        PreparedStatement paymentStmt = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = db_connection.getConnection();
            conn.setAutoCommit(false);
            
            double totalAmount = 0;
            for (CartItem item : cartItems) {
                totalAmount += item.getTotalPrice();
            }
            
            String orderSql = "INSERT INTO orders (user_id, order_status_id, total_amount, shipping_address, delivery_phone, delivery_comment) " +
                             "VALUES (?, ?, ?, ?, ?, ?)";
            orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, userId);
            orderStmt.setInt(2, 1);
            orderStmt.setDouble(3, totalAmount);
            orderStmt.setString(4, shippingAddress);
            orderStmt.setString(5, deliveryPhone);
            orderStmt.setString(6, comment);
            
            int affectedRows = orderStmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }
            
            generatedKeys = orderStmt.getGeneratedKeys();
            int orderId;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return false;
            }
            
            String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_time, total_price) " +
                            "VALUES (?, ?, ?, ?, ?)";
            itemStmt = conn.prepareStatement(itemSql);
            
            for (CartItem item : cartItems) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getProduct().getId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getProduct().getPrice());
                itemStmt.setDouble(5, item.getTotalPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();
            
            String deliverySql = "INSERT INTO deliveries (order_id, delivery_status_id, delivery_address) " +
                                "VALUES (?, ?, ?)";
            deliveryStmt = conn.prepareStatement(deliverySql);
            deliveryStmt.setInt(1, orderId);
            deliveryStmt.setInt(2, 1);
            deliveryStmt.setString(3, shippingAddress);
            deliveryStmt.executeUpdate();
            
            String paymentSql = "INSERT INTO payments (order_id, payment_method_id, amount, status) " +
                               "VALUES (?, ?, ?, ?)";
            paymentStmt = conn.prepareStatement(paymentSql);
            paymentStmt.setInt(1, orderId);
            paymentStmt.setInt(2, paymentMethodId);
            paymentStmt.setDouble(3, totalAmount);
            paymentStmt.setString(4, "pending");
            paymentStmt.executeUpdate();
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (orderStmt != null) orderStmt.close();
                if (itemStmt != null) itemStmt.close();
                if (deliveryStmt != null) deliveryStmt.close();
                if (paymentStmt != null) paymentStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}