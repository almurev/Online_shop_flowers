package services;

import models.CartItem;
import models.product;
import java.util.ArrayList;
import java.util.List;

public class cart_service {
    private List<CartItem> cartItems = new ArrayList<>();
    private product_service productService = new product_service();

    // Добавить товар в корзину
    public boolean addToCart(int productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        // Проверяем наличие товара
        product prod = productService.getProductById(productId);
        if (prod == null) {
            return false;
        }

        // Проверяем, есть ли на складе
        if (prod.getCountInStock() < quantity) {
            return false;
        }

        // Проверяем, есть ли уже такой товар в корзине
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                int newQuantity = item.getQuantity() + quantity;
                if (prod.getCountInStock() >= newQuantity) {
                    item.setQuantity(newQuantity);
                    return true;
                }
                return false;
            }
        }

        // Добавляем новый товар
        cartItems.add(new CartItem(prod, quantity));
        return true;
    }

    // Удалить товар из корзины
    public boolean removeFromCart(int productId) {
        return cartItems.removeIf(item -> item.getProduct().getId() == productId);
    }

    // Обновить количество товара в корзине
    public boolean updateQuantity(int productId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeFromCart(productId);
        }

        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                product prod = productService.getProductById(productId);
                if (prod != null && prod.getCountInStock() >= newQuantity) {
                    item.setQuantity(newQuantity);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    // Получить все товары в корзине
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    // Очистить корзину
    public void clearCart() {
        cartItems.clear();
    }

    // Получить общую сумму
    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Получить общее количество товаров в корзине
    public int getTotalItems() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    // Проверить, пуста ли корзина
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}