package models;

import java.time.LocalDateTime;

// Модель заказа
public class order {

    private int id;
    private int userId;
    private double totalPrice;
    private LocalDateTime orderDate;

    public order(
            int id,
            int userId,
            double totalPrice,
            LocalDateTime orderDate
    ) {

        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}