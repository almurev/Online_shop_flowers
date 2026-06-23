package services;

import db.db_connection;
import models.product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class product_service {
    
    // Получить все товары
    public List<product> getAllProducts() {
        List<product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "ORDER BY p.id";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                product product = new product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("category_id"),
                    rs.getString("image_path"),
                    rs.getInt("count_in_stock")
                );
                product.setCategoryName(rs.getString("category_name"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    // Получить товар по ID
    public product getProductById(int id) {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.id = ?";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                product prod = new product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("category_id"),
                    rs.getString("image_path"),
                    rs.getInt("count_in_stock")
                );
                prod.setCategoryName(rs.getString("category_name"));
                return prod;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Проверить наличие товара на складе
    public boolean isProductAvailable(int productId, int requestedQuantity) {
        String sql = "SELECT count_in_stock FROM products WHERE id = ?";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int stock = rs.getInt("count_in_stock");
                return stock >= requestedQuantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Добавить новый товар
    public boolean addProduct(String name, String description, double price, 
                              int categoryId, int countInStock) {
        return addProduct(name, description, price, categoryId, null, countInStock);
    }
    
    public boolean addProduct(String name, String description, double price, 
                              int categoryId, String imagePath, int countInStock) {
        if (name == null || name.isEmpty()) return false;
        if (price <= 0) return false;
        if (countInStock < 0) return false;
        
        String sql = "INSERT INTO products (name, description, price, category_id, image_path, count_in_stock) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setInt(4, categoryId);
            stmt.setString(5, imagePath);
            stmt.setInt(6, countInStock);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Обновить товар
    public boolean updateProduct(int id, String name, String description, double price, 
                                 int categoryId, int countInStock) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, " +
                     "category_id = ?, count_in_stock = ? WHERE id = ?";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setInt(4, categoryId);
            stmt.setInt(5, countInStock);
            stmt.setInt(6, id);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Уменьшить количество товара (при покупке)
    public boolean decreaseStock(int productId, int quantity) {
        String sql = "UPDATE products SET count_in_stock = count_in_stock - ? WHERE id = ? AND count_in_stock >= ?";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Получить все категории
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT name FROM categories ORDER BY id";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    // Получить ID категории по названию
    public int getCategoryIdByName(String categoryName) {
        String sql = "SELECT id FROM categories WHERE name = ?";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}